package com.teamwizardry.librarianlib.models.testmod.models

import com.teamwizardry.librarianlib.models.ModelInstance
import com.teamwizardry.librarianlib.models.ModelRenderer
import com.teamwizardry.librarianlib.models.testmod.TestModel
import com.teamwizardry.librarianlib.models.testmod.logger
import com.teamwizardry.librarianlib.testbase.objects.TestEntity

open class SimpleModel(id: String, name: String): TestModel<Any?>(id, name) {
    val modelInstance = ModelInstance(model)

    override fun createState(): Any? = null

    override fun tickState(state: Any?) {

    }

    override fun render(entity: TestEntity, partialTicks: Float, state: Any?) {
        try {
            ModelRenderer.render(modelInstance)
        } catch (e: Exception) {
            logger.error(e)
        }
    }
}