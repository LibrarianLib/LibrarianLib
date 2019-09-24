package com.teamwizardry.librarianlib.models.testmod

import com.teamwizardry.librarianlib.math.Quaternion
import com.teamwizardry.librarianlib.math.vec
import com.teamwizardry.librarianlib.models.Bone
import com.teamwizardry.librarianlib.models.Model
import com.teamwizardry.librarianlib.models.testmod.models.ArmatureModel
import com.teamwizardry.librarianlib.models.testmod.models.SimpleModel
import com.teamwizardry.librarianlib.testbase.TestMod
import com.teamwizardry.librarianlib.testbase.objects.TestEntity
import com.teamwizardry.librarianlib.testbase.objects.TestEntityConfig
import com.teamwizardry.librarianlib.testbase.objects.TestEntityRenderer
import com.teamwizardry.librarianlib.testbase.objects.TestItem
import com.teamwizardry.librarianlib.testbase.objects.TestItemConfig
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Mod("librarianlib-models-test")
class LibModelsTestModule: TestMod("models", "Models", logger) {
    val models = listOfNotNull<TestModel<*>>(
        SimpleModel("static", "Static model"),
        SimpleModel("static_textured", "Static textured model"),

        ArmatureModel("simple_bone", "Simple single-bone model", { 0 }) {
            it.data++
            val bone = it.model["Armature"]["Bone"]
            bone.translation = vec(0, sin(Math.toRadians(it.data * 2.0)), 0)
            bone.rotation = Quaternion.fromAngleRadAxis(Math.toRadians(it.data % 360.0), 1.0, 0.0, 0.0)
        },
        ArmatureModel("shear_bone", "Shearing bone", { 0 }) {
            it.data++
            val bone = it.model["Armature"]["Bone"]
            val angle = Math.toRadians(it.data * 2.0)
            bone.translation = vec(sin(angle), 0, cos(angle))
        },
        ArmatureModel("envelope_weights", "Envelope weights", { 0 }) {
            it.data++
            val left = it.model["Armature"]["Left"]
            val right = it.model["Armature"]["Right"]
            var angle = Math.toRadians(it.data * 2.0)
            angle = PI/4 * sin(angle)
            val quaternion = Quaternion.fromAngleRadAxis(angle, 1.0, 0.0, 0.0)
            left.rotation = quaternion
            right.rotation = quaternion
        },
        ArmatureModel("bendy_tube", "Bendy tube chain", { 0 }) {
            it.data++
            val bones = it.model["Armature"].let {
                listOf(
                    it["Bone_1"],
                    it["Bone_2"],
                    it["Bone_3"]
                )
            }
            var angle = Math.toRadians((it.data * 2.0) % 360.0)
            angle = PI/4 * sin(angle)
            val quaternion = Quaternion.fromAngleRadAxis(angle, 1.0, 0.0, 0.0)
            bones.forEach { it.rotation = quaternion }
        },
        ArmatureModel("steve", "Steve", { 0 }) { state ->
            val cycle = sin((state.data / 40.0) * PI * 2)
            val angle = Math.toRadians(cycle * 20)

            val rot1 = Quaternion.fromAngleRadAxis(angle, 0.0, 0.0, 1.0)
            val rot2 = Quaternion.fromAngleRadAxis(angle, 0.0, 0.0, -1.0)
            val armature = state.model["Armature"]
            armature["Right_Arm"].rotation = rot1
            armature["Left_Leg"].rotation = rot1
            armature["Left_Arm"].rotation = rot2
            armature["Right_Leg"].rotation = rot2
            armature["Head"].rotation = Quaternion.fromAngleDegAxis(10.0, 0.0, 0.0, 1.0) * Quaternion.fromAngleRadAxis(angle, 0.0, 1.0, 0.0)

            state.data++
        },
        ArmatureModel("non_axis_aligned_bones", "Off-axis bones", { 0 }) { state ->
            val bones = state.model["Armature"].let {
                listOf(it["+z+x"], it["+z-x"], it["-z-x"], it["-z+x"])
            }

            bones.forEachIndexed { i, bone ->
                val ticks = state.data + i * 10
                val cycle = sin(ticks / 40.0)
                val angle = sin(Math.toRadians(cycle * 15.0))
                bone.rotation = Quaternion.fromAngleRadAxis(angle, 1.0, 0.0, 0.0)
            }

            state.data++
        },

        null
    )

    init {
        +TestItem(TestItemConfig("reload_models", "Reload Entity Models") {
            client {
                rightClick {
                    Model.reloadAll()
                }
            }
        })

        models.forEach { model ->
            @Suppress("UNCHECKED_CAST")
            model as TestModel<Any?>

            +TestEntityConfig(model.id, model.name) {
//                description = system.description

                stateFactory = { model.createState() }
                tick.client {
                    model.tickState(target.state)
                }

                renderer = object: TestEntityRenderer {
                    override fun render(entity: TestEntity, partialTicks: Float) {
                        model.render(entity, partialTicks, entity.state)
                    }
                }
            }
        }
    }
}

internal val logger = LogManager.getLogger("LibrarianLib/Models/Test")
