package net.azisaba.packed.sounds

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import net.azisaba.packed.util.KeySerializer
import net.azisaba.packed.util.KeyedPackResource
import net.azisaba.packed.PackResourceType
import net.kyori.adventure.key.Key
import kotlin.io.path.writeText

@Serializable
data class PackSoundEvent(
    val sounds: Collection<PackSound>,
    @Serializable(with = KeySerializer::class)
    val subtitle: Key? = null,
    val replace: Boolean = false,
) {
    internal companion object {
        fun resourceType(): PackResourceType<KeyedPackResource<PackSoundEvent>> =
            PackResourceType { json, pathResolver, resourceSet ->
                val soundEventsByNamespace = resourceSet
                    .associate { it.key to it.value }
                    .entries
                    .groupBy { it.key.namespace() }

                for ((namespace, soundEvents) in soundEventsByNamespace) {
                    val soundEventMap = soundEvents.associate { it.key.value() to it.value }
                    val jsonString = json.encodeToString(
                        MapSerializer(String.serializer(), serializer()),
                        soundEventMap
                    )
                    val jsonFile = pathResolver.filePath(Key.key(namespace, "sounds"), extension = ".json")
                    jsonFile.writeText(jsonString)
                }
            }
    }
}
