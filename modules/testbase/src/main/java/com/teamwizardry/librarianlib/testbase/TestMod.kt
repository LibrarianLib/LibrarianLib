package com.teamwizardry.librarianlib.testbase

import com.teamwizardry.librarianlib.core.LibrarianLibModule
import com.teamwizardry.librarianlib.core.util.Client
import com.teamwizardry.librarianlib.core.util.ClientRunnable
import com.teamwizardry.librarianlib.core.util.DistinctColors
import com.teamwizardry.librarianlib.core.util.kotlin.obf
import com.teamwizardry.librarianlib.core.util.kotlin.unmodifiableView
import com.teamwizardry.librarianlib.testbase.objects.TestItem
import com.teamwizardry.librarianlib.testbase.objects.TestObject
import com.teamwizardry.librarianlib.virtualresources.VirtualResources
import net.minecraft.block.Block
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.client.renderer.color.ItemColors
import net.minecraft.client.renderer.model.IUnbakedModel
import net.minecraft.client.renderer.model.ModelResourceLocation
import net.minecraft.client.renderer.model.ModelRotation
import net.minecraft.client.resources.LanguageManager
import net.minecraft.client.resources.Locale
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.resources.IResourceManager
import net.minecraft.util.ResourceLocation
import net.minecraft.util.Util
import net.minecraft.util.registry.Registry
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ICustomModelLoader
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.client.model.ModelLoaderRegistry
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.resource.VanillaResourceType
import org.apache.logging.log4j.Logger
import java.awt.Color

abstract class TestMod(name: String, val humanName: String, logger: Logger): LibrarianLibModule("$name-test", logger) {

    val itemGroup = object : ItemGroup(modid) {
        private val stack: ItemStack by lazy {
            val stack = ItemStack(LibTestBaseModule.testTool)
            stack.orCreateTag.putString("mod", modid)
            return@lazy stack
        }
        override fun createIcon(): ItemStack {
            return stack
        }
    }

    val _items: MutableList<Item> = mutableListOf()
    val _blocks: MutableList<Block> = mutableListOf()
    val _entities: MutableList<EntityType<*>> = mutableListOf()

    val items: List<Item> = _items.unmodifiableView()
    val blocks: List<Block> = _blocks.unmodifiableView()
    val entities: List<EntityType<*>> = _entities.unmodifiableView()

    operator fun <T: Item> T.unaryPlus(): T {
        _items.add(this)
        return this
    }

    operator fun <T: Block> T.unaryPlus(): T {
        val item = BlockItem(this, Item.Properties())
        item.registryName = ResourceLocation(this.registryName!!.namespace, this.registryName!!.path + "_block")
        _blocks.add(this)
        _items.add(item)
        return this
    }

    operator fun <T: EntityType<*>> T.unaryPlus(): T {
        _entities.add(this)
        return this
    }

    init {
        LibTestBaseModule.add(this)
    }

    override fun setup(event: FMLCommonSetupEvent) {
    }

    override fun clientSetup(event: FMLClientSetupEvent) {
        entities.forEach {
//            RenderingRegistry.registerEntityRenderingHandler(entityClass) { ParticleSpawnerEntityRenderer(it) }
        }

        items.forEach { item ->
            if(item is TestItem) {
                val name = item.registryName!!
                VirtualResources.client.add(
                    ResourceLocation(name.namespace, "models/item/${name.path}.json"),
                    """
                        {
                            "parent": "librarianlib-testbase:item/test_tool"
                        }
                    """.trimIndent()
                )
            }
        }
    }

    @SubscribeEvent
    internal fun registerColors(colorHandlerEvent: ColorHandlerEvent.Item) {
        colorHandlerEvent.itemColors.register(IItemColor { stack, tintIndex ->
            if(tintIndex == 1 && stack.item is TestItem)
                DistinctColors.forObject(stack.item.registryName).rgb
            else
                Color.WHITE.rgb
        }, *items.toTypedArray())
    }

    override fun registerBlocks(blockRegistryEvent: RegistryEvent.Register<Block>) {
        blocks.forEach {
            ForgeRegistries.BLOCKS.register(it)
        }
    }

    override fun registerItems(itemRegistryEvent: RegistryEvent.Register<Item>) {
        items.forEach {
            ForgeRegistries.ITEMS.register(it)
        }
    }

    override fun registerEntities(entityRegistryEvent: RegistryEvent.Register<EntityType<*>>) {
        entities.forEach {
            ForgeRegistries.ENTITIES.register(it)
        }
    }
}