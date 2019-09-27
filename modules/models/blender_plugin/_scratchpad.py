import bpy
from math import floor, ceil, isclose
from mathutils import Matrix, Vector, Quaternion
from collections import namedtuple
import pprint
import textwrap

from typing import List, Tuple, Iterable, Dict, Sequence
from bpy.types import Armature, Bone, PoseBone, AnimData, Object, NlaTrack


# https://stackoverflow.com/a/7718776/1541907
def block(function):
    """Immediately calls and discards the decorated function, making it act like an anonymous block."""
    function()


pp = pprint.PrettyPrinter(indent=4)


class Sample:
    """The value of a property as sampled at a specific frame"""
    frame: int
    value: float

    def __init__(self, frame: int, value: float):
        self.frame = frame
        self.value = value

    def slope_to(self, other: 'Sample') -> float:
        return (other.value - self.value) / (other.frame - self.frame)

    def __str__(self) -> str:
        return '%3d - %f' % (self.frame, self.value)

    precision: float = 1e-5


class KeySample(Sample):
    """A sample that isn't eligible for optimization"""

    def __init__(self, other: Sample):
        super().__init__(other.frame, other.value)

    def __str__(self) -> str:
        return '%3d # %f (key)' % (self.frame, self.value)


class Curve:
    samples: List[Sample]

    def __init__(self, samples: Sequence[Sample]):
        self.samples = [it for it in samples]

    def __str__(self) -> str:
        if len(self.samples) == 0:
            return '[]'
        return '[\n' + textwrap.indent('\n'.join([str(it) for it in self.samples]), '    ') + '\n]'

    def copy(self) -> 'Curve':
        return Curve(self.samples)

    def maximum_error(self, start_index: int, end_index: int) -> float:
        if start_index > end_index:
            raise IndexError('start index %d is >= end index %d' % (start_index, end_index))
        if start_index + 1 >= end_index:
            return 0
        start = self.samples[start_index]
        end = self.samples[end_index]
        slope = start.slope_to(end)

        # calculate the difference between the value that would be expected when lerping and the actual value
        def error(sample):
            expected = start.value + (sample.frame - start.frame) * slope
            return sample.value - expected

        return max([abs(error(self.samples[i])) for i in range(start_index + 1, end_index)])

    def optimized(self):
        """Creates an optimized copy of this curve"""

        # zero, one, or two samples can't be further optimized
        if len(self.samples) <= 2:
            return self.copy()

        value_range = (min([point.value for point in self.samples]), max([point.value for point in self.samples]))
        error_tolerance = (value_range[1] - value_range[0]) * 0.01

        curve = self.copy()

        curve.samples[0] = KeySample(curve.samples[0])
        curve.samples[-1] = KeySample(curve.samples[-1])
        curve = curve.__approximate(Sample.precision)
        curve = curve.__approximate(error_tolerance)

        return curve

    def __collapse_linear(self) -> 'Curve':
        """Finds sequences of samples with equal slope and replaces them with two key samples at the endpoints"""
        if len(self.samples) <= 2:
            return self.copy()

        out = [KeySample(self.samples[0])]
        is_linear = False
        for i in range(1, len(self.samples) - 1):
            current_sample = self.samples[i]
            next_sample = self.samples[i + 1]
            is_linear_continuation = isclose(
                out[-1].slope_to(current_sample),
                current_sample.slope_to(next_sample),
                rel_tol=Sample.precision
            )
            if is_linear_continuation:
                if not is_linear:
                    out[-1] = KeySample(out[-1])  # make the starting point of this segment a key sample
                is_linear = True
            else:
                if is_linear:
                    out.append(KeySample(current_sample))
                else:
                    out.append(current_sample)
                is_linear = False
            if is_linear_continuation and isinstance(current_sample, KeySample):
                # key samples will be inserted even if the keyframe is a linear continuation
                out.append(current_sample)

        out.append(KeySample(self.samples[-1]))

        return Curve(out)

    # graphical explanation: https://i.imgur.com/Gtb7pKl.png
    def __approximate(self, tolerance: float) -> 'Curve':
        """Approximates this curve, ensuring the approximation error will never exceed the passed tolerance"""
        if len(self.samples) <= 2:
            return self.copy()

        latest_sample = 0

        # the first value will always be present
        optimized = [self.samples[0]]
        for i in range(1, len(self.samples) - 1):
            # if the next point introduces too much error, the approximation segment ends here
            if self.maximum_error(latest_sample, i + 1) > tolerance:
                if latest_sample < i - 1:
                    # make the endpoints key samples, otherwise the approximations themselves might be optimized away
                    # in future passes
                    optimized[-1] = KeySample(optimized[-1])
                    optimized.append(KeySample(self.samples[i]))
                else:
                    # if no points were actually optimized out, just append this point
                    optimized.append(self.samples[i])
                latest_sample = i
        optimized.append(self.samples[-1])

        return Curve(optimized)


