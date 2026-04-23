package net.azisaba.packed.lang

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.key.Key
import net.kyori.adventure.translation.Translatable
import org.jetbrains.annotations.ApiStatus

@Serializable(with = TranslationKeySerializer::class)
sealed interface TranslationKey : Translatable {
    companion object {
        fun key(key: String): TranslationKey = TranslationKeyImpl(key)

        fun block(key: Key): TranslationKey = key("block.${key.namespace()}.${key.value()}")

        fun item(key: Key): TranslationKey = key("item.${key.namespace()}.${key.value()}")

        fun entity(key: Key): TranslationKey = key("entity.${key.namespace()}.${key.value()}")

        fun effect(key: Key): TranslationKey = key("effect.${key.namespace()}.${key.value()}")

        fun biome(key: Key): TranslationKey = key("biome.${key.namespace()}.${key.value()}")
    }
}

private data class TranslationKeyImpl(val key: String) : TranslationKey {
    override fun translationKey(): String = key
}

@ApiStatus.Internal
object TranslationKeySerializer : KSerializer<TranslationKey> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("TranslationKey", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: TranslationKey) {
        encoder.encodeString(value.translationKey())
    }

    override fun deserialize(decoder: Decoder): TranslationKey {
        return TranslationKeyImpl(decoder.decodeString())
    }
}
