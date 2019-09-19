package com.teamwizardry.librarianlib.math

import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.Vec3d
import java.util.Collections
import java.util.IdentityHashMap
import kotlin.math.max
import kotlin.math.min

interface CoordinateSpace3D {
    /**
     * The parent coordinate space. All points in this space are transformed relative to its parent.
     */
    val parentSpace: CoordinateSpace3D?
    /**
     * The "normal" matrix that converts points from this space to the parent space.
     *
     * e.g. if the child space is embedded with an offset (x,y) within its parent, this will be
     * `Matrix4d().transform(x,y)`
     */
    val matrix: Matrix4d
    /**
     * The inverse of [matrix]. Often the best way to get this is to apply inverse transforms instead of directly
     * inverting the matrix. This allows elegant failure state when scaling by zero. Inverting a matrix with zero
     * scale is impossible, but when applying inverse transforms this can be accounted for by ignoring inverse scales.
     */
    val inverseMatrix: Matrix4d

    /**
     * Create a matrix that, when applied to a point in this coordinate space, returns the corresponding point in the
     * [other] coordinate space.
     */
    @JvmDefault
    fun conversionMatrixTo(other: CoordinateSpace3D): Matrix4d {
        if(other === this) return Matrix4d.IDENTITY
        if(other === this.parentSpace) return this.matrix
        if(other.parentSpace === this) return other.inverseMatrix

        val lca = lowestCommonAncestor(other) ?: throw UnrelatedCoordinateSpace3DException(this, other)

        if(lca === other) return this.matrixToParent(other)
        if(lca === this) return other.matrixFromParent(this)

        val matrix = MutableMatrix4d()
        matrix *= other.matrixFromParent(lca)
        matrix *= this.matrixToParent(lca)
        return matrix
    }

    /**
     * Create a matrix that, when applied to a point in the [other] coordinate space, returns the corresponding point
     * in the this coordinate space.
     */
    @JvmDefault
    fun conversionMatrixFrom(other: CoordinateSpace3D): Matrix4d = other.conversionMatrixTo(this)

    /**
     * Converts a point in this coordinate space into the corresponding point in the [other] coordinate space
     */
    @JvmDefault
    fun convertPointTo(point: Vec3d, other: CoordinateSpace3D): Vec3d = conversionMatrixTo(other) * point

    /**
     * Converts a point in the [other] coordinate space into the corresponding point in this coordinate space
     */
    @JvmDefault
    fun convertPointFrom(point: Vec3d, other: CoordinateSpace3D): Vec3d = other.convertPointTo(point, this)

