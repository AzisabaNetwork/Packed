package net.azisaba.packed

import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import net.azisaba.packed.equipment.PackEquipmentLayer
import net.azisaba.packed.equipment.PackEquipmentLayerKey
import net.azisaba.packed.equipment.PackEquipmentModel
import net.azisaba.packed.font.PackFont
import net.azisaba.packed.items.PackItemModel
import net.azisaba.packed.lang.PackLanguage
import net.azisaba.packed.lang.Translation
import net.azisaba.packed.models.PackModel
import net.azisaba.packed.sounds.PackSoundEvent

fun PackedComponent.Companion.equipmentModel(entries: PackedEntries<PackEquipmentModel>): PackedComponent<PackEquipmentModel> =
    PackedComponent.JsonFilePerEntry(
        entries,
        MapSerializer(PackEquipmentLayerKey.serializer(), PackEquipmentLayer.serializer()),
        "equipment",
    )

fun PackedComponent.Companion.font(entries: PackedEntries<PackFont>): PackedComponent<PackFont> =
    PackedComponent.JsonFilePerEntry(
        entries,
        PackFont.serializer(),
        "font",
    )

fun PackedComponent.Companion.itemModel(entries: PackedEntries<PackItemModel>): PackedComponent<PackItemModel> =
    PackedComponent.JsonFilePerEntry(
        entries,
        PackItemModel.serializer(),
        "items",
    )

fun PackedComponent.Companion.language(entries: PackedEntries<PackLanguage>): PackedComponent<PackLanguage> =
    PackedComponent.JsonFilePerEntry(
        entries,
        MapSerializer(String.serializer(), Translation.serializer()),
        "lang",
    )

fun PackedComponent.Companion.model(entries: PackedEntries<PackModel>): PackedComponent<PackModel> =
    PackedComponent.JsonFilePerEntry(
        entries,
        PackModel.serializer(),
        "models",
    )

fun PackedComponent.Companion.soundEvent(entries: PackedEntries<PackSoundEvent>): PackedComponent<PackSoundEvent> =
    PackedComponent.JsonFilePerNamespace(
        entries,
        PackSoundEvent.serializer(),
        "sounds.json",
    )
