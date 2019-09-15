package com.teamwizardry.librarianlib.models.testmod.models

import com.teamwizardry.librarianlib.core.util.kotlin.color
import com.teamwizardry.librarianlib.core.util.kotlin.pos
import com.teamwizardry.librarianlib.math.times
import com.teamwizardry.librarianlib.models.testmod.TestModel
import com.teamwizardry.librarianlib.testbase.objects.TestEntity
import de.javagl.obj.ObjData
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11

object StaticModel: TestModel<Any?>("static", "Static model") {
    override fun createState(): Any? = null

    override fun tickState(state: Any?) {

    }

    override fun render(entity: TestEntity, partialTicks: Float, state: Any?) {
        val tessellator = Tessellator.getInstance()
        val vb = tessellator.buffer

        vb.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION)

        for(faceIndex in 0 until model.obj.numFaces) {
            val face = model.obj.getFace(faceIndex)
            for(index in 0 until face.numVertices) {
                val vertex = model.obj.getVertex(face.getVertexIndex(index))
//                val normal = model.obj.getNormal(face.getNormalIndex(index))
//                val texCoord = model.obj.getTexCoord(face.getTexCoordIndex(index))

                vb.pos(vertex.x, vertex.y, vertex.z).endVertex()
            }
        }

        tessellator.draw()
    }
}