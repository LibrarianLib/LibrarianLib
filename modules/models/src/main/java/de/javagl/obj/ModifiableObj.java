package de.javagl.obj;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface ModifiableObj extends ReadableObj {
    /**
     * Replaces the armature with the given index. Note that the index
     * is <b>0</b>-based, in contrast to the <b>1</b>-based indices of the
     * actual OBJ file.
     *
     * @param index The index of the armature
     * @param armature The new armature with the given index
     * @throws IndexOutOfBoundsException If the index is negative or not
     * smaller than {@link #getNumArmatures()}
     */
    void setArmature(int index, ObjArmature armature);

    /**
     * Replaces the vertex with the given index. Note that the index
     * is <b>0</b>-based, in contrast to the <b>1</b>-based indices of the
     * actual OBJ file.
     *
     * @param index The index of the vertex
     * @param vertex The new vertex with the given index
     * @throws IndexOutOfBoundsException If the index is negative or not
     * smaller than {@link #getNumVertices()}
     */
    void setVertex(int index, FloatTuple vertex);

    /**
     * Replaces the weights of a vertex with the given index. Note that
     * the index is <b>0</b>-based, in contrast to the <b>1</b>-based
     * indices of the actual OBJ file.
     *
     * @param index The index of the vertex
     * @param weights The new weights for the vertex with the given index
     * @throws IndexOutOfBoundsException If the index is negative or not
     * smaller than {@link #getNumVertices()}
     */
    void setWeights(int index, VertexWeightSet weights);

    /**
     * Replaces the texture coordinate with the given index. Note that the
     * is <b>0</b>-based, in contrast to the <b>1</b>-based indices of the
     * actual OBJ file.
     *
     * @param index The index of the texture coordinate
     * @param texCoord The new texture coordinate with the given index
     * @throws IndexOutOfBoundsException If the index is negative or not
     * smaller than {@link #getNumTexCoords()}
     */
    void setTexCoord(int index, FloatTuple texCoord);

    /**
     * Replaces the normal with the given index. Note that the index
     * is <b>0</b>-based, in contrast to the <b>1</b>-based indices of the
     * actual OBJ file.
     *
     * @param index The index of the normal
     * @param normal The new normal with the given index
     * @throws IndexOutOfBoundsException If the index is negative or not
     * smaller than {@link #getNumNormals()}
     */
    void setNormal(int index, FloatTuple normal);

    /**
     * Replaces the face with the given index.
     *
     * @param index The index of the face.
     * @param face The new face with the given index
     * @throws IndexOutOfBoundsException If the index is negative or not
     * smaller than {@link #getNumFaces()}
     */
    void setFace(int index, ObjFace face);

    /**
     * Replaces the group with the given index.
     *
     * @param index The index of the group.
     * @param group The new group with the given index.
     * @throws IndexOutOfBoundsException If the index is negative or not
     * smaller than {@link #getNumGroups()}
     */
    void setGroup(int index, ObjGroup group);

    /**
     * Replaces the group with the given name, or <code>null</code> if
     * there is no such group in this Obj.
     *
     * @param name The name of the group.
     * @param group The new group with the given name.
     */
    void setGroup(String name, ObjGroup group);

    /**
     * Replaces the material group with the given index.
     *
     * @param index The index of the material group.
     * @param group The new material group with the given index.
     * @throws IndexOutOfBoundsException If the index is negative or not
     * smaller than {@link #getNumMaterialGroups()}
     */
    void setMaterialGroup(int index, ObjGroup group);

    /**
     * Replaces the material group with the given name, or <code>null</code> if
     * there is no such group in this Obj.
     *
     * @param name The name of the material group.
     * @param group The new material group with the given name.
     */
    void setMaterialGroup(String name, ObjGroup group);


    /**
     * Replaces an unmodifiable list containing the names of the MTL file
     * that are associated with this OBJ, as they have been read from
     * the <code>mtllib</code> line.
     * This may be an empty list, if no MTL file names have been read.
     *
     * @param fileNames The names of the MTL files.
     */
    void setMtlFileNames(List<String> fileNames);

    /**
     * Replaces an unmodifiable list containing the names of the ACT file
     * that are associated with this OBJ, as they have been read from
     * the <code>actlib</code> line.
     * This may be an empty list, if no ACT file names have been read.
     *
     * @param fileNames The names of the ACT files.
     */
    void setActFileNames(List<String> fileNames);
}
