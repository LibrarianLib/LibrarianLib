package com.teamwizardry.librarianlib.models.testmod

import com.teamwizardry.librarianlib.models.Model
import com.teamwizardry.librarianlib.models.testmod.models.SimpleModel
import com.teamwizardry.librarianlib.models.testmod.models.StaticModel
import com.teamwizardry.librarianlib.testbase.TestMod
import com.teamwizardry.librarianlib.testbase.objects.TestEntity
import com.teamwizardry.librarianlib.testbase.objects.TestEntityConfig
import com.teamwizardry.librarianlib.testbase.objects.TestEntityRenderer
import com.teamwizardry.librarianlib.testbase.objects.TestItem
import com.teamwizardry.librarianlib.testbase.objects.TestItemConfig
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager

@Mod("librarianlib-models-test")
class LibModelsTestModule: TestMod("models", "Models", logger) {
    val models = listOf<TestModel<*>>(
        SimpleModel("static", "Static model"),
        SimpleModel("static_textured", "Static textured model"),
        SimpleModel("simple_bone", "Simple single-bone model")
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
