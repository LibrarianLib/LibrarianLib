package de.javagl.obj;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActAction {
    @NotNull
    private String name;
    private List<ActChannel> channels = new ArrayList<>();
    private Map<String, ActChannel> channelsByName = new HashMap<>();

    public ActAction(@NotNull String name) {
        this.name = name;
    }

    public ActAction(@NotNull ActAction other) {
        this.name = other.getName();
        for(int i = 0; i < other.getChannelCount(); i++) {
            this.addChannel(new ActChannel(other.getChannel(i)));
        }
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public ActChannel getChannel(int index) {
        return channels.get(index);
    }

    @Nullable
    public ActChannel getChannel(@NotNull String name) {
        return channelsByName.get(name);
    }

    public int getChannelCount() {
        return channels.size();
    }

    public void addChannel(@NotNull ActChannel channel) {
        if(getChannel(channel.getName()) != null) {
            throw new IllegalStateException("Object `" + getName() + "` already has an action named `" + channel.getName() + "`");
        }
        channels.add(channel);
        channelsByName.put(channel.getName(), channel);
    }
}
