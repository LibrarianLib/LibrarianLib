package com.teamwizardry.librarianlib.models

import com.mojang.blaze3d.platform.GlStateManager
import com.teamwizardry.librarianlib.core.util.Client
import com.teamwizardry.librarianlib.core.util.kotlin.color
import com.teamwizardry.librarianlib.core.util.kotlin.pos
import com.teamwizardry.librarianlib.core.util.kotlin.tex
import com.teamwizardry.librarianlib.math.WorldSpace
import com.teamwizardry.librarianlib.math.vec
import de.javagl.obj.FloatTuples
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.awt.Color

object ModelRenderer {
    @JvmStatic
    fun render(instance: ModelInstance) {
        val obj = instance.model

        val tessellator = Tessellator.getInstance()
        val vb = tessellator.buffer

        for(materialIndex in 0 until obj.numMaterialGroups) {
            val group = obj.getMaterialGroup(materialIndex)
            val mtl = instance.root.mtls[group.name]
            val textured = mtl?.mapKd != null

            if(textured) {
                Client.renderEngine.bindTexture(ResourceLocation(group.name))
                GlStateManager.enableTexture()
            } else {
                GlStateManager.disableTexture()
            }

            vb.begin(GL11.GL_TRIANGLES,
                if(textured)
                    DefaultVertexFormats.POSITION_TEX_NORMAL
                else
                    DefaultVertexFormats.POSITION_NORMAL
            )

            for (faceIndex in 0 until group.numFaces) {
                val face = group.getFace(faceIndex)
                for (index in 0 until face.numVertices) {
                    val vertex = obj.getVertex(face.getVertexIndex(index))
                    val normal = obj.getNormal(face.getNormalIndex(index))
                    val texCoord = if (textured && face.containsTexCoordIndices())
                        obj.getTexCoord(face.getTexCoordIndex(index))
                    else
                        FloatTuples.create(0f, 0f)

                    vb.pos(vertex.x, vertex.y, vertex.z)
                    if(textured)
                        vb.tex(texCoord.x, 1-texCoord.y) // 1-v because blender uses a bottom-left uv origin
                    vb.normal(normal.x, normal.y, normal.z)
                    vb.endVertex()
                }
            }

            tessellator.draw()
        }
    }

    @JvmStatic
    fun renderArmatures(instance: ModelInstance) {
        GlStateManager.disableTexture()
        GlStateManager.shadeModel(GL11.GL_SMOOTH)

        GlStateManager.depthFunc(GL11.GL_ALWAYS)
        GlStateManager.depthMask(false)
        GlStateManager.lineWidth(4f)
        instance.armatures.forEach {
            renderArmature(it, Color.DARK_GRAY)
        }

        GlStateManager.depthFunc(GL11.GL_LEQUAL)
        GlStateManager.depthMask(true)
        GlStateManager.lineWidth(2f)

        GlStateManager.shadeModel(GL11.GL_FLAT)
    }

    private fun renderArmature(armature: Armature, color: Color) {
        val tessellator = Tessellator.getInstance()
        val vb = tessellator.buffer

        vb.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR)
        armature.bones.forEach { bone ->
            val start = bone.convertPointTo(vec(0, 0, 0), WorldSpace)
            val end = bone.convertPointTo(vec(0, bone.length, 0), WorldSpace)
            vb.pos(start).color(Color(0xA020F0)).endVertex()
            vb.pos(end).color(Color.PINK).endVertex()
        }
        tessellator.draw()
    }
}