package de.javagl.obj;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Act {
    private List<ActObject> objects = new ArrayList<>();
    private Map<String, ActObject> objectsByName = new HashMap<>();
    @Nullable
    private ActObject currentObject = null;
    @Nullable
    private ActAction currentAction = null;
    @Nullable
    private ActChannel currentChannel = null;

    @NotNull
    public ActObject getObject(int index) {
        return objects.get(index);
    }

    @Nullable
    public ActObject getObject(@NotNull String name) {
        return objectsByName.get(name);
    }

    public int getObjectCount() {
        return objects.size();
    }

    public void beginObject(@NotNull String name) {
        ActObject object = new ActObject(name);
        objects.add(object);
        objectsByName.put(name, object);
        currentObject = object;
        currentAction = null;
        currentChannel = null;
    }

    public void beginAction(@NotNull String name) {
        if(currentObject == null) {
            throw new IllegalStateException("Cannot create an action without an object");
        }
        ActAction action = new ActAction(name);
        currentObject.addAction(action);
        currentAction = action;
        currentChannel = null;
    }

    public void beginChannel(@NotNull String name) {
        if(currentAction == null) {
            throw new IllegalStateException("Cannot create a channel without an action");
        }
        ActChannel channel = new ActChannel(name);
        currentAction.addChannel(channel);
        currentChannel = channel;
    }

    public void addSample(float time, float value) {
        if(currentChannel == null) {
            throw new IllegalStateException("Cannot add a sample without a channel");
        }
        currentChannel.addSample(time, value);
    }

    public void merge(@NotNull Act other) {
        for(int i = 0; i < other.getObjectCount(); i++) {
            ActObject otherObject = other.getObject(i);
            ActObject ourObject = getObject(otherObject.getName());
            if(ourObject == null) {
                ourObject = new ActObject(otherObject.getName());
                objects.add(ourObject);
                objectsByName.put(ourObject.getName(), ourObject);
            }
            ourObject.merge(otherObject);
        }
    }
}
