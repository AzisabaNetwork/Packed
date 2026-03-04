package net.azisaba.packed.lang

import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import net.azisaba.packed.KeyedJsonPerFileResourceType
import net.azisaba.packed.util.TranslationSerializer

typealias PackLang = Map<String, Translation>

internal object PackLangResourceType : KeyedJsonPerFileResourceType<PackLang>(
    "packed",
    MapSerializer(String.serializer(), TranslationSerializer)
)
