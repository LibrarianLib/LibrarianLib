package com.teamwizardry.librarianlib.math

object WorldSpace: CoordinateSpace3D {
    override val parentSpace: CoordinateSpace3D? = null
    override val matrix: Matrix4d
        get() = Matrix4d.IDENTITY
    override val inverseMatrix: Matrix4d
        get() = Matrix4d.IDENTITY
}
