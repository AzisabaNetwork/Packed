package net.azisaba.packed.dsl

import net.azisaba.packed.*
import net.azisaba.packed.equipment.PackEquipmentModel
import net.azisaba.packed.font.PackFont
import net.azisaba.packed.items.PackItemModel
import net.azisaba.packed.models.PackModel
import net.azisaba.packed.sounds.PackSoundEvent
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import kotlin.math.max

fun packed(block: PackedBuilder.() -> Unit): Packed = PackedBuilder().apply(block).build()

@DslMarker
annotation class PackedDsl

@PackedDsl
class PackedBuilder internal constructor() {
    private val metadataScope: MetadataScope = MetadataScope()
    private val namespaceScopes: MutableList<NamespaceScope> = mutableListOf()

    fun metadata(block: MetadataScope.() -> Unit) {
        metadataScope.apply(block)
    }

    fun namespace(namespace: String, block: NamespaceScope.() -> Unit) {
        namespaceScopes += NamespaceScope(namespace).apply(block)
    }

    fun build(): Packed {
        val mergedResourceMap = mutableMapOf<Packed.Type<*>, MutableSet<*>>()

        for (namespaceScope in namespaceScopes) {
            for ((resourceType, resourceSet) in namespaceScope.toMap()) {
                val bucket = mergedResourceMap.getOrPut(resourceType) { mutableSetOf<Any>() }
                @Suppress("UNCHECKED_CAST")
                (bucket as MutableSet<Any>).addAll(resourceSet as Set<Any>)
            }
        }

        return Packed(
            metadataScope.toMetadata(),
            mergedResourceMap.mapValues { it.value.toSet() }
        )
    }
}

@PackedDsl
class MetadataScope {
    private var packFormat: PackFormat? = null

    private var minFormat: PackFormat? = null
    private var maxFormat: PackFormat? = null

    private val supportedFormats: MutableSet<PackFormatRange> = mutableSetOf()

    private var description: Component? = null

    fun packFormat(major: Int, minor: Int) {
        packFormat = PackFormat(major, minor)
    }

    fun minFormat(major: Int, minor: Int = 0) {
        minFormat = PackFormat(major, minor)
    }

    fun maxFormat(major: Int, minor: Int = 0) {
        maxFormat = PackFormat(major, minor)
    }

    fun supportedFormat(minInclusive: Int, maxInclusive: Int) {
        supportedFormats += PackFormatRange(minInclusive, maxInclusive)
    }

    fun describe(description: Component) {
        this.description = description
    }

    internal fun toMetadata(): PackMetadata = PackMetadata(
        PackMetadata.Info(
            packFormat,
            minFormat,
            maxFormat,
            supportedFormats.toSet(),
            description,
        )
    )
}

@PackedDsl
class NamespaceScope(private val namespace: String) {
    private val resourceMap: MutableMap<Packed.Type<*>, Set<*>> = mutableMapOf()

    fun equipment(block: ResourceScope<PackEquipmentModel>.() -> Unit) {
        val scope = ResourceScope<PackEquipmentModel>(namespace).apply(block)
        resourceMap[Packed.EQUIPMENT_MODEL] = scope.toSet()
    }

    fun font(block: ResourceScope<PackFont>.() -> Unit) {
        val scope = ResourceScope<PackFont>(namespace).apply(block)
        resourceMap[Packed.FONT] = scope.toSet()
    }

    fun items(block: ResourceScope<PackItemModel>.() -> Unit) {
        val scope = ResourceScope<PackItemModel>(namespace).apply(block)
        resourceMap[Packed.ITEM_MODEL] = scope.toSet()
    }

    fun models(block: ResourceScope<PackModel>.() -> Unit) {
        val scope = ResourceScope<PackModel>(namespace).apply(block)
        resourceMap[Packed.MODEL] = scope.toSet()
    }

    fun sounds(block: ResourceScope<PackSoundEvent>.() -> Unit) {
        val scope = ResourceScope<PackSoundEvent>(namespace).apply(block)
        resourceMap[Packed.SOUND_EVENT] = scope.toSet()
    }

    internal fun toMap(): Map<Packed.Type<*>, Set<*>> = resourceMap.toMap()
}

@PackedDsl
class ResourceScope<R : Any>(private val namespace: String) {
    private val resourceSet: MutableSet<IdentifiedResource<R>> = mutableSetOf()

    operator fun String.invoke(value: R) {
        resourceSet += IdentifiedResource(Key.key(namespace, this), value)
    }

    internal fun toSet(): Set<IdentifiedResource<R>> = resourceSet.toSet()
}
