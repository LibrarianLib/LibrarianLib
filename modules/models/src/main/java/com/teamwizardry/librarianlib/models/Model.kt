package com.teamwizardry.librarianlib.models

import com.teamwizardry.librarianlib.core.util.Client
import com.teamwizardry.librarianlib.core.util.kotlin.filename
import com.teamwizardry.librarianlib.core.util.kotlin.getResourceOrNull
import com.teamwizardry.librarianlib.core.util.kotlin.resolveSibling
import de.javagl.obj.Mtl
import de.javagl.obj.MtlReader
import de.javagl.obj.Obj
import de.javagl.obj.ObjReader
import de.javagl.obj.ObjUtils
import de.javagl.obj.Objs
import net.minecraft.util.ResourceLocation
import net.minecraftforge.resource.VanillaResourceType
import java.lang.ref.WeakReference

class Model(val location: ResourceLocation) {

    init {
        reloadList.add(WeakReference(this))
        load()
    }

    val default = ModelInstance(this)
    lateinit var obj: Obj
    lateinit var mtls: MutableMap<String, Mtl>

    private fun load() {
        val objFile = Client.resourceManager.getResourceOrNull(location.resolveSibling(location.filename + ".obj"))
        if(objFile != null) {
            obj = objFile.inputStream.use {
                ObjUtils.convertToRenderable(ObjReader.read(it))
            }
            mtls = obj.mtlFileNames.flatMap { mtlName ->
                val mtlFile = Client.resourceManager.getResourceOrNull(location.resolveSibling(mtlName))
                mtlFile?.inputStream?.use {
                    MtlReader.read(it)
                } ?: emptyList()
            }.associateBy { it.name }.toMutableMap()
        } else {
            obj = Objs.create()
            mtls = mutableMapOf()
        }
    }

    companion object {
        private val reloadList = mutableListOf<WeakReference<Model>>()

        init {
            Client.resourceReloadHandler.register(VanillaResourceType.MODELS) {
                reloadList.removeIf { model ->
                    model.get()?.also {
                        it.load()
                    } == null
                }
            }
        }
    }
}