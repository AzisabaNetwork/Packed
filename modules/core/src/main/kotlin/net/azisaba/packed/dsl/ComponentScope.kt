package net.azisaba.packed.dsl

import net.azisaba.packed.MutablePackedEntries
import net.azisaba.packed.PackedComponent
import net.azisaba.packed.PackedEntries
import net.azisaba.packed.PackedKey

@PackedDsl
class ComponentScope<T : Any> internal constructor(private val factory: (PackedEntries<T>) -> PackedComponent<T>) {
    private val mutableEntries: MutablePackedEntries<T> = mutableMapOf()

    infix fun PackedKey<T>.to(value: T) {
        mutableEntries[this] = value
    }

    internal fun build(): PackedComponent<T> = factory(mutableEntries.toMap())
}
