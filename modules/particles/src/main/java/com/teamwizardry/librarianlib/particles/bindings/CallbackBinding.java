package com.teamwizardry.librarianlib.particles.bindings;

import com.teamwizardry.librarianlib.particles.ReadParticleBinding;
import org.jetbrains.annotations.NotNull;

public class CallbackBinding implements ReadParticleBinding {
    private final double[] contents;
    private final Callback callback;

    public CallbackBinding(int size, Callback callback) {
        this.contents = new double[size];
        this.callback = callback;
    }

    @NotNull
    @Override
    public double[] getContents() {
        return contents;
    }

    @Override
    public void load(@NotNull double[] particle) {
        callback.call(particle, contents);
    }

    @FunctionalInterface
    public interface Callback {
        void call(@NotNull double[] particle, @NotNull double[] contents);
    }
}
