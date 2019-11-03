from math import floor, ceil, isclose
from typing import List, Tuple, Dict, Sequence, Generic, TypeVar

import bpy
from bpy.types import PoseBone, Object, NlaTrack
from mathutils import Matrix

T = TypeVar('T')
class TrackedObject(Generic[T]):
    tracked: T
    name: str

    def __init__(self, tracked: T, name: str):
        pass

    def sample(self) -> Dict[str, Sequence[float]]:
        """Samples the object's current state."""
        pass

    def clear(self):
        """Clears the object's transformation. This is called before starting an animation to prevent lingering state"""
        pass

    def default(self) -> Dict[str, Sequence[float]]:
        """Returns the default state of this object. This is used to exclude keys that have no animation."""
        pass


class TrackedPoseBone(TrackedObject[PoseBone]):
    bone: PoseBone
