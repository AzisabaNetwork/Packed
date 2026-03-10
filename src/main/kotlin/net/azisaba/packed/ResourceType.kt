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

data class ResourceType<T : Any>(val writer: ResourceWriter<T>) {
    fun write(context: ResourceWriteContext) {
        writer.write(context, this)
    }

    companion object Builtins {
        val EQUIPMENT_MODEL: ResourceType<PackEquipmentModel> = ResourceType(
            ResourceWriter.jsonFilePerResource(
                "equipment",
                MapSerializer(PackEquipmentLayerKey.serializer(), PackEquipmentLayer.serializer())
            )
        )
        val FONT: ResourceType<PackFont> = ResourceType(
            ResourceWriter.jsonFilePerResource("font", PackFont.serializer())
        )
        val ITEM_MODEL: ResourceType<PackItemModel> = ResourceType(
            ResourceWriter.jsonFilePerResource("items", PackItemModel.serializer())
        )
        val LANG: ResourceType<PackLanguage> = ResourceType(
            ResourceWriter.jsonFilePerResource(
                "lang",
                MapSerializer(String.serializer(), Translation.serializer())
            )
        )
        val MODEL: ResourceType<PackModel> = ResourceType(
            ResourceWriter.jsonFilePerResource("models", PackModel.serializer())
        )
        val SOUND_EVENT: ResourceType<PackSoundEvent> = ResourceType(
            ResourceWriter.jsonFilePerNamespace("sounds", PackSoundEvent.serializer())
        )
    }
}
