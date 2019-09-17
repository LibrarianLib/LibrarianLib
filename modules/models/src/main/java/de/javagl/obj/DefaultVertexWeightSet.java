package de.javagl.obj;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DefaultVertexWeightSet implements MutableVertexWeightSet {
    private List<BoneIndex> indices;
    private List<Float> weights;

    public DefaultVertexWeightSet() {
        indices = new ArrayList<>();
        weights = new ArrayList<>();
    }

    public DefaultVertexWeightSet(List<BoneIndex> indices, List<Float> weights) {
        if(indices.size() != weights.size())
            throw new IllegalArgumentException("indices and weights lists are different sizes");
        this.indices = indices;
        this.weights = weights;
    }

    public DefaultVertexWeightSet(VertexWeightSet other) {
        indices = new ArrayList<>();
        weights = new ArrayList<>();
        for (int i = 0; i < other.getNumWeights(); i++) {
            indices.add(other.getBoneIndex(i));
            weights.add(other.getWeight(i));
        }
    }

    @Override
    public int getNumWeights() {
        return indices.size();
    }

    @NotNull
    @Override
    public BoneIndex getBoneIndex(int number) {
        return indices.get(number);
    }

    @Override
    public float getWeight(int number) {
        return weights.get(number);
    }

    @Override
    public void addWeight(@NotNull BoneIndex bone, float weight) {
        indices.add(bone);
        weights.add(weight);
    }
}
