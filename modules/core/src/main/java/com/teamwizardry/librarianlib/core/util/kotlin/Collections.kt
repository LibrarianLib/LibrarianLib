@file:Suppress("unused")

package com.teamwizardry.librarianlib.core.util.kotlin

import java.util.Collections
import java.util.IdentityHashMap
import java.util.NavigableMap
import java.util.NavigableSet
import java.util.SortedMap
import java.util.SortedSet
import java.util.TreeMap
import java.util.TreeSet
import kotlin.reflect.KProperty1

/**
 * Create a sequence using [next] to generate the next element in the sequence. The sequence ends when [next] returns
 * null.
 *
 * This method was originally designed to easily iterate through an object's ancestors using a `parent` property. For
 * example:
 * ```
 * val someFile = File("/some/file/path")
 * val allParents = someFile.linkedSequence { it.parent }.map { it.name }.toList()
 * // allParents == listOf("path", "file", "some")
 * ```
 */
fun <T: Any> T.linkedSequence(next: (T) -> T?): Sequence<T> {
    return LinkedSequence(this, next)
}

class LinkedSequence<T>(val initialValue: T, val generator: (T) -> T?): Sequence<T> {
    override fun iterator(): Iterator<T> = LinkedIterator(initialValue, generator)

    class LinkedIterator<T>(var nextValue: T?, val generator: (T) -> T?): Iterator<T> {
        override fun hasNext(): Boolean {
            return nextValue != null
        }

        override fun next(): T {
            val current = nextValue ?: throw NoSuchElementException()
            nextValue = generator(current)
            return current
        }
    }
}

// Unmodifiable/synchronized wrappers ==================================================================================

fun <T> Collection<T>.unmodifiableView(): Collection<T> = Collections.unmodifiableCollection(this)
fun <T> Set<T>.unmodifiableView(): Set<T> = Collections.unmodifiableSet(this)
fun <T> SortedSet<T>.unmodifiableView(): SortedSet<T> = Collections.unmodifiableSortedSet(this)
fun <T> NavigableSet<T>.unmodifiableView(): NavigableSet<T> = Collections.unmodifiableNavigableSet(this)
fun <T> List<T>.unmodifiableView(): List<T> = Collections.unmodifiableList(this)
fun <K, V> Map<K, V>.unmodifiableView(): Map<K, V> = Collections.unmodifiableMap(this)
fun <K, V> SortedMap<K, V>.unmodifiableView(): SortedMap<K, V> = Collections.unmodifiableSortedMap(this)
fun <K, V> NavigableMap<K, V>.unmodifiableView(): NavigableMap<K, V> = Collections.unmodifiableNavigableMap(this)

fun <T> Collection<T>.unmodifiableCopy(): Collection<T> = Collections.unmodifiableCollection(this.toList())
fun <T> Set<T>.unmodifiableCopy(): Set<T> = Collections.unmodifiableSet(this.toSet())
fun <T> SortedSet<T>.unmodifiableCopy(): SortedSet<T> = Collections.unmodifiableSortedSet(this.toSortedSet(this.comparator()))
fun <T> NavigableSet<T>.unmodifiableCopy(): NavigableSet<T> = Collections.unmodifiableNavigableSet(TreeSet(this))
fun <T> List<T>.unmodifiableCopy(): List<T> = Collections.unmodifiableList(this.toList())
fun <K, V> Map<K, V>.unmodifiableCopy(): Map<K, V> = Collections.unmodifiableMap(this.toMap())
fun <K, V> SortedMap<K, V>.unmodifiableCopy(): SortedMap<K, V> = Collections.unmodifiableSortedMap(this.toSortedMap(this.comparator()))
fun <K, V> NavigableMap<K, V>.unmodifiableCopy(): NavigableMap<K, V> = Collections.unmodifiableNavigableMap(TreeMap(this))

fun <T> MutableCollection<T>.synchronized(): MutableCollection<T> = Collections.synchronizedCollection(this)
fun <T> MutableSet<T>.synchronized(): MutableSet<T> = Collections.synchronizedSet(this)
fun <T> SortedSet<T>.synchronized(): SortedSet<T> = Collections.synchronizedSortedSet(this)
fun <T> NavigableSet<T>.synchronized(): NavigableSet<T> = Collections.synchronizedNavigableSet(this)
fun <T> MutableList<T>.synchronized(): MutableList<T> = Collections.synchronizedList(this)
fun <K, V> MutableMap<K, V>.synchronized(): MutableMap<K, V> = Collections.synchronizedMap(this)
fun <K, V> SortedMap<K, V>.synchronized(): SortedMap<K, V> = Collections.synchronizedSortedMap(this)
fun <K, V> NavigableMap<K, V>.synchronized(): NavigableMap<K, V> = Collections.synchronizedNavigableMap(this)

// Identity map ========================================================================================================

fun <K, V> identityMapOf(): MutableMap<K, V> = IdentityHashMap()
fun <K, V> identityMapOf(vararg pairs: Pair<K, V>): MutableMap<K, V> {
    return IdentityHashMap<K, V>(mapCapacity(pairs.size)).apply { putAll(pairs) }
}

fun <T> identitySetOf(): MutableSet<T> = Collections.newSetFromMap(IdentityHashMap())
fun <T> identitySetOf(vararg elements: T): MutableSet<T> {
    val map = IdentityHashMap<T, Boolean>(mapCapacity(elements.size))
    return elements.toCollection(Collections.newSetFromMap(map))
}

fun <K, V> Map<K, V>.toIdentityMap(): MutableMap<K, V> = IdentityHashMap(this)
fun <T> Set<T>.toIdentitySet(): MutableSet<T> = identitySetOf<T>().also { it.addAll(this) }

fun <K, V> Map<K, V>.unmodifiableIdentityCopy(): Map<K, V> = Collections.unmodifiableMap(this.toIdentityMap())
fun <T> Set<T>.unmodifiableIdentityCopy(): Set<T> = Collections.unmodifiableSet(this.toIdentitySet())

// Private utils =======================================================================================================

// ripped from the Kotlin runtime:
private fun mapCapacity(expectedSize: Int): Int {
    if (expectedSize < 3) {
        return expectedSize + 1
    }
    if (expectedSize < INT_MAX_POWER_OF_TWO) {
        return expectedSize + expectedSize / 3
    }
    return Int.MAX_VALUE // any large value
}

private const val INT_MAX_POWER_OF_TWO: Int = Int.MAX_VALUE / 2 + 1
