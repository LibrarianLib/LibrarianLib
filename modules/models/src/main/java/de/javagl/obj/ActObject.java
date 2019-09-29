package de.javagl.obj;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActObject {
    private String name;
    private List<ActAction> actions = new ArrayList<>();
    private Map<String, ActAction> actionsByName = new HashMap<>();
    @Nullable
    private ActAction currentAction = null;

    public ActObject(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public ActAction getAction(int index) {
        return actions.get(index);
    }

    @Nullable
    public ActAction getAction(@NotNull String name) {
        return actionsByName.get(name);
    }

    public int getActionCount() {
        return actions.size();
    }

    public void addAction(@NotNull ActAction action) {
        if(getAction(action.getName()) != null) {
            throw new IllegalStateException("Object `" + getName() + "` already has an action named `" + action.getName() + "`");
        }
        actions.add(action);
        actionsByName.put(action.getName(), action);
    }

    public void merge(@NotNull ActObject other) {
        for(int i = 0; i < other.getActionCount(); i++) {
            ActAction otherAction = other.getAction(i);
            ActAction ourAction = getAction(otherAction.getName());
            if(ourAction == null) {
                ourAction = new ActAction(otherAction);
                actions.add(ourAction);
                actionsByName.put(ourAction.getName(), ourAction);
            } else {
                throw new IllegalStateException("Object `" + getName() + "` already has an action named `" + otherAction.getName() + "`");
            }

        }
    }
}
