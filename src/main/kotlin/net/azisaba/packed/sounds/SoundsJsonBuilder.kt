package net.azisaba.packed.sounds

import kotlinx.serialization.json.Json
import net.azisaba.packed.PathResolver
import net.kyori.adventure.key.Key

internal fun createSoundsJson(pathResolver: PathResolver, json: Json, map: Map<Key, PackSoundEvent>) {
    val soundEventsByNamespace = map.entries.groupBy { it.key.namespace() }
    for ((namespace, soundEvents) in soundEventsByNamespace) {
        val soundEventMap = soundEvents.associate { it.key.value() to it.value }
        val jsonString = json.encodeToString(soundEventMap)
        val jsonFile = pathResolver.filePath(Key.key(namespace, "sounds"), "", ".json")
    }
}
