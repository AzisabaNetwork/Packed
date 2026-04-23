package net.azisaba.packed.lang

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.key.Key
import org.jetbrains.annotations.ApiStatus

fun PackLanguage(builderAction: PackLanguageBuilder.() -> Unit): PackLanguage =
    PackLanguageBuilder().apply(builderAction).build()

@Serializable(with = PackLanguageSerializer::class)
data class PackLanguage(internal val translations: Map<TranslationKey, Translation>) {
    operator fun get(key: TranslationKey): Translation? = translations[key]

    operator fun contains(key: TranslationKey): Boolean = translations.containsKey(key)
}

class PackLanguageBuilder internal constructor() {
    private val translations: MutableMap<TranslationKey, Translation> = mutableMapOf()

    infix fun TranslationKey.translate(translation: Translation) {
        translations[this] = translation
    }
    
    fun key(key: String): TranslationKey = TranslationKey.key(key)

    fun blockKey(key: Key): TranslationKey = TranslationKey.key("block.${key.namespace()}.${key.value()}")

    fun itemKey(key: Key): TranslationKey = TranslationKey.key("item.${key.namespace()}.${key.value()}")

    fun entityKey(key: Key): TranslationKey = TranslationKey.key("entity.${key.namespace()}.${key.value()}")

    fun effectKey(key: Key): TranslationKey = TranslationKey.key("effect.${key.namespace()}.${key.value()}")

    fun biomeKey(key: Key): TranslationKey = TranslationKey.key("biome.${key.namespace()}.${key.value()}")
    
    fun literal(literal: String): Translation = Translation.literal(literal)
    
    fun placeholder(): Translation = Translation.placeholder()
    
    fun placeholder(index: Int): Translation = Translation.placeholder(index)

    fun black(): Translation = Translation.literal("§0")

    fun darkBlue(): Translation = Translation.literal("§1")

    fun darkGreen(): Translation = Translation.literal("§2")

    fun darkAqua(): Translation = Translation.literal("§3")

    fun darkRed(): Translation = Translation.literal("§4")

    fun darkPurple(): Translation = Translation.literal("§5")

    fun gold(): Translation = Translation.literal("§6")

    fun gray(): Translation = Translation.literal("§7")

    fun darkGray(): Translation = Translation.literal("§8")

    fun blue(): Translation = Translation.literal("§9")

    fun green(): Translation = Translation.literal("§a")

    fun aqua(): Translation = Translation.literal("§b")

    fun red(): Translation = Translation.literal("§c")

    fun lightPurple(): Translation = Translation.literal("§d")

    fun yellow(): Translation = Translation.literal("§e")

    fun white(): Translation = Translation.literal("§f")

    fun obfuscated(): Translation = Translation.literal("§k")

    fun bold(): Translation = Translation.literal("§l")

    fun strikethrough(): Translation = Translation.literal("§m")

    fun underline(): Translation = Translation.literal("§n")

    fun italic(): Translation = Translation.literal("§o")

    fun resetStyle(): Translation = Translation.literal("§r")

    internal fun build(): PackLanguage = PackLanguage(translations)
}

@ApiStatus.Internal
object PackLanguageSerializer : KSerializer<PackLanguage> {
    private val delegate: KSerializer<Map<TranslationKey, Translation>> =
        MapSerializer(TranslationKey.serializer(), Translation.serializer())

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: PackLanguage) {
        delegate.serialize(encoder, value.translations)
    }

    override fun deserialize(decoder: Decoder): PackLanguage {
        return PackLanguage(delegate.deserialize(decoder))
    }
}