class BoneState:
    name: str
    matrix: Matrix
    rest_matrix: Matrix

    def __init__(self, bone: PoseBone):
        self.name = bone.name
        self.matrix = bone.matrix.copy()
        self.rest_matrix = bone.bone.matrix_local.copy()

    def get_channels(self) -> List[Tuple[str, float]]:
        relative_matrix = self.rest_matrix.inverted() * self.matrix
        translation = relative_matrix.translation
        rotation = relative_matrix.to_quaternion()

        return [
            ('tx', translation.x),
            ('ty', translation.y),
            ('tz', translation.z),
            ('rw', rotation.w),
            ('rx', rotation.x),
            ('ry', rotation.y),
            ('rz', rotation.z),
        ]

    def __repr__(self) -> str:
        relative_matrix = self.rest_matrix.inverted() * self.matrix
        translation = relative_matrix.translation
        rotation = relative_matrix.to_quaternion()
        out = self.name
        if isclose(translation.x, 0) and isclose(translation.y, 0) and isclose(translation.z, 0):
            out += ' ()'
        else:
            out += ' (%f, %f, %f)' % translation.to_tuple()

        if isclose(rotation.w, 1) and isclose(rotation.x, 0) and isclose(rotation.y, 0) and isclose(rotation.z, 0):
            out += ' ()'
        else:
            out += ' (%f, %f, %f, %f)' % (rotation.w, rotation.x, rotation.y, rotation.z)
        return out

    default_channels: Dict[str, float] = {
        'tx': 0,
        'ty': 0,
        'tz': 0,
        'rw': 1,
        'rx': 0,
        'ry': 0,
        'rz': 0,
    }


Frame = List[BoneState]


class Animation:
    armature: Armature
    name: str
    frame_range: range
    frames: List[Frame]

    def __init__(self, armature_object: Object, track: NlaTrack):
        # noinspection PyTypeChecker
        self.armature = armature_object.data
        self.name = track.name
        self.frames = []

        track.is_solo = True

        for pb in armature_object.pose.bones:
            pb.matrix_basis = Matrix()

        start = int(floor(min([strip.frame_start for strip in track.strips])))
        end = int(ceil(max([strip.frame_end for strip in track.strips])))

        self.frame_range = range(start, end + 1)

        for f in self.frame_range:
            bpy.context.scene.frame_set(f)
            self.frames.append([BoneState(bone) for bone in armature_object.pose.bones])

        bpy.context.scene.frame_set(0)
        track.is_solo = False
        for pb in armature_object.pose.bones:
            pb.matrix_basis = Matrix()

    def get_curves(self) -> Dict[str, Curve]:
        start = self.frame_range.start
        bones: Dict[str, Dict[str, List[Sample]]] = {}
        for idx, states in enumerate(self.frames):
            frame = start + idx
            for bone in states:
                curves = bones.setdefault(bone.name, {})
                for channel, value in bone.get_channels():
                    curves.setdefault(channel, []).append(Sample(frame, value))
        all_curves: Dict[str, Curve] = {}
        for bone, curves in bones.items():
            for channel, samples in curves.items():
                default_value = BoneState.default_channels[channel]
                is_empty = all(isclose(it.value, default_value, rel_tol=Sample.precision) for it in samples)
                if not is_empty:
                    all_curves['%s.%s' % (bone, channel)] = Curve(samples)
        return all_curves


def get_animations(armature_object: Object):
    # noinspection PyTypeChecker
    tracks: List[NlaTrack] = armature_object.animation_data.nla_tracks
    # tracks = armature_object.animation_data.nla_tracks
    return [Animation(armature_object, track) for track in tracks if len(track.strips) > 0]


target_object = bpy.data.objects['Armature']
animations = get_animations(target_object)
for animation in animations:
    print('Track `%s`:' % animation.name)
    curves = {channel: curve.optimized() for channel, curve in animation.get_curves().items()}
    for channel, curve in curves.items():
        print(textwrap.indent('%s: %s' % (channel, str(curve)), '    '))



