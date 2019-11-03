from math import floor, ceil, isclose
from typing import List, Tuple, Dict, Sequence, Any, TypeVar, Generic

import bpy
from bpy.types import PoseBone, Object, NlaTrack
from mathutils import Matrix


T = TypeVar('T')


class AnimatedObject:
    def clear(self):
        """Clears the object's transformation. This is called before starting an animation to prevent lingering state"""
        pass


class TrackedObject(Generic[T]):
    def __init__(self, tracked: T, name: str):
        self.tracked = tracked
        self.name = name

    def sample(self) -> Dict[str, Sequence[float]]:
        """Samples the object's current state."""
        pass

    def default(self) -> Dict[str, Sequence[float]]:
        """Returns the default state of this object. This is used to exclude keys that have no animation."""
        pass

    def __repr__(self) -> str:

        relative_matrix = self.matrix
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


class TrackedPoseBone(TrackedObject):

    def __init__(self, bone: PoseBone):
        super().__init__(bone, bone.name)
        self.bone = bone

    def sample(self) -> Dict[str, Sequence[float]]:
        rest_inv = self.bone.bone.matrix_local.copy().inverted()
        if self.bone.parent:
            parent_inv = self.bone.parent.matrix.copy().inverted()
            parent_rest = self.bone.parent.bone.matrix_local.copy()
        else:
            parent_inv = Matrix()
            parent_rest = Matrix()
        matrix = rest_inv * parent_rest * parent_inv * self.bone.matrix.copy()

        translation = matrix.translation
        rotation = matrix.to_quaternion()

        return {
            'pos': (translation.x, translation.y, translation.z),
            'rot': (rotation.w, rotation.x, rotation.y, rotation.z),
        }

    def clear(self):
        self.bone.matrix_basis = Matrix()

    def default(self) -> Dict[str, Sequence[float]]:
        return {
            'pos': (0, 0, 0),
            'rot': (1, 0, 0, 0)
        }

