package de.javagl.obj;

import java.util.Objects;

/**
 * Interface for armature/bone ids and weights
 */
public interface ObjBoneIndex {
    /**
     * Return the armature index (zero-indexed)
     *
     * @return The armature index
     */
    int getArmature();

    /**
     * Return the bone index (zero-indexed)
     *
     * @return The bone index
     */
    int getBone();

    public static boolean equals(ObjBoneIndex a, Object o) {
        if (a == o) return true;
        if (!(o instanceof ObjBoneIndex)) return false;
        ObjBoneIndex that = (ObjBoneIndex) o;
        return a.getArmature() == that.getArmature() &&
                a.getBone() == that.getBone();
    }

    public static int hashCode(ObjBoneIndex index) {
        return Objects.hash(index.getArmature(), index.getBone());
    }
}