    /**
     * Converts a box in this coordinate space to the _**smallest bounding box**_ around it in the [other]
     * coordinate space
     *
     * ## NOTE!
     *
     * This operation _**IS NOT REVERSIBLE**_. If there is any rotation returned box will not equal the passed box,
     * instead it will _contain_ it.
     */
    @JvmDefault
    fun convertBoxTo(box: AxisAlignedBB, other: CoordinateSpace3D): AxisAlignedBB {
        val corners = arrayOf(
            vec(box.minX, box.minY, box.minZ),
            vec(box.minX, box.maxY, box.minZ),
            vec(box.minX, box.maxY, box.maxZ),
            vec(box.minX, box.minY, box.maxZ),
            vec(box.maxX, box.minY, box.minZ),
            vec(box.maxX, box.maxY, box.minZ),
            vec(box.maxX, box.maxY, box.maxZ),
            vec(box.maxX, box.minY, box.maxZ)
        )

        val matrix = conversionMatrixTo(other)
        for(i in corners.indices) {
            corners[i] = matrix * corners[i]
        }

        var minX = 0.0
        var minY = 0.0
        var minZ = 0.0
        var maxX = 0.0
        var maxY = 0.0
        var maxZ = 0.0

        corners.forEach {
            minX = min(minX, it.x)
            minY = min(minY, it.y)
            minZ = min(minZ, it.z)
            maxX = max(maxX, it.x)
            maxY = max(maxY, it.y)
            maxZ = max(maxZ, it.z)
        }

        return AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ)
    }

    /**
     * Converts a box in the [other] coordinate space to the _**smallest bounding box**_ around it in this
     * coordinate space
     *
     * ## NOTE!
     *
     * This operation _**IS NOT REVERSIBLE**_. If there is any rotation the returned box will not equal the passed
     * box, instead it will _contain_ it.
     */
    @JvmDefault
    fun convertBoxFrom(box: AxisAlignedBB, other: CoordinateSpace3D) = other.convertBoxTo(box, this)

    /**
     * Converts a point in this coordinate space into the corresponding point in the parent coordinate space.
     *
     * If this space has no parent, this method returns the original point.
     */
    @JvmDefault
    fun convertPointToParent(point: Vec3d) = parentSpace?.let { convertPointTo(point, it) } ?: point

    /**
     * Converts a point in the parent coordinate space into the corresponding point in this coordinate space.
     *
     * If this space has no parent, this method returns the original point.
     */
    @JvmDefault
    fun convertPointFromParent(point: Vec3d) = parentSpace?.let { convertPointFrom(point, it) } ?: point

    /**
     * Converts a box in this coordinate space to the _**smallest bounding box**_ around it in the [other]
     * coordinate space
     *
     * If this space has no parent, this method returns the original box.
     *
     * ## NOTE!
     *
     * This operation _**IS NOT REVERSIBLE**_. If there is any rotation returned box will not equal the passed box,
     * instead it will _contain_ it.
     */
    @JvmDefault
    fun convertBoxToParent(box: AxisAlignedBB) = parentSpace?.let { convertBoxTo(box, it) } ?: box

    /**
     * Converts a box in the [other] coordinate space to the _**smallest bounding box**_ around it in this
     * coordinate space
     *
     * If this space has no parent, this method returns the original box.
     *
     * ## NOTE!
     *
     * This operation _**IS NOT REVERSIBLE**_. If there is any rotation the returned box will not equal the passed
     * box, instead it will _contain_ it.
     */
    @JvmDefault
    fun convertBoxFromParent(box: AxisAlignedBB) = parentSpace?.let { convertBoxFrom(box, it) } ?: box

    private fun lowestCommonAncestor(other: CoordinateSpace3D): CoordinateSpace3D? {
        // check for straight-line relationships next (doing both in parallel because that minimizes time
        // when the distance is short)
        var thisAncestor = this.parentSpace
        var otherAncestor = other.parentSpace
        while(thisAncestor != null || otherAncestor != null) {
            if(thisAncestor === other) return other
            if(otherAncestor === this) return this
            thisAncestor = thisAncestor?.parentSpace
            otherAncestor = otherAncestor?.parentSpace
        }

        val ancestors: MutableSet<CoordinateSpace3D> = Collections.newSetFromMap<CoordinateSpace3D>(IdentityHashMap())
        var ancestor = this.parentSpace
        while(ancestor != null) {
            ancestors.add(ancestor)
            ancestor = ancestor.parentSpace
        }
        ancestor = other.parentSpace
        while(ancestor != null) {
            if(ancestor in ancestors) return ancestor
            ancestor = ancestor.parentSpace
        }

        return null
    }

    /**
     * The matrix to get our coordinates back to [other]'s space. [other] is one of our ancestors
     */
    private fun matrixToParent(parent: CoordinateSpace3D): MutableMatrix4d {
        val ancestors = mutableListOf<CoordinateSpace3D>()
        var space: CoordinateSpace3D = this
        while(space !== parent) {
            ancestors.add(space)
            space = space.parentSpace!!
        }

        val matrix = MutableMatrix4d()
        ancestors.reversed().forEach {
            matrix *= it.matrix
        }
        return matrix
    }

    /**
     * The matrix to get [other]'s coordinates down to our space. [other] is one of our ancestors
     */
    private fun matrixFromParent(other: CoordinateSpace3D): MutableMatrix4d {
        val ancestors = mutableListOf<CoordinateSpace3D>()
        var space: CoordinateSpace3D = this
        while(space !== other) {
            ancestors.add(space)
            space = space.parentSpace!!
        }

        val matrix = MutableMatrix4d()
        ancestors.forEach {
            matrix *= it.inverseMatrix
        }
        return matrix
    }
}

class UnrelatedCoordinateSpace3DException(val space1: CoordinateSpace3D, val space2: CoordinateSpace3D): RuntimeException()
