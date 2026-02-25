package net.azisaba.packed.equipment

import kotlinx.serialization.builtins.MapSerializer
import net.azisaba.packed.KeyedJsonPerFileResourceType

typealias PackEquipmentModel = Map<PackEquipmentLayerKey, PackEquipmentLayer>

internal object EquipmentModelResourceType : KeyedJsonPerFileResourceType<PackEquipmentModel>(
    "equipment",
    MapSerializer(PackEquipmentLayerKey.serializer(), PackEquipmentLayer.serializer())
)
