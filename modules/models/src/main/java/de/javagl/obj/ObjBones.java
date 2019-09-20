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
 * Methods to create {@link ObjBone} instances
 */
public class ObjBones
{
    /**
     * Create a copy of the given {@link ObjBone}
     *
     * @param other The other bone
     * @return The {@link ObjBone}
     */
    public static ObjBone copy(ObjBone other)
    {
        return new DefaultObjBone(other);
    }

    /**
     * Create a new {@link ObjBone} with the given parameters
     *
     * @param parent The parent index
     * @param name The bone name
     * @param position The position
     * @param rotation The rotation quaternion
     * @param length the bone length
     * @return The {@link ObjBone}
     */
    public static ObjBone create(int parent, String name, FloatTuple position, FloatTuple rotation, float length)
    {
        return new DefaultObjBone(parent, name, position, rotation, length);
    }

    /**
     * Create a new {@link ObjBone} with the given parameters
     *
     * @param parent The parent index
     * @param name The bone name
     * @param x The x component of the position
     * @param y The y component of the position
     * @param z The z component of the position
     * @param rotX The x component of the rotation quaternion
     * @param rotY The y component of the rotation quaternion
     * @param rotZ The z component of the rotation quaternion
     * @param rotW The w component of the rotation quaternion
     * @param length The length of the bone
     * @return The {@link ObjBone}
     */
    public static ObjBone create(int parent, String name, float x, float y, float z, float rotX, float rotY, float rotZ, float rotW, float length)
    {
        return new DefaultObjBone(parent, name, FloatTuples.create(x, y, z), FloatTuples.create(rotX, rotY, rotZ, rotW), length);
    }

    /**
     * Returns the string for the given bone that is used for representing
     * the given bone in an OBJ file
     *
     * @param bone The bone
     * @return The string for the given bone
     */
    public static String createString(ObjBone bone)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(bone.getParent() + 1);
        sb.append(' ');
        append3Tuple(sb, bone.getPosition());
        sb.append(' ');
        append3Tuple(sb, bone.getRotation());
        sb.append(' ');
        sb.append(bone.getLength());
        sb.append(' ');
        sb.append(bone.getName());
        return sb.toString();
    }

    private static void append3Tuple(StringBuilder sb, FloatTuple tuple) {
        sb.append(tuple.getX());
        sb.append(' ');
        sb.append(tuple.getY());
        sb.append(' ');
        sb.append(tuple.getZ());
    }

    private static void append4Tuple(StringBuilder sb, FloatTuple tuple) {
        sb.append(tuple.getX());
        sb.append(' ');
        sb.append(tuple.getY());
        sb.append(' ');
        sb.append(tuple.getZ());
        sb.append(' ');
        sb.append(tuple.getW());
    }

    /**
     * Private constructor to prevent instantiation
     */
    private ObjBones()
    {
        // Private constructor to prevent instantiation
    }
}
