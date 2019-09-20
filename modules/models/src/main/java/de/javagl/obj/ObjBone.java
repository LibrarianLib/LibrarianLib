package de.javagl.obj;

// b <parent index> <x> <y> <z> <rot x> <rot y> <rot z> <rot w> <length> <name>
public interface ObjBone {

    /**
     * Gets the parent bone's index (zero-indexed)
     */
    int getParent();

    /**
     * Gets the bone's name
     */
    String getName();

    /**
     * Gets the bone's position.
     */
    FloatTuple getPosition();

    /**
     * Gets the bone's rotation quaternion.
     */
    FloatTuple getRotation();

    /**
     * Gets the bone's length.
     * @return The bone's length.
     */
    float getLength();
}
