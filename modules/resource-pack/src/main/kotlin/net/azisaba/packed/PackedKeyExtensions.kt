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

fun PackedKey.Companion.equipmentModel(@KeyPattern.Namespace namespace: String, @KeyPattern.Value value: String): PackedKey<PackEquipmentModel> =
    PackedKey.key(Key.key(namespace, value))

fun PackedKey.Companion.equipmentModel(namespaced: Namespaced, @KeyPattern.Value value: String): PackedKey<PackEquipmentModel> =
    PackedKey.key(Key.key(namespaced, value))

fun PackedKey.Companion.equipmentModel(key: Key): PackedKey<PackEquipmentModel> = PackedKey.key(key)

fun PackedKey.Companion.font(@KeyPattern.Namespace namespace: String, @KeyPattern.Value value: String): PackedKey<PackFont> =
    PackedKey.key(Key.key(namespace, value))

fun PackedKey.Companion.font(namespaced: Namespaced, @KeyPattern.Value value: String): PackedKey<PackFont> =
    PackedKey.key(Key.key(namespaced, value))

fun PackedKey.Companion.font(key: Key): PackedKey<PackFont> = PackedKey.key(key)

fun PackedKey.Companion.itemModel(@KeyPattern.Namespace namespace: String, @KeyPattern.Value value: String): PackedKey<PackItemModel> =
    PackedKey.key(Key.key(namespace, value))

fun PackedKey.Companion.itemModel(namespaced: Namespaced, @KeyPattern.Value value: String): PackedKey<PackItemModel> =
    PackedKey.key(Key.key(namespaced, value))

fun PackedKey.Companion.itemModel(key: Key): PackedKey<PackItemModel> = PackedKey.key(key)

fun PackedKey.Companion.lang(@KeyPattern.Namespace namespace: String, @KeyPattern.Value value: String): PackedKey<PackLanguage> =
    PackedKey.key(Key.key(namespace, value))

fun PackedKey.Companion.lang(namespaced: Namespaced, @KeyPattern.Value value: String): PackedKey<PackLanguage> =
    PackedKey.key(Key.key(namespaced, value))

fun PackedKey.Companion.lang(@KeyPattern.Namespace namespace: String, locale: Locale): PackedKey<PackLanguage> =
    PackedKey.key(Key.key(namespace, locale.toString().lowercase()))

fun PackedKey.Companion.lang(namespaced: Namespaced, locale: Locale): PackedKey<PackLanguage> =
    PackedKey.key(Key.key(namespaced, locale.toString().lowercase()))

fun PackedKey.Companion.lang(key: Key): PackedKey<PackLanguage> = PackedKey.key(key)

fun PackedKey.Companion.model(@KeyPattern.Namespace namespace: String, @KeyPattern.Value value: String): PackedKey<PackModel> =
    PackedKey.key(Key.key(namespace, value))

fun PackedKey.Companion.model(namespaced: Namespaced, @KeyPattern.Value value: String): PackedKey<PackModel> =
    PackedKey.key(Key.key(namespaced, value))

fun PackedKey.Companion.model(key: Key): PackedKey<PackModel> = PackedKey.key(key)

fun PackedKey.Companion.soundEvent(@KeyPattern.Namespace namespace: String, @KeyPattern.Value value: String): PackedKey<PackSoundEvent> =
    PackedKey.key(Key.key(namespace, value))

fun PackedKey.Companion.soundEvent(namespaced: Namespaced, @KeyPattern.Value value: String): PackedKey<PackSoundEvent> =
    PackedKey.key(Key.key(namespaced, value))

fun PackedKey.Companion.soundEvent(key: Key): PackedKey<PackSoundEvent> = PackedKey.key(key)
