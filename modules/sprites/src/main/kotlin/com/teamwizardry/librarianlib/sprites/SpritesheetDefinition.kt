package com.teamwizardry.librarianlib.sprites

import com.teamwizardry.librarianlib.math.Vec2i
import net.minecraft.util.ResourceLocation
import java.awt.Color
import java.awt.image.BufferedImage

class SpritesheetDefinition internal constructor(val location: ResourceLocation) {
    /**
     * The size of the texture specified in the mcmeta file, used for computing 0-1 UV coordinates
     */
    var uvSize: Vec2i = Vec2i.ZERO
        internal set
    var sprites: List<SpriteDefinition> = emptyList()
        internal set
    var colors: List<ColorDefinition> = emptyList()
        internal set

    lateinit var image: BufferedImage
        internal set
    lateinit var missingSprite: SpriteDefinition
        internal set
}

class SpriteDefinition internal constructor(val name: String) {
    var missing: Boolean = false
        internal set

    lateinit var sheet: SpritesheetDefinition
        internal set
    var uv: Vec2i = Vec2i.ZERO
        internal set
    var size: Vec2i = Vec2i.ZERO
        internal set

    var minUCap: Int = 0
        internal set
    var minVCap: Int = 0
        internal set
    var maxUCap: Int = 0
        internal set
    var maxVCap: Int = 0
        internal set

    var minUPin: Boolean = true
        internal set
    var minVPin: Boolean = true
        internal set
    var maxUPin: Boolean = true
        internal set
    var maxVPin: Boolean = true
        internal set

    var frameUVs: List<Vec2i> = emptyList()
        internal set
    var frameImages: List<BufferedImage> = emptyList()
        internal set

    /**
     * Transforms an integer U coordinate to the 0-1 range appropriate for rendering
     */
    fun texU(u: Int): Float = u / sheet.uvSize.xf

    /**
     * Transforms an integer V coordinate to the 0-1 range appropriate for rendering
     */
    fun texV(v: Int): Float = v / sheet.uvSize.yf

    lateinit var image: BufferedImage
        internal set
}

class ColorDefinition(val name: String) {
    lateinit var sheet: SpritesheetDefinition
        internal set

    var uv: Vec2i = Vec2i.ZERO
        internal set
    var color: Color = Color.MAGENTA
        internal set
}
