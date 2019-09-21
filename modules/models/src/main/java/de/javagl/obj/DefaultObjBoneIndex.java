package de.javagl.obj;

import java.util.Objects;

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
        return armature;
    }

    @Override
    public int getBone() {
        return bone;
    }

    @Override
    public int hashCode() {
        return ObjBoneIndex.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return ObjBoneIndex.equals(this, obj);
    }
}
