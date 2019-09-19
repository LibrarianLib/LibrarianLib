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

/**
 * Methods to create {@link ObjArmature} instances
 */
public class ObjArmatures
{
    /**
     * Create a copy of the given {@link ObjArmature}
     *
     * @param other The other armature
     * @return The {@link ObjArmature}
     */
    public static ObjArmature copy(@NotNull ObjArmature other)
    {
        return new DefaultObjArmature(other);
    }

    /**
     * Create a new {@link ObjArmature} with the given name
     *
     * @param name The name of the armature
     * @return The {@link ObjArmature}
     */
    public static ObjArmature create(String name)
    {
        return new DefaultObjArmature(name);
    }

    /**
     * Create a new {@link MutableObjArmature}
     *
     * @param name The name of the armature
     * @return The {@link MutableObjArmature}
     */
    public static MutableObjArmature createMutable(String name)
    {
        return new DefaultObjArmature(name);
    }

    /**
     * Create a new {@link MutableObjArmature}
     *
     * @param other The armature to copy
     * @return The {@link MutableObjArmature}
     */
    public static MutableObjArmature createMutable(@NotNull ObjArmature other)
    {
        return new DefaultObjArmature(other);
    }

    public static void addToObj(ReadableObj input, WritableObj output) {
        for (int i = 0; i < input.getNumArmatures(); i++) {
            ObjArmature armature = input.getArmature(i);
            output.addArmature(armature.getName());
            for (int j = 0; j < armature.getNumBones(); j++) {
                ObjBone bone = armature.getBone(j);
                output.addBone(bone.getParent(), bone.getHead(), bone.getTail(), bone.getName());
            }
        }
    }
    /**
     * Private constructor to prevent instantiation
     */
    private ObjArmatures()
    {
        // Private constructor to prevent instantiation
    }
}
