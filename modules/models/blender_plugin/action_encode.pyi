from mathutils import Matrix, Vector, Quaternion
from typing import List, Tuple, Iterable, Dict, Sequence
from bpy.types import Armature, Bone, PoseBone, AnimData, Object, NlaTrack


class Sample:
    frame: int
    value: float

    def slope_to(self, other: 'Sample') -> float:
        pass

    precision: float


class KeySample(Sample):
    pass


class Curve:
    samples: List[Sample]

    def __init__(self, samples: Sequence[Sample]):
        pass

    def maximum_error(self, start_index: int, end_index: int) -> float:
        pass

    def copy(self) -> 'Curve':
        pass

    def optimized(self) -> 'Curve':
        pass

    def __approximate(self, tolerance: float) -> 'Curve':
        pass


class BoneState:
    name: str
    matrix: Matrix

    default_channels: Dict[str, float]

    def get_channels(self) -> List[Tuple[str, float]]:
        pass


Frame = List[BoneState]


class Animation:
    armature: Armature
    name: str
    frame_range: range
    frames: List[Frame]

    def __init__(self, armature_object: Object, track: NlaTrack):
        pass

    def get_curves(self) -> Dict[str, Curve]:
        pass


def get_animations(armature_object: Object) -> List[Animation]:
    pass
