package com.teamwizardry.librarianlib.testbase.objects


import net.minecraft.client.renderer.texture.AtlasTexture
import net.minecraft.entity.item.TNTEntity
import net.minecraft.util.ResourceLocation
import com.mojang.blaze3d.platform.GlStateManager
import com.teamwizardry.librarianlib.core.util.kotlin.color
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
class TestEntityRenderer(renderManagerIn: EntityRendererManager): EntityRenderer<TestEntity>(renderManagerIn) {
    /**
     * Renders the desired `T` type Entity.
     */
    override fun doRender(entity: TestEntity, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        this.bindEntityTexture(entity)
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

//        drawCube(entity)
        renderOffsetAABB(entity.relativeBoundingBox, 0.0, 0.0, 0.0)
        drawLook(entity, partialTicks)

        if (this.renderOutlines) {
            GlStateManager.tearDownSolidRenderingTextureCombine()
            GlStateManager.disableColorMaterial()
        }

        GlStateManager.popMatrix()
        GlStateManager.enableTexture()
        super.doRender(entity, x, y, z, entityYaw, partialTicks)
    }

    fun drawLook(entity: TestEntity, partialTicks: Float) {
        GlStateManager.disableTexture()
        GlStateManager.disableLighting()
        GlStateManager.lineWidth(2f)

        val color = Color.BLUE

        val tessellator = Tessellator.getInstance()
        val vb = tessellator.buffer
        vb.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR)

        vb.pos(0, 0, 0).color(color).endVertex()
        vb.pos(entity.getLook(partialTicks) * entity.config.lookLength).color(color).endVertex()

        tessellator.draw()

        GlStateManager.lineWidth(1f)
        GlStateManager.enableTexture()
        GlStateManager.enableLighting()
    }

    fun drawCube(entity: TestEntity) {
        val tessellator = Tessellator.getInstance()
        val vb = tessellator.buffer
        val size = entity.getSize(Pose.STANDING)
        val width = size.width.toDouble()
        val height = size.height.toDouble()
        val color = Color.WHITE

        vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR)

        // bottom (-Y)
        vb.pos(-width/2, -height/2, -width/2).color(color).endVertex()
        vb.pos(+width/2, -height/2, -width/2).color(color).endVertex()
        vb.pos(+width/2, -height/2, +width/2).color(color).endVertex()
        vb.pos(-width/2, -height/2, +width/2).color(color).endVertex()

        // top (+Y)
        vb.pos(-width/2, +height/2, -width/2).color(color).endVertex()
        vb.pos(-width/2, +height/2, +width/2).color(color).endVertex()
        vb.pos(+width/2, +height/2, +width/2).color(color).endVertex()
        vb.pos(+width/2, +height/2, -width/2).color(color).endVertex()

        // north (-Z)
        vb.pos(-width/2, -height/2, -width/2).color(color).endVertex()
        vb.pos(-width/2, +height/2, -width/2).color(color).endVertex()
        vb.pos(+width/2, +height/2, -width/2).color(color).endVertex()
        vb.pos(+width/2, -height/2, -width/2).color(color).endVertex()

        // south (+Z)
        vb.pos(-width/2, -height/2, +width/2).color(color).endVertex()
        vb.pos(+width/2, -height/2, +width/2).color(color).endVertex()
        vb.pos(+width/2, +height/2, +width/2).color(color).endVertex()
        vb.pos(-width/2, +height/2, +width/2).color(color).endVertex()

        // west (-X)
        vb.pos(-width/2, -height/2, -width/2).color(color).endVertex()
        vb.pos(-width/2, -height/2, +width/2).color(color).endVertex()
        vb.pos(-width/2, +height/2, +width/2).color(color).endVertex()
        vb.pos(-width/2, +height/2, -width/2).color(color).endVertex()

        // east (+X)
        vb.pos(+width/2, -height/2, -width/2).color(color).endVertex()
        vb.pos(+width/2, +height/2, -width/2).color(color).endVertex()
        vb.pos(+width/2, +height/2, +width/2).color(color).endVertex()
        vb.pos(+width/2, -height/2, +width/2).color(color).endVertex()

        tessellator.draw()
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    override fun getEntityTexture(entity: TestEntity): ResourceLocation? {
        return ResourceLocation("missingno")
    }
}