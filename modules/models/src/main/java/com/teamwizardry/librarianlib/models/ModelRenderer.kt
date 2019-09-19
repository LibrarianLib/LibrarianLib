package com.teamwizardry.librarianlib.models

import com.mojang.blaze3d.platform.GlStateManager
import com.teamwizardry.librarianlib.core.util.Client
import com.teamwizardry.librarianlib.core.util.kotlin.pos
import com.teamwizardry.librarianlib.core.util.kotlin.tex
import de.javagl.obj.FloatTuples
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

object ModelRenderer {
    @JvmStatic
    fun render(instance: ModelInstance) {
        val model = instance.model

        val tessellator = Tessellator.getInstance()
        val vb = tessellator.buffer

        for(materialIndex in 0 until model.obj.numMaterialGroups) {
            val group = model.obj.getMaterialGroup(materialIndex)
            val mtl = model.mtls[group.name]
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

            for (faceIndex in 0 until model.obj.numFaces) {
                val face = model.obj.getFace(faceIndex)
                for (index in 0 until face.numVertices) {
                    val vertex = model.obj.getVertex(face.getVertexIndex(index))
                    val normal = model.obj.getNormal(face.getNormalIndex(index))
                    val texCoord = if (textured && face.containsTexCoordIndices())
                        model.obj.getTexCoord(face.getTexCoordIndex(index))
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
}