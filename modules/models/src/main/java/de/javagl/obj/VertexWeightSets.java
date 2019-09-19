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
 * Methods to create {@link VertexWeightSet} instances
 */
public class VertexWeightSets
{
    /**
     * Create a copy of the given {@link VertexWeightSet}
     *
     * @param other The other weights
     * @return The {@link VertexWeightSet}
     */
    public static VertexWeightSet copy(@NotNull VertexWeightSet other)
    {
        return new DefaultVertexWeightSet(other);
    }

    /**
     * Create a new {@link VertexWeightSet} with the weights
     *
     * @param indices The bone indices
     * @param weights The weights
     * @return The {@link VertexWeightSet}
     */
    public static VertexWeightSet create(List<ObjBoneIndex> indices, List<Float> weights)
    {
        return new DefaultVertexWeightSet(indices, weights);
    }

    /**
     * Create a new {@link MutableVertexWeightSet}
     *
     * @return The {@link MutableVertexWeightSet}
     */
    public static MutableVertexWeightSet createMutable()
    {
        return new DefaultVertexWeightSet();
    }


    /**
     * Create a new {@link MutableVertexWeightSet}
     *
     * @param other The weight set to copy
     * @return The {@link MutableVertexWeightSet}
     */
    public static MutableVertexWeightSet createMutable(@NotNull VertexWeightSet other)
    {
        return new DefaultVertexWeightSet(other);
    }
    /**
     * Adds the passed weights to the passed Obj, or does nothing if the passed weights are null.
     *
     * @param obj the Obj to add the vertices to
     * @param weights the weights to add, if any
     */
    public static void addToObj(int armatureOffset, WritableObj obj, @Nullable VertexWeightSet weights) {
        if(weights != null) {
            for (int i = 0; i < weights.getNumWeights(); i++) {
                ObjBoneIndex index = weights.getBoneIndex(i);
                obj.addWeight(index.getArmature() + armatureOffset, index.getBone(), weights.getWeight(i));
            }
        }
    }

    /**
     * Private constructor to prevent instantiation
     */
    private VertexWeightSets()
    {
        // Private constructor to prevent instantiation
    }
}
