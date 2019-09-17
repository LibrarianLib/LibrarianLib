package de.javagl.obj;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DefaultArmature implements MutableArmature {
    private final String name;
    private final List<Bone> bones = new ArrayList<>();

    public DefaultArmature(String name) {
        this.name = name;
    }

    public DefaultArmature(Armature other) {
        this.name = other.getName();
        for (int i = 0; i < other.getNumBones(); i++) {
            bones.add(Bones.copy(other.getBone(i)));
        }
    }

    @Override
    public void addBone(Bone bone) {
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
    public Bone getBone(int number) {
        return bones.get(number);
    }
}
