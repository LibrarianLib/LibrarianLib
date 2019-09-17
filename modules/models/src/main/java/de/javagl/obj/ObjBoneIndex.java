package de.javagl.obj;

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
}
