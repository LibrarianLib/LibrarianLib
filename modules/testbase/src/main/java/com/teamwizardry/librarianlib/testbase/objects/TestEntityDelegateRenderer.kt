package com.teamwizardry.librarianlib.testbase.objects


import net.minecraft.client.renderer.texture.AtlasTexture
import net.minecraft.entity.item.TNTEntity
import net.minecraft.util.ResourceLocation
import com.mojang.blaze3d.platform.GlStateManager
import com.teamwizardry.librarianlib.core.util.kotlin.color
import com.teamwizardry.librarianlib.core.util.kotlin.identityMapOf
import com.teamwizardry.librarianlib.core.util.kotlin.pos
import com.teamwizardry.librarianlib.math.fastSin
import com.teamwizardry.librarianlib.math.times
import net.minecraft.block.Blocks
import net.minecraft.util.math.MathHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BlockRendererDispatcher
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import realms.z
import realms.y
import realms.x
import net.minecraft.command.arguments.EntityArgument.entity
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.TippedArrowRenderer
import net.minecraft.entity.Pose
import net.minecraft.util.math.Vec3d
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.abs
import kotlin.math.tan

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
