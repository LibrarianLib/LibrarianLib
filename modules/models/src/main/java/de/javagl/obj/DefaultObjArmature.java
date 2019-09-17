package de.javagl.obj;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DefaultObjArmature implements MutableObjArmature {
    private final String name;
    private final List<ObjBone> bones = new ArrayList<>();

    public DefaultObjArmature(String name) {
        this.name = name;
    }

    public DefaultObjArmature(ObjArmature other) {
        this.name = other.getName();
        for (int i = 0; i < other.getNumBones(); i++) {
            bones.add(ObjBones.copy(other.getBone(i)));
        }
    }

    @Override
    public void addBone(ObjBone bone) {
        bones.add(bone);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNumBones() {
        return bones.size();
    }

    @NotNull
    @Override
    public ObjBone getBone(int number) {
        return bones.get(number);
    }
}
