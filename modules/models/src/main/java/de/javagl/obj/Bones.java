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
 * Methods to create {@link Bone} instances
 */
public class Bones
{
    /**
     * Create a copy of the given {@link Bone}
     *
     * @param other The other bone
     * @return The {@link Bone}
     */
    public static Bone copy(Bone other)
    {
        return new DefaultBone(other);
    }

    /**
     * Create a new {@link Bone} with the given parameters
     *
     * @param parent The parent index
     * @param name The bone name
     * @param head The head position
     * @param tail The tail position
     * @return The {@link Bone}
     */
    public static Bone create(int parent, String name, FloatTuple head, FloatTuple tail)
    {
        return new DefaultBone(parent, name, head, tail);
    }

    /**
     * Create a new {@link Bone} with the given parameters
     *
     * @param parent The parent index
     * @param name The bone name
     * @param headX The x component of the head position
     * @param headY The y component of the head position
     * @param headZ The z component of the head position
     * @param tailX The x component of the tail position
     * @param tailY The y component of the tail position
     * @param tailZ The z component of the tail position
     * @return The {@link Bone}
     */
    public static Bone create(int parent, String name, float headX, float headY, float headZ, float tailX, float tailY, float tailZ)
    {
        return new DefaultBone(parent, name, FloatTuples.create(headX, headY, headZ), FloatTuples.create(tailX, tailY, tailZ));
    }

    /**
     * Returns the string for the given bone that is used for representing
     * the given bone in an OBJ file
     *
     * @param bone The bone
     * @return The string for the given bone
     */
    public static String createString(Bone bone)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(bone.getParent() + 1);
        sb.append(' ');
        append3Tuple(sb, bone.getHead());
        sb.append(' ');
        append3Tuple(sb, bone.getTail());
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

    /**
     * Private constructor to prevent instantiation
     */
    private Bones()
    {
        // Private constructor to prevent instantiation
    }
}
