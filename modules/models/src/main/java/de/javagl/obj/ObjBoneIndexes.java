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

/**
 * Methods to create {@link ObjBoneIndex} instances
 */
public class ObjBoneIndexes
{
    /**
     * Create a copy of the given {@link ObjBoneIndex}
     *
     * @param other The other index
     * @return The {@link ObjBoneIndex}
     */
    public static ObjBoneIndex copy(ObjBoneIndex other)
    {
        return new DefaultObjBoneIndex(other);
    }

    /**
     * Create a new {@link ObjBoneIndex} with the given parameters
     *
     * @param armature The armature index
     * @param bone The bone index
     * @return The {@link ObjBoneIndex}
     */
    public static ObjBoneIndex create(int armature, int bone)
    {
        return new DefaultObjBoneIndex(armature, bone);
    }


    /**
     * Returns the string for the given index that is used for representing
     * the given weight in an OBJ file
     *
     * @param index The index
     * @return The string for the given index
     */
    public static String createString(ObjBoneIndex index)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(index.getArmature() + 1);
        sb.append(' ');
        sb.append(index.getBone() + 1);
        return sb.toString();
    }

    /**
     * Private constructor to prevent instantiation
     */
    private ObjBoneIndexes()
    {
        // Private constructor to prevent instantiation
    }
}
