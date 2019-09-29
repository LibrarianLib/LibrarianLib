package de.javagl.obj;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ActChannel {
    @NotNull
    private final String name;
    @NotNull
    private final TreeMap<Float, Float> samples = new TreeMap<>();

    public ActChannel(@NotNull String name) {
        this.name = name;
    }

    public ActChannel(@NotNull ActChannel other) {
        this.name = other.getName();
        for(float time : other.getSamples()) {
            addSample(time, other.getValue(time));
        }
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void addSample(float time, float value) {
        samples.put(time, value);
    }

    public Iterable<Float> getSamples() {
        return samples.keySet();
    }

    public float getValue(float frame) {
        Map.Entry<Float, Float> ceiling = samples.ceilingEntry(frame);
        if(ceiling != null && ceiling.getKey() == frame)
            return ceiling.getValue();
        Map.Entry<Float, Float> floor = samples.floorEntry(frame);
        if(ceiling == null && floor == null)
            throw new IndexOutOfBoundsException("Can't calculate frame " + frame + " of an empty animation");
        if(ceiling == null)
            return floor.getValue();
        if(floor == null)
            return ceiling.getValue();
        float fraction = (frame - floor.getKey()) / (ceiling.getKey() - floor.getKey());
        return floor.getValue() + (ceiling.getValue() - floor.getValue()) * fraction;
    }
}
