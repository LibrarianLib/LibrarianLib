package com.teamwizardry.librarianlib.models

import com.teamwizardry.librarianlib.core.util.Client
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
    var obj: Obj = load()

    private fun load(): Obj {
        val objLocation = ResourceLocation(location.namespace, location.path + ".obj")
        if(!Client.resourceManager.hasResource(objLocation))
            return Objs.create()
        return ObjUtils.convertToRenderable(
            ObjReader.read(Client.resourceManager.getResource(objLocation).inputStream)
        )
    }

    companion object {
        private val reloadList = mutableListOf<WeakReference<Model>>()

        init {
            Client.resourceReloadHandler.register(VanillaResourceType.MODELS) {
                reloadList.removeIf { model ->
                    model.get()?.also {
                        it.obj = it.load()
                    } == null
                }
            }
        }
    }
}