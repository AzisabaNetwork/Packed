package net.azisaba.packed

import net.azisaba.packed.equipment.PackEquipmentModel
import net.azisaba.packed.font.PackFont
import net.azisaba.packed.items.PackItemModel
import net.azisaba.packed.lang.PackLanguage
import net.azisaba.packed.models.PackModel
import net.azisaba.packed.sounds.PackSoundEvent
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.KeyPattern
import net.kyori.adventure.key.Namespaced
import java.util.*

sealed interface PackedKey<T : Any> : Key {
    companion object {
        fun <T : Any> key(@KeyPattern.Namespace namespace: String, @KeyPattern.Value value: String): PackedKey<T> =
            key(Key.key(namespace, value))

        fun <T : Any> key(namespaced: Namespaced, @KeyPattern.Value value: String): PackedKey<T> =
            key(Key.key(namespaced, value))

        fun <T : Any> key(key: Key): PackedKey<T> = PackedKeyImpl(key)

        fun equipmentModel(@KeyPattern.Namespace namespace: String, @KeyPattern.Value value: String): PackedKey<PackEquipmentModel> =
            key(Key.key(namespace, value))

        fun equipmentModel(namespaced: Namespaced, @KeyPattern.Value value: String): PackedKey<PackEquipmentModel> =
            key(Key.key(namespaced, value))

        fun equipmentModel(key: Key): PackedKey<PackEquipmentModel> = key(key)

        fun font(@KeyPattern.Namespace namespace: String, @KeyPattern.Value value: String): PackedKey<PackFont> =
            key(Key.key(namespace, value))

        fun font(namespaced: Namespaced, @KeyPattern.Value value: String): PackedKey<PackFont> =
            key(Key.key(namespaced, value))

        fun font(key: Key): PackedKey<PackFont> = key(key)

        fun itemModel(@KeyPattern.Namespace namespace: String, @KeyPattern.Value value: String): PackedKey<PackItemModel> =
            key(Key.key(namespace, value))

        fun itemModel(namespaced: Namespaced, @KeyPattern.Value value: String): PackedKey<PackItemModel> =
            key(Key.key(namespaced, value))

        fun itemModel(key: Key): PackedKey<PackItemModel> = key(key)

        fun lang(@KeyPattern.Namespace namespace: String, @KeyPattern.Value value: String): PackedKey<PackLanguage> =
            key(Key.key(namespace, value))

        fun lang(namespaced: Namespaced, @KeyPattern.Value value: String): PackedKey<PackLanguage> =
            key(Key.key(namespaced, value))

        fun lang(@KeyPattern.Namespace namespace: String, locale: Locale): PackedKey<PackLanguage> =
            key(Key.key(namespace, locale.toString().lowercase()))

        fun lang(namespaced: Namespaced, locale: Locale): PackedKey<PackLanguage> =
            key(Key.key(namespaced, locale.toString().lowercase()))

        fun lang(key: Key): PackedKey<PackLanguage> = key(key)

        fun model(@KeyPattern.Namespace namespace: String, @KeyPattern.Value value: String): PackedKey<PackModel> =
            key(Key.key(namespace, value))

        fun model(namespaced: Namespaced, @KeyPattern.Value value: String): PackedKey<PackModel> =
            key(Key.key(namespaced, value))

        fun model(key: Key): PackedKey<PackModel> = key(key)

        fun soundEvent(@KeyPattern.Namespace namespace: String, @KeyPattern.Value value: String): PackedKey<PackSoundEvent> =
            key(Key.key(namespace, value))

        fun soundEvent(namespaced: Namespaced, @KeyPattern.Value value: String): PackedKey<PackSoundEvent> =
            key(Key.key(namespaced, value))

        fun soundEvent(key: Key): PackedKey<PackSoundEvent> = key(key)
    }
}

private class PackedKeyImpl<T : Any>(val key: Key) : PackedKey<T> {
    override fun namespace(): String = key.namespace()

    override fun value(): String = key.value()

    override fun asString(): String = key.asString()

    override fun toString(): String = "PackedKey[${asString()}]"
}
