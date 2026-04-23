package net.azisaba.packed.equipment

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.annotations.ApiStatus

fun PackEquipmentModel(builderAction: PackEquipmentModelBuilder.() -> Unit): PackEquipmentModel =
    PackEquipmentModelBuilder().apply(builderAction).build()

@Serializable(with = PackEquipmentModelSerializer::class)
data class PackEquipmentModel(internal val layers: Map<PackEquipmentLayerKey, PackEquipmentLayer>) {
    operator fun get(key: PackEquipmentLayerKey): PackEquipmentLayer? = layers[key]

    operator fun contains(key: PackEquipmentLayerKey): Boolean = layers.containsKey(key)
}

class PackEquipmentModelBuilder internal constructor() {
    private val layers: MutableMap<PackEquipmentLayerKey, PackEquipmentLayer> = mutableMapOf()

    infix fun PackEquipmentLayerKey.layer(layer: PackEquipmentLayer) {
        layers[this] = layer
    }

    internal fun build(): PackEquipmentModel = PackEquipmentModel(layers)
}

@ApiStatus.Internal
object PackEquipmentModelSerializer : KSerializer<PackEquipmentModel> {
    private val delegate: KSerializer<Map<PackEquipmentLayerKey, PackEquipmentLayer>> =
        MapSerializer(PackEquipmentLayerKey.serializer(), PackEquipmentLayer.serializer())

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: PackEquipmentModel) {
        delegate.serialize(encoder, value.layers)
    }

    override fun deserialize(decoder: Decoder): PackEquipmentModel {
        return PackEquipmentModel(delegate.deserialize(decoder))
    }
}
