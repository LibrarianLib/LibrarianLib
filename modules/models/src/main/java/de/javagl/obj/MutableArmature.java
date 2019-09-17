package de.javagl.obj;

public interface MutableArmature extends Armature {
    /**
     * Adds the passed bone to this armature
     */
    void addBone(Bone bone);
}
