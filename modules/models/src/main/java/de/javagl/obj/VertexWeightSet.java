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
 * The bone weights of a single vertex
 */
public interface VertexWeightSet
{
    /**
     * Returns the number of weights in this set.
     * 
     * @return The number of weights in this set.
     */
    int getNumWeights();

    /**
     * Returns the bone index with the given number
     *
     * @param number The number of the weight
     * @return The bone referenced by the weight.
     * @throws IndexOutOfBoundsException If the given number is negative
     * or not smaller than {@link #getNumWeights()}
     */
    @NotNull
    ObjBoneIndex getBoneIndex(int number);

    /**
     * Returns the weight with the given number
     *
     * @param number The number of the weight
     * @return The weight for the bone
     * @throws IndexOutOfBoundsException If the given number is negative
     * or not smaller than {@link #getNumWeights()}
     */
    float getWeight(int number);
}
