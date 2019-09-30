package com.teamwizardry.librarianlib.models

import com.teamwizardry.librarianlib.core.util.Client
import com.teamwizardry.librarianlib.core.util.kotlin.filename
import com.teamwizardry.librarianlib.core.util.kotlin.getResourceOrNull
import com.teamwizardry.librarianlib.core.util.kotlin.resolveSibling
import de.javagl.obj.Act
import de.javagl.obj.ActReader
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

    lateinit var obj: Obj
        private set
    lateinit var mtls: MutableMap<String, Mtl>
        private set
    lateinit var actions: Act
        private set
    private val instances = mutableListOf<WeakReference<ModelInstance>>()

    init {
        reloadList.add(WeakReference(this))
        reload()
    }

    val default = ModelInstance(this)

    fun reload() {
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
            actions = Act()
            obj.actFileNames.forEach { actName ->
                val actFile = Client.resourceManager.getResourceOrNull(location.resolveSibling(actName))
                actFile?.inputStream?.use {
                    actions.merge(ActReader.read(it))
                }
            }
        } else {
            obj = Objs.create()
            mtls = mutableMapOf()
            actions = Act()
        }

        instances.removeIf { instance ->
            instance.get()?.also {
                it.load()
            } == null
        }
    }

    internal fun register(instance: ModelInstance) {
        instances.add(WeakReference(instance))
    }

    companion object {
        private val reloadList = mutableListOf<WeakReference<Model>>()

        init {
            Client.resourceReloadHandler.register(VanillaResourceType.MODELS) {
                reloadAll()
            }
        }

        internal fun reloadAll() {
            reloadList.removeIf { model ->
                model.get()?.also {
                    it.reload()
                } == null
            }
        }
    }
}