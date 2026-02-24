package net.azisaba.packed.sounds

import kotlinx.serialization.json.Json
import net.azisaba.packed.PackPathResolver
import net.azisaba.packed.Packed
import net.kyori.adventure.key.Key
import kotlin.io.path.writeText

internal fun createSoundsJson(
    json: Json,
    pathResolver: PackPathResolver,
    resourceSet: Set<Packed.IdentifiedResource<PackSoundEvent>>
) {
    val soundEventsByNamespace = resourceSet
        .associate { it.key to it.value }
        .entries
        .groupBy { it.key.namespace() }

    for ((namespace, soundEvents) in soundEventsByNamespace) {
        val soundEventMap = soundEvents.associate { it.key.value() to it.value }
        val jsonString = json.encodeToString(soundEventMap)
        val jsonFile = pathResolver.filePath(Key.key(namespace, "sounds"), "", ".json")
        jsonFile.writeText(jsonString)
    }
}
