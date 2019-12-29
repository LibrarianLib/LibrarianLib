package com.teamwizardry.librarianlib.core.util

import com.google.common.collect.Lists
import com.teamwizardry.librarianlib.math.Vec2d
import com.teamwizardry.librarianlib.math.vec
import net.minecraft.client.MainWindow
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.resources.IFutureReloadListener
import net.minecraft.resources.IReloadableResourceManager
import net.minecraft.resources.IResourceManager
import net.minecraft.util.Timer
import net.minecraft.util.math.Vec3d
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.resource.IResourceType
import net.minecraftforge.resource.ISelectiveResourceReloadListener
import net.minecraftforge.resource.SelectiveReloadStateHandler

object Client {
    @JvmStatic
    val minecraft: Minecraft get() = Minecraft.getInstance()
    @JvmStatic
    val window: MainWindow get() = minecraft.mainWindow
    @JvmStatic
    val guiScaleFactor: Double get() = window.guiScaleFactor
    @JvmStatic
    val resourceManager: IResourceManager get() = minecraft.resourceManager
    @JvmStatic
    val renderEngine: TextureManager get() = minecraft.textureManager

    /**
     * The game time, as measured from the game launch
     */
    @JvmStatic
    val time: Time = object: Time() {
        override val ticks: Int
            get() = globalTicks
        override val partialTicks: Float
            get() = timer.renderPartialTicks
    }
    /**
     * The world time, as measured from the game launch
     */
    @JvmStatic
    val worldTime: Time = object: Time() {
        override val ticks: Int
            get() = worldTicks
        override val partialTicks: Float
            get() = renderPartialTicksPaused.getFloat(minecraft)
    }

    @JvmStatic
    val resourceReloadHandler = ResourceReload()

    abstract class Time {
        abstract val ticks: Int
        abstract val partialTicks: Float
        val time: Float get() = ticks + partialTicks
        val seconds: Float get() = time / 20

        fun interp(previous: Double, current: Double): Double {
            return previous + (current - previous) * partialTicks
        }

        @Suppress("NOTHING_TO_INLINE")
        inline fun interp(previous: Number, current: Number): Double = interp(previous.toDouble(), current.toDouble())

        fun interp(previous: Vec2d, current: Vec2d): Vec2d {
            return vec(interp(previous.x, current.x), interp(previous.y, current.y))
        }

        fun interp(previous: Vec3d, current: Vec3d): Vec3d {
            return vec(interp(previous.x, current.x), interp(previous.y, current.y), interp(previous.z, current.z))
        }
    }

    private val timer: Timer = Minecraft::class.java.let {
        it.getDeclaredField("timer") ?: it.getDeclaredField("field_71428_T")
    }.also { it.isAccessible = true }.get(minecraft) as Timer

    private val renderPartialTicksPaused = Minecraft::class.java.let {
        it.getDeclaredField("renderPartialTicksPaused") ?: it.getDeclaredField("field_193996_ah")
    }.also { it.isAccessible = true }

    private var worldTicks: Int = 0
    private var globalTicks: Int = 0

    @SubscribeEvent
    internal fun clientTickEnd(event: TickEvent.ClientTickEvent) {
        if (event.phase == TickEvent.Phase.END) {
            val mc = Minecraft.getInstance()
            if(!mc.isGamePaused)
                worldTicks += timer.elapsedTicks
            globalTicks += timer.elapsedTicks
        }
    }
}
