/*
 * www.javagl.de - Obj
 *
 * Copyright (c) 2008-2015 Marco Hutter - http://www.javagl.de
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.obj;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Methods to create {@link Armature} instances
 */
public class Armatures
{
    /**
     * Create a copy of the given {@link Armature}
     *
     * @param other The other armature
     * @return The {@link Armature}
     */
    public static Armature copy(@NotNull Armature other)
    {
        return new DefaultArmature(other);
    }

    /**
     * Create a new {@link Armature} with the given name
     *
     * @param name The name of the armature
     * @return The {@link Armature}
     */
    public static Armature create(String name)
    {
        return new DefaultArmature(name);
    }

    /**
     * Create a new {@link MutableArmature}
     *
     * @param name The name of the armature
     * @return The {@link MutableArmature}
     */
    public static MutableArmature createMutable(String name)
    {
        return new DefaultArmature(name);
    }


    public static void addToObj(ReadableObj input, WritableObj output) {
        for (int i = 0; i < input.getNumArmatures(); i++) {
            Armature armature = input.getArmature(i);
            output.addArmature(armature.getName());
            for (int j = 0; j < armature.getNumBones(); j++) {
                Bone bone = armature.getBone(j);
                output.addBone(bone.getParent(), bone.getHead(), bone.getTail(), bone.getName());
            }
        }
    }
    /**
     * Private constructor to prevent instantiation
     */
    private Armatures()
    {
        // Private constructor to prevent instantiation
    }
}
