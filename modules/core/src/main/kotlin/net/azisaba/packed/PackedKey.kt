package net.azisaba.packed

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.KeyPattern
import net.kyori.adventure.key.Namespaced

sealed interface PackedKey<T : Any> : Key {
    companion object {
        fun <T : Any> key(@KeyPattern.Namespace namespace: String, @KeyPattern.Value value: String): PackedKey<T> =
            key(Key.key(namespace, value))

        fun <T : Any> key(namespaced: Namespaced, @KeyPattern.Value value: String): PackedKey<T> =
            key(Key.key(namespaced, value))

        fun <T : Any> key(key: Key): PackedKey<T> = PackedKeyImpl(key)
    }
}

private class PackedKeyImpl<T : Any>(val key: Key) : PackedKey<T> {
    override fun namespace(): String = key.namespace()

    override fun value(): String = key.value()

    override fun asString(): String = key.asString()

    override fun toString(): String = "PackedKey[${asString()}]"
}
