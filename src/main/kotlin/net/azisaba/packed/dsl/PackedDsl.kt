package net.azisaba.packed.dsl

import net.azisaba.packed.IdentifiedResource
import net.azisaba.packed.Packed
import net.azisaba.packed.equipment.PackEquipmentModel
import net.azisaba.packed.font.PackFont
import net.azisaba.packed.models.PackModel
import net.azisaba.packed.sounds.PackSoundEvent
import net.kyori.adventure.key.Key

fun packed(block: PackedBuilder.() -> Unit): Packed = PackedBuilder().apply(block).build()

@DslMarker
annotation class PackedDsl

@PackedDsl
class PackedBuilder internal constructor() {
    private val namespaceScopes: MutableList<NamespaceScope> = mutableListOf()

    fun namespace(namespace: String, block: NamespaceScope.() -> Unit) {
        namespaceScopes += NamespaceScope(namespace).apply(block)
    }

    fun build(): Packed {
        val mergedResourceMap = mutableMapOf<Packed.ResourceType<*>, MutableSet<*>>()

        for (namespaceScope in namespaceScopes) {
            for ((resourceType, resourceSet) in namespaceScope.toMap()) {
                val bucket = mergedResourceMap.getOrPut(resourceType) { mutableSetOf<Any>() }
                @Suppress("UNCHECKED_CAST")
                (bucket as MutableSet<Any>).addAll(resourceSet as Set<Any>)
            }
        }

        return Packed(mergedResourceMap.mapValues { it.value.toSet() })
    }
}

@PackedDsl
class NamespaceScope(private val namespace: String) {
    private val resourceMap: MutableMap<Packed.ResourceType<*>, Set<*>> = mutableMapOf()

    fun equipment(block: ResourceScope<PackEquipmentModel>.() -> Unit) {
        val scope = ResourceScope<PackEquipmentModel>(namespace).apply(block)
        resourceMap[Packed.EQUIPMENT_MODEL] = scope.toSet()
    }

    fun font(block: ResourceScope<PackFont>.() -> Unit) {
        val scope = ResourceScope<PackFont>(namespace).apply(block)
        resourceMap[Packed.FONT] = scope.toSet()
    }

    fun models(block: ResourceScope<PackModel>.() -> Unit) {
        val scope = ResourceScope<PackModel>(namespace).apply(block)
        resourceMap[Packed.MODEL] = scope.toSet()
    }

    fun sounds(block: ResourceScope<PackSoundEvent>.() -> Unit) {
        val scope = ResourceScope<PackSoundEvent>(namespace).apply(block)
        resourceMap[Packed.SOUND_EVENT] = scope.toSet()
    }

    fun toMap(): Map<Packed.ResourceType<*>, Set<*>> = resourceMap.toMap()
}

@PackedDsl
class ResourceScope<R : Any>(private val namespace: String) {
    private val resourceSet: MutableSet<IdentifiedResource<R>> = mutableSetOf()

    operator fun String.invoke(value: R) {
        resourceSet += IdentifiedResource(Key.key(namespace, this), value)
    }

    internal fun toSet(): Set<IdentifiedResource<R>> = resourceSet.toSet()
}
