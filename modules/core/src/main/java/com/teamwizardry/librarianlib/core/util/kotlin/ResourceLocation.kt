@file:JvmName("RLPaths")
package com.teamwizardry.librarianlib.core.util.kotlin

import net.minecraft.util.ResourceLocation
import kotlin.math.min

/**
 * Appends a relative path to this location. `..` is supported. If the passed string is an absolute path (it starts with
 * `/`), it will be resolved relative to the root path.
 */
fun ResourceLocation.resolve(relative: String): ResourceLocation {
    if(relative.startsWith("/"))
        return ResourceLocation(namespace, relative.substring(1))
    return ResourceLocation(namespace, "$path/$relative").normalize()
}

/**
 * Adds a relative path to this location's parent. `..` is supported. If the passed string is an absolute path (it
 * starts with `/`), it will be resolved relative to the root path.
 */
fun ResourceLocation.resolveSibling(relative: String): ResourceLocation {
    if(relative.startsWith("/"))
        return ResourceLocation(namespace, relative.substring(1))
    return ResourceLocation(namespace, "$path/../$relative").normalize()
}

/**
 * Gets the path of this location relative to the [base] location, such that passing the returned location to
 * [base.resolve()][ResourceLocation.resolve] will return this location.
 *
 * @throws IllegalArgumentException if this and [base] are in different namespaces
 */
fun ResourceLocation.relativeTo(base: ResourceLocation): String {
    if(this.namespace != base.namespace)
        throw IllegalArgumentException("$this and $base are in different namespaces")

    val thisComponents = this.pathComponents.normalize()
    val baseComponents = base.pathComponents.normalize()
    var commonBase = 0

    for(i in 0 until min(thisComponents.size, baseComponents.size)) {
        if(thisComponents[i] != baseComponents[i])
            break
        commonBase++
    }

    val relativePath = (0 until baseComponents.size - commonBase).map { ".." } + thisComponents.subList(commonBase, thisComponents.size)
    return relativePath.joinToString("/")
}

/**
 * Normalizes this location's path by resolving any parent path components (`..`), and collapsing any empty path
 * components (`//` or `.`).
 */
fun ResourceLocation.normalize(): ResourceLocation {
    return ResourceLocation(namespace, (if(path.startsWith("/")) "/" else "") + pathComponents.normalize().joinToString("/"))
}

private fun List<String>.normalize(): List<String> {
    val normalized = mutableListOf<String>()
    for(component in this) {
        when(component) {
            ".", "" -> {}
            ".." -> if(normalized.isNotEmpty()) normalized.removeAt(normalized.lastIndex)
            else -> normalized.add(component)
        }
    }
    return normalized
}

val ResourceLocation.pathComponents: List<String>
    get() = path.splitToSequence('/').toMutableList().also {
        if(it.isEmpty()) return@also
        if(it.first().isEmpty())
            it.removeAt(0)
        if(it.last().isEmpty())
            it.removeAt(it.lastIndex)
    }

/**
 * This location's parent, or `null` if it has no parent (i.e. it is the root path)
 */
val ResourceLocation.parent: ResourceLocation?
    get() = ResourceLocation(namespace, path.substringBeforeLast('/'))

/**
 * Returns the final component of this location's path
 */
val ResourceLocation.filename: String
    get() = path.substringAfterLast('/')

/**
 * Returns this file's extension (not including the dot), or an empty string if this file doesn't have one.
 */
val ResourceLocation.extension: String
    get() = filename.substringAfterLast('.', "")

/**
 * Returns this file's name without an extension.
 */
val ResourceLocation.filenameWithoutExtension: String
    get() = filename.substringBeforeLast('.', "")
