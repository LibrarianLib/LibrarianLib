package com.teamwizardry.librarianlib.models

import com.teamwizardry.librarianlib.math.CoordinateSpace3D
import com.teamwizardry.librarianlib.math.Matrix3d
import com.teamwizardry.librarianlib.math.Matrix4d
import com.teamwizardry.librarianlib.math.MutableMatrix3d
import com.teamwizardry.librarianlib.math.MutableMatrix4d
import com.teamwizardry.librarianlib.math.Quaternion
import com.teamwizardry.librarianlib.math.WorldSpace
import com.teamwizardry.librarianlib.math.cross
import com.teamwizardry.librarianlib.math.minus
import com.teamwizardry.librarianlib.math.times
import com.teamwizardry.librarianlib.math.vec
import de.javagl.obj.FloatTuples
import de.javagl.obj.ObjArmature
import de.javagl.obj.ObjBoneIndex
import de.javagl.obj.ObjBoneIndexes
import de.javagl.obj.ObjUtils
import de.javagl.obj.Objs
import de.javagl.obj.ReadableObj
import net.minecraft.util.math.Vec3d

// much of this logic is based around this: https://veeenu.github.io/2014/05/09/implementing-skeletal-animation.html

class ModelInstance(val root: Model) {
    private var _model = Objs.create()
    val model: ReadableObj get() = _model
    var armatures: List<Armature> = listOf()
        private set
    private val bones: MutableMap<ObjBoneIndex, Bone> = mutableMapOf()
    private var armatureMap: Map<String, Armature> = mapOf()

    init {
        root.register(this)
        load()
    }

    operator fun get(name: String): Armature = armatureMap.getValue(name)

    fun updateTransforms() {
        fun update(bone: Bone) {
            bone.updateMatrices()
            bone.children.forEach {
                update(it)
            }
        }
        armatures.forEach { armature ->
            armature.rootBones.forEach {
                update(it)
            }
        }

        for(vertexIndex in 0 until model.numVertices) {
            val vertex = root.obj.getVertex(vertexIndex)
            val weights = root.obj.getWeights(vertexIndex)
            var weightSum = 0.0
            var xSum = 0.0
            var ySum = 0.0
            var zSum = 0.0
            if(weights != null) {
                for(weightIndex in 0 until weights.numWeights) {
                    val boneIndex = weights.getBoneIndex(weightIndex)
                    val weight = weights.getWeight(weightIndex)
                    val bone = bones[boneIndex] ?: continue
                    val transformed = bone.finalMatrix * vec(vertex.x, vertex.y, vertex.z)
                    xSum += transformed.x * weight
                    ySum += transformed.y * weight
                    zSum += transformed.z * weight
                    weightSum += weight
                }
            }
            if(weightSum != 0.0) {
                _model.setVertex(vertexIndex, FloatTuples.create(
                    (xSum / weightSum).toFloat(),
                    (ySum / weightSum).toFloat(),
                    (zSum / weightSum).toFloat()
                ))
            }
        }
    }

    fun load() {
        _model = Objs.create()
        ObjUtils.add(root.obj, _model)
        val armatures = mutableListOf<Armature>()
        for(armatureIndex in 0 until _model.numArmatures) {
            val objArmature = _model.getArmature(armatureIndex)
            val armature = Armature(objArmature.name, armatureIndex)
            armatures.add(armature)
            armature.loadObj(model, objArmature)
            armature.bones.forEachIndexed { boneIndex, bone ->
                this.bones[ObjBoneIndexes.create(armatureIndex, boneIndex)] = bone
            }
        }
        this.armatures = armatures
        this.armatureMap = armatures.associateBy { it.name }
    }
}

class Armature(val name: String, val index: Int) {
    var bones: List<Bone> = listOf()
        private set
    var rootBones: List<Bone> = listOf()
        private set
    private var boneMap: Map<String, Bone> = mapOf()

    operator fun get(name: String): Bone = boneMap.getValue(name)

    fun loadObj(obj: ReadableObj, objArmature: ObjArmature) {
        val bones = mutableListOf<Bone>()
        val rootBones = mutableListOf<Bone>()

        for(boneIndex in 0 until objArmature.numBones) {
            val objBone = objArmature.getBone(boneIndex)
            val parent = if(objBone.parent < 0) null else bones[objBone.parent]
            var head = vec(objBone.head.x, objBone.head.y, objBone.head.z)
            var tail = vec(objBone.tail.x, objBone.tail.y, objBone.tail.z)

            if(parent != null) {
                head = parent.worldToRest * head
                tail = parent.worldToRest * tail
            }

            val bone = Bone(parent, objBone.name, head, tail)
            bone.updateMatrices()
            bones.add(bone)
            if(parent == null)
                rootBones.add(bone)
        }

        this.bones = bones
        this.rootBones = rootBones
        this.boneMap = bones.associateBy { it.name }
    }
}

class Bone(
    /**
     * This bone's parent, or null if it a root bone
     */
    val parent: Bone?,
    /**
     * This bone's name
     */
    val name: String,
    /**
     * The "head" position of this bone. The head position is the "origin" of the bone, and is often equal to its
     * parent's [tail] position. In Blender's default bone visualization style, the fat end of the bone corresponds to
     * the bone's head, and the narrow end corresponds to the tail.
     */
    val head: Vec3d,
    /**
     * The "tail" position of this bone. The tail position is the "end" or "tip" of the bone. In Blender's default bone
     * visualization style, the narrow end of the bone corresponds to the bone's tail, and the fat end corresponds to
     * the head.
     */
    val tail: Vec3d
): CoordinateSpace3D {
    val children: MutableList<Bone> = mutableListOf()
    init {
        parent?.children?.add(this)
    }

    /**
     * The local transformation of this bone at rest, relative to its parent.
     */
    val restTransform: Matrix4d = run {
        val up = if (head.x == tail.x && head.z == tail.z)
            vec(0, 0, 1)
        else
            vec(0, 1, 0)

        Matrix4d.temporaryMatrix.also {
            it.set(Matrix4d.IDENTITY)
            it.translate(head)
        } * Matrix4d.createLookAt(head, tail, up)
    }
    /**
     * The transformation to take points in the world space into this bone's local space
     */
    val worldToRest: Matrix4d = ((parent?.restTransform ?: Matrix4d.IDENTITY) * restTransform).invert()

    /**
     *
     */
    var localTransform: Matrix4d = Matrix4d.IDENTITY

    /**
     *
     */
    var rotation: Quaternion = Quaternion()

    /**
     *
     */
    var translation: Vec3d = vec(0, 0, 0)

    override val parentSpace: CoordinateSpace3D? = parent ?: WorldSpace
    override var matrix: Matrix4d = Matrix4d.IDENTITY
        private set
    override var inverseMatrix: Matrix4d = Matrix4d.IDENTITY
        private set

    var finalMatrix: Matrix4d = Matrix4d.IDENTITY
        private set

    fun updateMatrices() {
        val local = MutableMatrix4d()
        local.rotate(rotation)
        //local.translate(translation)
        localTransform = local.toImmutable()

        matrix = localTransform * restTransform
        inverseMatrix = matrix.invert()
        finalMatrix = worldToRest * this.conversionMatrixTo(WorldSpace)
    }
}