package de.javagl.obj;

public class DefaultObjBone implements ObjBone {
    private final int parent;
    private final String name;
    private final FloatTuple position;
    private final FloatTuple rotation;
    private final float length;

    public DefaultObjBone(int parent, String name, FloatTuple position, FloatTuple rotation, float length) {
        this.parent = parent;
        this.name = name;
        this.position = position;
        this.rotation = rotation;
        this.length = length;
    }

    public DefaultObjBone(ObjBone other) {
        this.parent = other.getParent();
        this.name = other.getName();
        this.position = other.getPosition();
        this.rotation = other.getRotation();
        this.length = other.getLength();
    }

    @Override
    public int getParent() {
        return parent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public FloatTuple getPosition() {
        return position;
    }

    @Override
    public FloatTuple getRotation() {
        return rotation;
    }

    @Override
    public float getLength() {
        return length;
    }
}
