package de.javagl.obj;

public class DefaultObjBoneIndex implements ObjBoneIndex {
    private int armature;
    private int bone;

    public DefaultObjBoneIndex(int armature, int bone) {
        this.armature = armature;
        this.bone = bone;
    }

    public DefaultObjBoneIndex(ObjBoneIndex other) {
        this.armature = other.getArmature();
        this.bone = other.getBone();
    }

    @Override
    public int getArmature() {
        return 0;
    }

    @Override
    public int getBone() {
        return 0;
    }
}
