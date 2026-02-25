package net.azisaba.packed.util

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed

data class KeyedPackResource<T>(val key: Key, val value: T) : Keyed {
    override fun key(): Key = key
}
