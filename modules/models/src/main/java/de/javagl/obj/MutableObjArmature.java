package de.javagl.obj;

public interface MutableObjArmature extends ObjArmature {
    /**
     * Adds the passed bone to this armature
     */
    void addBone(ObjBone bone);
}
