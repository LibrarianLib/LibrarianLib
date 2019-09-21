package com.teamwizardry.librarianlib.math

import java.io.Serializable

import net.minecraft.util.math.Vec3d
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sign
import kotlin.math.sin
import kotlin.math.sqrt

// adapted from flow/math: https://github.com/flow/math
/**
 * Represent a quaternion of the form `xi + yj + zk + w`. The x, y, z and w components are stored as doubles. This class is immutable.
 */
class Quaternion
/**
 * Constructs a new quaternion from the double components.
 *
 * @param x The x (imaginary) component
 * @param y The y (imaginary) component
 * @param z The z (imaginary) component
 * @param w The w (real) component
 */
(
    /**
     * Gets the x (imaginary) component of this quaternion.
     *
     * @return The x (imaginary) component
     */
    val x: Double,
    /**
     * Gets the y (imaginary) component of this quaternion.
     *
     * @return The y (imaginary) component
     */
    val y: Double,
    /**
     * Gets the z (imaginary) component of this quaternion.
     *
     * @return The z (imaginary) component
     */
    val z: Double,
    /**
     * Gets the w (real) component of this quaternion.
     *
     * @return The w (real) component
     */
    val w: Double): Comparable<Quaternion>, Serializable, Cloneable {
    @Volatile
    @Transient
    private var hashCode = 0

    /**
     * Returns a unit vector representing the direction of this quaternion, which is [Vec3d.FORWARD] rotated by this quaternion.
     *
     * @return The vector representing the direction this quaternion is pointing to
     */
    val direction: Vec3d
        get() = rotate(vec(0, 0, 1))

    /**
     * Returns the axis of rotation for this quaternion.
     *
     * @return The axis of rotation
     */
    val axis: Vec3d
        get() {
            val q = fastInvSqrt(1 - w * w)
            return vec(x * q, y * q, z * q)
        }

    /**
     * Returns the angles in degrees around the x, y and z axes that correspond to the rotation represented by this quaternion.
     *
     * @return The angle in degrees for each axis, stored in a vector, in the corresponding component
     */
    val axesAnglesDeg: Vec3d
        get() = axesAnglesRad * (180/PI)

    /**
     * Returns the angles in radians around the x, y and z axes that correspond to the rotation represented by this quaternion.
     *
     * @return The angle in radians for each axis, stored in a vector, in the corresponding component
     */
    val axesAnglesRad: Vec3d
        get() {
            val roll: Double
            val pitch: Double
            val yaw: Double
            val test = w * x - y * z
            if (abs(test) < 0.4999) {
                roll = atan2(2 * (w * z + x * y), 1 - 2 * (x * x + z * z))
                pitch = asin(2 * test)
                yaw = atan2(2 * (w * y + z * x), 1 - 2 * (x * x + y * y))
            } else {
                val sign = if (test < 0) -1 else 1
                roll = 0.0
                pitch = sign * Math.PI / 2
                yaw = -sign * 2 * atan2(z, w)
            }
            return Vec3d(pitch, yaw, roll)
        }

    /**
     * Constructs a new quaternion from the float components.
     *
     * @param x The x (imaginary) component
     * @param y The y (imaginary) component
     * @param z The z (imaginary) component
     * @param w The w (real) component
     */
    @JvmOverloads
    constructor(x: Float = 0f, y: Float = 0f, z: Float = 0f, w: Float = 1f): this(x.toDouble(), y.toDouble(), z.toDouble(), w.toDouble()) {
    }

    /**
     * Copy constructor.
     *
     * @param q The quaternion to copy
     */
    constructor(q: Quaternion): this(q.x, q.y, q.z, q.w) {}

    /** Operator function for Kotlin  */
    operator fun component1(): Double {
        return x
    }

    /** Operator function for Kotlin  */
    operator fun component2(): Double {
        return y
    }

    /** Operator function for Kotlin  */
    operator fun component3(): Double {
        return z
    }

    /** Operator function for Kotlin  */
    operator fun component4(): Double {
        return w
    }

    /**
     * Adds another quaternion to this one.
     *
     * @param q The quaternion to add
     * @return A new quaternion, which is the sum of both
     */
    fun add(q: Quaternion): Quaternion {
        return add(q.x, q.y, q.z, q.w)
    }

    /** Operator function for Kotlin  */
    operator fun plus(q: Quaternion): Quaternion {
        return add(q)
    }

    /**
     * Adds the float components of another quaternion to this one.
     *
     * @param x The x (imaginary) component of the quaternion to add
     * @param y The y (imaginary) component of the quaternion to add
     * @param z The z (imaginary) component of the quaternion to add
     * @param w The w (real) component of the quaternion to add
     * @return A new quaternion, which is the sum of both
     */
    fun add(x: Float, y: Float, z: Float, w: Float): Quaternion {
        return add(x.toDouble(), y.toDouble(), z.toDouble(), w.toDouble())
    }

    /**
     * Adds the double components of another quaternion to this one.
     *
     * @param x The x (imaginary) component of the quaternion to add
     * @param y The y (imaginary) component of the quaternion to add
     * @param z The z (imaginary) component of the quaternion to add
     * @param w The w (real) component of the quaternion to add
     * @return A new quaternion, which is the sum of both
     */
    fun add(x: Double, y: Double, z: Double, w: Double): Quaternion {
        return Quaternion(this.x + x, this.y + y, this.z + z, this.w + w)
    }

    /**
     * Subtracts another quaternion from this one.
     *
     * @param q The quaternion to subtract
     * @return A new quaternion, which is the difference of both
     */
    fun sub(q: Quaternion): Quaternion {
        return sub(q.x, q.y, q.z, q.w)
    }

    /** Operator function for Kotlin  */
    operator fun minus(q: Quaternion): Quaternion {
        return sub(q)
    }

    /**
     * Subtracts the float components of another quaternion from this one.
     *
     * @param x The x (imaginary) component of the quaternion to subtract
     * @param y The y (imaginary) component of the quaternion to subtract
     * @param z The z (imaginary) component of the quaternion to subtract
     * @param w The w (real) component of the quaternion to subtract
     * @return A new quaternion, which is the difference of both
     */
    fun sub(x: Float, y: Float, z: Float, w: Float): Quaternion {
        return sub(x.toDouble(), y.toDouble(), z.toDouble(), w.toDouble())
    }

    /**
     * Subtracts the double components of another quaternion from this one.
     *
     * @param x The x (imaginary) component of the quaternion to subtract
     * @param y The y (imaginary) component of the quaternion to subtract
     * @param z The z (imaginary) component of the quaternion to subtract
     * @param w The w (real) component of the quaternion to subtract
     * @return A new quaternion, which is the difference of both
     */
    fun sub(x: Double, y: Double, z: Double, w: Double): Quaternion {
        return Quaternion(this.x - x, this.y - y, this.z - z, this.w - w)
    }

    /**
     * Multiplies the components of this quaternion by a float scalar.
     *
     * @param a The multiplication scalar
     * @return A new quaternion, which has each component multiplied by the scalar
     */
    fun mul(a: Float): Quaternion {
        return mul(a.toDouble())
    }

    /** Operator function for Kotlin  */
    operator fun times(a: Float): Quaternion {
        return mul(a)
    }

    /**
     * Multiplies the components of this quaternion by a double scalar.
     *
     * @param a The multiplication scalar
     * @return A new quaternion, which has each component multiplied by the scalar
     */
    fun mul(a: Double): Quaternion {
        return Quaternion(x * a, y * a, z * a, w * a)
    }

    /** Operator function for Kotlin  */
    operator fun times(a: Double): Quaternion {
        return mul(a)
    }

    /**
     * Multiplies another quaternion with this one.
     *
     * @param q The quaternion to multiply with
     * @return A new quaternion, which is the product of both
     */
    fun mul(q: Quaternion): Quaternion {
        return mul(q.x, q.y, q.z, q.w)
    }

    /** Operator function for Kotlin  */
    operator fun times(q: Quaternion): Quaternion {
        return mul(q)
    }

    /**
     * Multiplies the float components of another quaternion with this one.
     *
     * @param x The x (imaginary) component of the quaternion to multiply with
     * @param y The y (imaginary) component of the quaternion to multiply with
     * @param z The z (imaginary) component of the quaternion to multiply with
     * @param w The w (real) component of the quaternion to multiply with
     * @return A new quaternion, which is the product of both
     */
    fun mul(x: Float, y: Float, z: Float, w: Float): Quaternion {
        return mul(x.toDouble(), y.toDouble(), z.toDouble(), w.toDouble())
    }

    /**
     * Multiplies the double components of another quaternion with this one.
     *
     * @param x The x (imaginary) component of the quaternion to multiply with
     * @param y The y (imaginary) component of the quaternion to multiply with
     * @param z The z (imaginary) component of the quaternion to multiply with
     * @param w The w (real) component of the quaternion to multiply with
     * @return A new quaternion, which is the product of both
     */
    fun mul(x: Double, y: Double, z: Double, w: Double): Quaternion {
        return Quaternion(
            this.w * x + this.x * w + this.y * z - this.z * y,
            this.w * y + this.y * w + this.z * x - this.x * z,
            this.w * z + this.z * w + this.x * y - this.y * x,
            this.w * w - this.x * x - this.y * y - this.z * z)
    }

    /**
     * Divides the components of this quaternion by a float scalar.
     *
     * @param a The division scalar
     * @return A new quaternion, which has each component divided by the scalar
     */
    operator fun div(a: Float): Quaternion {
        return div(a.toDouble())
    }

    /**
     * Divides the components of this quaternion by a double scalar.
     *
     * @param a The division scalar
     * @return A new quaternion, which has each component divided by the scalar
     */
    operator fun div(a: Double): Quaternion {
        return Quaternion(x / a, y / a, z / a, w / a)
    }

    /**
     * Divides this quaternions by another one.
     *
     * @param q The quaternion to divide with
     * @return The quotient of the two quaternions
     */
    operator fun div(q: Quaternion): Quaternion {
        return div(q.x, q.y, q.z, q.w)
    }

    /**
     * Divides this quaternions by the float components of another one.
     *
     * @param x The x (imaginary) component of the quaternion to divide with
     * @param y The y (imaginary) component of the quaternion to divide with
     * @param z The z (imaginary) component of the quaternion to divide with
     * @param w The w (real) component of the quaternion to divide with
     * @return The quotient of the two quaternions
     */
    fun div(x: Float, y: Float, z: Float, w: Float): Quaternion {
        return div(x.toDouble(), y.toDouble(), z.toDouble(), w.toDouble())
    }

    /**
     * Divides this quaternions by the double components of another one.
     *
     * @param x The x (imaginary) component of the quaternion to divide with
     * @param y The y (imaginary) component of the quaternion to divide with
     * @param z The z (imaginary) component of the quaternion to divide with
     * @param w The w (real) component of the quaternion to divide with
     * @return The quotient of the two quaternions
     */
    fun div(x: Double, y: Double, z: Double, w: Double): Quaternion {
        val d = x * x + y * y + z * z + w * w
        return Quaternion(
            (this.x * w - this.w * x - this.z * y + this.y * z) / d,
            (this.y * w + this.z * x - this.w * y - this.x * z) / d,
            (this.z * w - this.y * x + this.x * y - this.w * z) / d,
            (this.w * w + this.x * x + this.y * y + this.z * z) / d)
    }

    /**
     * Returns the dot product of this quaternion with another one.
     *
     * @param q The quaternion to calculate the dot product with
     * @return The dot product of the two quaternions
     */
    fun dot(q: Quaternion): Double {
        return dot(q.x, q.y, q.z, q.w)
    }

    /**
     * Returns the dot product of this quaternion with the float components of another one.
     *
     * @param x The x (imaginary) component of the quaternion to calculate the dot product with
     * @param y The y (imaginary) component of the quaternion to calculate the dot product with
     * @param z The z (imaginary) component of the quaternion to calculate the dot product with
     * @param w The w (real) component of the quaternion to calculate the dot product with
     * @return The dot product of the two quaternions
     */
    fun dot(x: Float, y: Float, z: Float, w: Float): Double {
        return dot(x.toDouble(), y.toDouble(), z.toDouble(), w.toDouble())
    }

    /**
     * Returns the dot product of this quaternion with the double components of another one.
     *
     * @param x The x (imaginary) component of the quaternion to calculate the dot product with
     * @param y The y (imaginary) component of the quaternion to calculate the dot product with
     * @param z The z (imaginary) component of the quaternion to calculate the dot product with
     * @param w The w (real) component of the quaternion to calculate the dot product with
     * @return The dot product of the two quaternions
     */
    fun dot(x: Double, y: Double, z: Double, w: Double): Double {
        return this.x * x + this.y * y + this.z * z + this.w * w
    }

    /**
     * Rotates a vector by this quaternion.
     *
     * @param v The vector to rotate
     * @return The rotated vector
     */
    fun rotate(v: Vec3d): Vec3d {
        return rotate(v.getX(), v.getY(), v.getZ())
    }

    /**
     * Rotates the float components of a vector by this quaternion.
     *
     * @param x The x component of the vector
     * @param y The y component of the vector
     * @param z The z component of the vector
     * @return The rotated vector
     */
    fun rotate(x: Float, y: Float, z: Float): Vec3d {
        return rotate(x.toDouble(), y.toDouble(), z.toDouble())
    }

    /**
     * Rotates the double components of a vector by this quaternion.
     *
     * @param x The x component of the vector
     * @param y The y component of the vector
     * @param z The z component of the vector
     * @return The rotated vector
     */
    fun rotate(x: Double, y: Double, z: Double): Vec3d {
        val length = length()
        if (abs(length) < DBL_EPSILON) {
            throw ArithmeticException("Cannot rotate by the zero quaternion")
        }
        val nx = this.x / length
        val ny = this.y / length
        val nz = this.z / length
        val nw = this.w / length
        val px = nw * x + ny * z - nz * y
        val py = nw * y + nz * x - nx * z
        val pz = nw * z + nx * y - ny * x
        val pw = -nx * x - ny * y - nz * z
        return Vec3d(
            pw * -nx + px * nw - py * nz + pz * ny,
            pw * -ny + py * nw - pz * nx + px * nz,
            pw * -nz + pz * nw - px * ny + py * nx)
    }

    /**
     * Conjugates the quaternion. <br></br> Conjugation of a quaternion `a` is an operation returning quaternion `a'` such that `a' * a = a * a' = |a|<sup>2</sup>` where
     * `|a|<sup>2<sup></sup></sup>` is squared length of `a`.
     *
     * @return the conjugated quaternion
     */
    fun conjugate(): Quaternion {
        return Quaternion(-x, -y, -z, w)
    }

    /**
     * Inverts the quaternion. <br></br> Inversion of a quaternion `a` returns quaternion `a<sup>-1</sup> = a' / |a|<sup>2</sup>` where `a'` is [ conjugation][.conjugate] of `a`, and `|a|<sup>2</sup>` is squared length of `a`. <br></br> For any quaternions `a, b, c`, such that `a * b = c` equations
     * `a<sup>-1</sup> * c = b` and `c * b<sup>-1</sup> = a` are true.
     *
     * @return the inverted quaternion
     */
    fun invert(): Quaternion {
        val lengthSquared = lengthSquared()
        if (abs(lengthSquared) < DBL_EPSILON) {
            throw ArithmeticException("Cannot invert a quaternion of length zero")
        }
        return conjugate().div(lengthSquared)
    }

    /**
     * Returns the square of the length of this quaternion.
     *
     * @return The square of the length
     */
    fun lengthSquared(): Double {
        return x * x + y * y + z * z + w * w
    }

    /**
     * Returns the length of this quaternion.
     *
     * @return The length
     */
    fun length(): Double {
        return sqrt(lengthSquared())
    }

    /**
     * Normalizes this quaternion.
     *
     * @return A new quaternion of unit length
     */
    fun normalize(): Quaternion {
        val length = length()
        if (abs(length) < DBL_EPSILON) {
            throw ArithmeticException("Cannot normalize the zero quaternion")
        }
        return Quaternion(x / length, y / length, z / length, w / length)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o !is Quaternion) {
            return false
        }
        val quaternion = o as Quaternion?
        if (quaternion!!.w.compareTo(w) != 0) {
            return false
        }
        if (quaternion.x.compareTo(x) != 0) {
            return false
        }
        if (quaternion.y.compareTo(y) != 0) {
            return false
        }
        return if (quaternion.z.compareTo(z) != 0) {
            false
        } else true
    }

    override fun hashCode(): Int {
        if (hashCode != 0) {
            var result = if (x != +0.0) x.hashCode() else 0
            result = 31 * result + if (y != +0.0) y.hashCode() else 0
            result = 31 * result + if (z != +0.0) z.hashCode() else 0
            result = 31 * result + if (w != +0.0) w.hashCode() else 0
            hashCode = if(result == 0) 1 else result
        }
        return hashCode
    }

    override fun compareTo(q: Quaternion): Int {
        return sign(lengthSquared() - q.lengthSquared()).toInt()
    }

    public override fun clone(): Quaternion {
        return Quaternion(this)
    }

    override fun toString(): String {
        return "($x, $y, $z, $w)"
    }

    companion object {
        private val DBL_EPSILON = Double.fromBits(0x3cb0000000000000L)

        /**
         * An immutable identity (0, 0, 0, 0) quaternion.
         */
        val ZERO = Quaternion(0f, 0f, 0f, 0f)
        /**
         * An immutable identity (0, 0, 0, 1) quaternion.
         */
        val IDENTITY = Quaternion(0f, 0f, 0f, 1f)

        /**
         * Creates a new quaternion from the double real component.
         *
         *
         * The [.ZERO] constant is re-used when `w` is 0.
         *
         * @param w The w (real) component
         * @return The quaternion created from the double real component
         */
        fun fromReal(w: Double): Quaternion {
            return if (w == 0.0) ZERO else Quaternion(0.0, 0.0, 0.0, w)
        }

        /**
         * Creates a new quaternion from the double imaginary components.
         *
         *
         * The [.ZERO] constant is re-used when `x`, `y`, and `z` are 0.
         *
         * @param x The x (imaginary) component
         * @param y The y (imaginary) component
         * @param z The z (imaginary) component
         * @return The quaternion created from the double imaginary components
         */
        fun fromImaginary(x: Double, y: Double, z: Double): Quaternion {
            return if (x == 0.0 && y == 0.0 && z == 0.0) ZERO else Quaternion(x, y, z, 0.0)
        }

        /**
         * Creates a new quaternion from the double components.
         *
         *
         * The [.ZERO] constant is re-used when `x`, `y`, `z`, and `w` are 0.
         *
         * @param x The x (imaginary) component
         * @param y The y (imaginary) component
         * @param z The z (imaginary) component
         * @param w The w (real) component
         * @return The quaternion created from the double components
         */
        fun from(x: Double, y: Double, z: Double, w: Double): Quaternion {
            return if (x == 0.0 && y == 0.0 && z == 0.0 && w == 0.0) ZERO else Quaternion(x, y, z, w)
        }

        /**
         * Creates a new quaternion from the float angles in degrees around the x, y and z axes.
         *
         * @param pitch The rotation around x
         * @param yaw The rotation around y
         * @param roll The rotation around z
         * @return The quaternion defined by the rotations around the axes
         */
        fun fromAxesAnglesDeg(pitch: Float, yaw: Float, roll: Float): Quaternion {
            return fromAxesAnglesDeg(pitch.toDouble(), yaw.toDouble(), roll.toDouble())
        }

        /**
         * Creates a new quaternion from the float angles in radians around the x, y and z axes.
         *
         * @param pitch The rotation around x
         * @param yaw The rotation around y
         * @param roll The rotation around z
         * @return The quaternion defined by the rotations around the axes
         */
        fun fromAxesAnglesRad(pitch: Float, yaw: Float, roll: Float): Quaternion {
            return fromAxesAnglesRad(pitch.toDouble(), yaw.toDouble(), roll.toDouble())
        }

        /**
         * Creates a new quaternion from the double angles in degrees around the x, y and z axes.
         *
         * @param pitch The rotation around x
         * @param yaw The rotation around y
         * @param roll The rotation around z
         * @return The quaternion defined by the rotations around the axes
         */
        fun fromAxesAnglesDeg(pitch: Double, yaw: Double, roll: Double): Quaternion {
            return fromAngleDegAxis(yaw, vec(0, 1, 0)).mul(fromAngleDegAxis(pitch, vec(1, 0, 0))).mul(fromAngleDegAxis(roll, vec(0, 0, 1)))
        }

        /**
         * Creates a new quaternion from the double angles in radians around the x, y and z axes.
         *
         * @param pitch The rotation around x
         * @param yaw The rotation around y
         * @param roll The rotation around z
         * @return The quaternion defined by the rotations around the axes
         */
        fun fromAxesAnglesRad(pitch: Double, yaw: Double, roll: Double): Quaternion {
            return fromAngleRadAxis(yaw, vec(0, 1, 0)).mul(fromAngleRadAxis(pitch, vec(1, 0, 0))).mul(fromAngleRadAxis(roll, vec(0, 0, 1)))
        }

        /**
         * Creates a new quaternion from the angle-axis rotation defined from the first to the second vector.
         *
         * @param from The first vector
         * @param to The second vector
         * @return The quaternion defined by the angle-axis rotation between the vectors
         */
        fun fromRotationTo(from: Vec3d, to: Vec3d): Quaternion {
            return fromAngleRadAxis(acos((from dot to) / (from.length() * to.length())), from cross to)
        }

        /**
         * Creates a new quaternion from the rotation float angle in degrees around the axis vector.
         *
         * @param angle The rotation angle in degrees
         * @param axis The axis of rotation
         * @return The quaternion defined by the rotation around the axis
         */
        fun fromAngleDegAxis(angle: Float, axis: Vec3d): Quaternion {
            return fromAngleRadAxis(Math.toRadians(angle.toDouble()), axis)
        }

        /**
         * Creates a new quaternion from the rotation float angle in radians around the axis vector.
         *
         * @param angle The rotation angle in radians
         * @param axis The axis of rotation
         * @return The quaternion defined by the rotation around the axis
         */
        fun fromAngleRadAxis(angle: Float, axis: Vec3d): Quaternion {
            return fromAngleRadAxis(angle.toDouble(), axis)
        }

        /**
         * Creates a new quaternion from the rotation double angle in degrees around the axis vector.
         *
         * @param angle The rotation angle in degrees
         * @param axis The axis of rotation
         * @return The quaternion defined by the rotation around the axis
         */
        fun fromAngleDegAxis(angle: Double, axis: Vec3d): Quaternion {
            return fromAngleRadAxis(Math.toRadians(angle), axis)
        }

        /**
         * Creates a new quaternion from the rotation double angle in radians around the axis vector.
         *
         * @param angle The rotation angle in radians
         * @param axis The axis of rotation
         * @return The quaternion defined by the rotation around the axis
         */
        fun fromAngleRadAxis(angle: Double, axis: Vec3d): Quaternion {
            return fromAngleRadAxis(angle, axis.getX(), axis.getY(), axis.getZ())
        }

        /**
         * Creates a new quaternion from the rotation float angle in degrees around the axis vector float components.
         *
         * @param angle The rotation angle in degrees
         * @param x The x component of the axis vector
         * @param y The y component of the axis vector
         * @param z The z component of the axis vector
         * @return The quaternion defined by the rotation around the axis
         */
        fun fromAngleDegAxis(angle: Float, x: Float, y: Float, z: Float): Quaternion {
            return fromAngleRadAxis(Math.toRadians(angle.toDouble()), x.toDouble(), y.toDouble(), z.toDouble())
        }

        /**
         * Creates a new quaternion from the rotation float angle in radians around the axis vector float components.
         *
         * @param angle The rotation angle in radians
         * @param x The x component of the axis vector
         * @param y The y component of the axis vector
         * @param z The z component of the axis vector
         * @return The quaternion defined by the rotation around the axis
         */
        fun fromAngleRadAxis(angle: Float, x: Float, y: Float, z: Float): Quaternion {
            return fromAngleRadAxis(angle.toDouble(), x.toDouble(), y.toDouble(), z.toDouble())
        }

        /**
         * Creates a new quaternion from the rotation double angle in degrees around the axis vector double components.
         *
         * @param angle The rotation angle in degrees
         * @param x The x component of the axis vector
         * @param y The y component of the axis vector
         * @param z The z component of the axis vector
         * @return The quaternion defined by the rotation around the axis
         */
        fun fromAngleDegAxis(angle: Double, x: Double, y: Double, z: Double): Quaternion {
            return fromAngleRadAxis(Math.toRadians(angle), x, y, z)
        }

        /**
         * Creates a new quaternion from the rotation double angle in radians around the axis vector double components.
         *
         * @param angle The rotation angle in radians
         * @param x The x component of the axis vector
         * @param y The y component of the axis vector
         * @param z The z component of the axis vector
         * @return The quaternion defined by the rotation around the axis
         */
        fun fromAngleRadAxis(angle: Double, x: Double, y: Double, z: Double): Quaternion {
            val halfAngle = angle / 2
            val q = sin(halfAngle) / sqrt(x * x + y * y + z * z)
            return Quaternion(x * q, y * q, z * q, cos(halfAngle))
        }

        /**
         * Creates a new quaternion from the rotation matrix. The matrix will be interpreted as a rotation matrix even if it is not.
         *
         * @param matrix The rotation matrix
         * @return The quaternion defined by the rotation matrix
         */
        fun fromRotationMatrix(matrix: Matrix3d): Quaternion {
            val trace = matrix.trace()
            if (trace < 0) {
                if (matrix[1, 1] > matrix[0, 0]) {
                    if (matrix[2, 2] > matrix[1, 1]) {
                        val r = sqrt(matrix[2, 2] - matrix[0, 0] - matrix[1, 1] + 1)
                        val s = 0.5f / r
                        return Quaternion(
                            (matrix[2, 0] + matrix[0, 2]) * s,
                            (matrix[1, 2] + matrix[2, 1]) * s,
                            0.5f * r,
                            (matrix[1, 0] - matrix[0, 1]) * s)
                    } else {
                        val r = sqrt(matrix[1, 1] - matrix[2, 2] - matrix[0, 0] + 1)
                        val s = 0.5f / r
                        return Quaternion(
                            (matrix[0, 1] + matrix[1, 0]) * s,
                            0.5f * r,
                            (matrix[1, 2] + matrix[2, 1]) * s,
                            (matrix[0, 2] - matrix[2, 0]) * s)
                    }
                } else if (matrix[2, 2] > matrix[0, 0]) {
                    val r = sqrt(matrix[2, 2] - matrix[0, 0] - matrix[1, 1] + 1)
                    val s = 0.5f / r
                    return Quaternion(
                        (matrix[2, 0] + matrix[0, 2]) * s,
                        (matrix[1, 2] + matrix[2, 1]) * s,
                        0.5f * r,
                        (matrix[1, 0] - matrix[0, 1]) * s)
                } else {
                    val r = sqrt(matrix[0, 0] - matrix[1, 1] - matrix[2, 2] + 1)
                    val s = 0.5f / r
                    return Quaternion(
                        0.5f * r,
                        (matrix[0, 1] + matrix[1, 0]) * s,
                        (matrix[2, 0] - matrix[0, 2]) * s,
                        (matrix[2, 1] - matrix[1, 2]) * s)
                }
            } else {
                val r = sqrt(trace + 1)
                val s = 0.5f / r
                return Quaternion(
                    (matrix[2, 1] - matrix[1, 2]) * s,
                    (matrix[0, 2] - matrix[2, 0]) * s,
                    (matrix[1, 0] - matrix[0, 1]) * s,
                    0.5f * r)
            }
        }
    }
}
/**
 * Constructs a new quaternion. The components are set to the identity (0, 0, 0, 1).
 */
