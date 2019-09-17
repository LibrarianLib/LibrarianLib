package de.javagl.obj;

// b <parent index> <head x> <head y> <head z> <tail x> <tail y> <tail z> <name>
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
     * Gets the bone's head position.
     *
     * The head is the "start" position, i.e. the endpoint often shared with
     * its parent, or the point closest to the root of the bone hierarchy.
     */
    FloatTuple getHead();

    /**
     * Gets the bone's tail position.
     *
     * The tail is the "end" position, i.e. the endpoint often shared with
     * its children, or the point farthest from the root of the bone hierarchy.
     */
    FloatTuple getTail();
}
