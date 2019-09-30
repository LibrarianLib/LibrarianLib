package com.teamwizardry.librarianlib.models.testmod.models

import com.teamwizardry.librarianlib.core.util.Client
import com.teamwizardry.librarianlib.models.Armature
import com.teamwizardry.librarianlib.models.ModelInstance
import com.teamwizardry.librarianlib.models.ModelRenderer
import com.teamwizardry.librarianlib.models.testmod.TestModel
import com.teamwizardry.librarianlib.models.testmod.logger
import com.teamwizardry.librarianlib.testbase.objects.TestEntity

open class ArmatureModel<S>(
    id: String, name: String,
    val stateGenerator: () -> S,
    val stateTicker: (ArmatureState<S>) -> Unit
): TestModel<ArmatureModel.ArmatureState<S>>(id, name) {
    override fun createState(): ArmatureState<S> = ArmatureState(ModelInstance(model), stateGenerator())

    override fun tickState(state: ArmatureState<S>) {
        try {
            stateTicker(state)
            state.model.updateTransforms()
        } catch (e: Exception) {
            logger.error(e)
        }
    }

    override fun render(entity: TestEntity, partialTicks: Float, state: ArmatureState<S>) {
        try {
            ModelRenderer.render(state.model)
            if(Client.minecraft.renderManager.isDebugBoundingBox)
                ModelRenderer.renderArmatures(state.model)
        } catch (e: Exception) {
            logger.error(e)
        }
    }

    class ArmatureState<S>(val model: ModelInstance, var data: S)
}
