package com.teamwizardry.librarianlib.models.testmod

import com.teamwizardry.librarianlib.models.Model
import com.teamwizardry.librarianlib.testbase.objects.TestEntity
import net.minecraft.util.ResourceLocation

abstract class TestModel<T>(val id: String, val name: String) {
    var model: Model = Model(ResourceLocation("librarianlib-models-test:models/entity/$id"))
    abstract fun createState(): T
    abstract fun tickState(state: T)
    abstract fun render(entity: TestEntity, partialTicks: Float, state: T)
}