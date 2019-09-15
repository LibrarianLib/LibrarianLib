package com.teamwizardry.librarianlib.models.testmod

import com.teamwizardry.librarianlib.models.testmod.models.StaticModel
import com.teamwizardry.librarianlib.testbase.TestMod
import com.teamwizardry.librarianlib.testbase.objects.TestEntity
import com.teamwizardry.librarianlib.testbase.objects.TestEntityConfig
import com.teamwizardry.librarianlib.testbase.objects.TestEntityRenderer
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager

@Mod("librarianlib-models-test")
class LibModelsTestModule: TestMod("models", "Models", logger) {
    val models = listOf<TestModel<*>>(
        StaticModel
    )

    init {
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
