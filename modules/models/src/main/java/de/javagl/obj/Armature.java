package de.javagl.obj;

import org.jetbrains.annotations.NotNull;

public interface Armature {
    /**
     * Returns the name of the armature
     */
    String getName();

    /**
     * Returns the number of bones this face consists of.
     *
     * @return The number of vertices this face consists of.
     */
    int getNumBones();

    /**
     * Returns the bone index with the given number
     *
     * @param number The index of the bone
     * @return The bone with the passed index
     * @throws IndexOutOfBoundsException If the given number is negative
     * or not smaller than {@link #getNumBones()}
     */
    @NotNull
    Bone getBone(int number);
}
