package com.teamwizardry.librarianlib.testbase.objects


import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class TestEntityDelegateRenderer(renderManagerIn: EntityRendererManager): EntityRenderer<TestEntity>(renderManagerIn) {
    /**
     * Renders the desired `T` type Entity.
     */
    override fun doRender(entity: TestEntity, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.disableTexture()
        GlStateManager.pushMatrix()
        GlStateManager.translatef(x.toFloat(), y.toFloat(), z.toFloat())
//        GlStateManager.rotatef(MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw) - 90.0f, 0.0f, 1.0f, 0.0f)
//        GlStateManager.rotatef(MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch), 0.0f, 0.0f, 1.0f)

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial()
            GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity))
        }

        entity.config.renderer.render(entity, partialTicks)

        if (this.renderOutlines) {
            GlStateManager.tearDownSolidRenderingTextureCombine()
            GlStateManager.disableColorMaterial()
        }

        GlStateManager.popMatrix()
        GlStateManager.enableTexture()
        super.doRender(entity, x, y, z, entityYaw, partialTicks)
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    override fun getEntityTexture(entity: TestEntity): ResourceLocation? {
        return ResourceLocation("missingno")
    }
}
