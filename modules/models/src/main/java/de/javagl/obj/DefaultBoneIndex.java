package de.javagl.obj;

public class DefaultBoneIndex implements BoneIndex {
    private int armature;
    private int bone;

    public DefaultBoneIndex(int armature, int bone) {
        this.armature = armature;
        this.bone = bone;
    }

    public DefaultBoneIndex(BoneIndex other) {
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
