package net.azisaba.packed.util.dsl

import net.azisaba.packed.*
import net.azisaba.packed.equipment.PackEquipmentModel
import net.azisaba.packed.font.PackFont
import net.azisaba.packed.items.PackItemModel
import net.azisaba.packed.models.PackModel
import net.azisaba.packed.sounds.PackSoundEvent
import net.azisaba.packed.util.KeyedPackResource
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Namespaced
import net.kyori.adventure.text.Component
import org.bukkit.plugin.Plugin

fun packed(block: PackBuilder.() -> Unit): Pack = PackBuilder().apply(block).build()

@DslMarker
annotation class PackDsl

@PackDsl
class PackBuilder internal constructor() {
    private val metadataScope: MetadataScope = MetadataScope()
    private val pluginsScope: PluginsScope = PluginsScope()
    private val namespaceScopes: MutableList<NamespaceScope> = mutableListOf()

    fun metadata(block: MetadataScope.() -> Unit) {
        metadataScope.apply(block)
    }

    fun plugins(block: PluginsScope.() -> Unit) {
        pluginsScope.apply(block)
    }

    fun namespace(namespace: Namespaced, block: NamespaceScope.() -> Unit) =
        namespace(namespace.namespace(), block)

    fun namespace(namespace: String, block: NamespaceScope.() -> Unit) {
        namespaceScopes += NamespaceScope(namespace).apply(block)
    }

    fun build(): Pack {
        val merged = mutableMapOf<PackResourceType<*>, MutableSet<*>>()

        for (namespaceScope in namespaceScopes) {
            for ((type, resourceSet) in namespaceScope.toMap()) {
                val bucket = merged.getOrPut(type) { mutableSetOf<Any>() }
                @Suppress("UNCHECKED_CAST")
                (bucket as MutableSet<Any>).addAll(resourceSet as Set<Any>)
            }
        }

        if (pluginsScope.isNotEmpty()) {
            @Suppress("UNCHECKED_CAST")
            val bucket = merged.getOrPut(PackResourceTypes.PLUGIN_RESOURCE) {
                mutableSetOf<Plugin>()
            } as MutableSet<Plugin>
            bucket.addAll(pluginsScope.toPluginSet())
        }

        return Pack(
            metadataScope.toMetadata(),
            merged.mapValues { it.value.toSet() }
        )
    }
}

@PackDsl
class MetadataScope internal constructor() {
    private var packFormat: Int? = null

    private var minFormat: PackFormat? = null
    private var maxFormat: PackFormat? = null

    private var description: Component? = null

    fun packFormat(major: Int) {
        packFormat = major
    }

    fun minFormat(major: Int, minor: Int = 0) {
        minFormat = PackFormat(major, minor)
    }

    fun maxFormat(major: Int, minor: Int = 0) {
        maxFormat = PackFormat(major, minor)
    }

    fun describe(description: Component) {
        this.description = description
    }

    internal fun toMetadata(): PackMetadata = PackMetadata(
        PackMetadata.Info(
            checkNotNull(minFormat) { "minFormat must be specified" },
            checkNotNull(maxFormat) { "maxFormat must be specified" },
            packFormat,
            checkNotNull(description) { "description must be specified" },
        )
    )
}

@PackDsl
class PluginsScope internal constructor() {
    private val plugins: MutableSet<Plugin> = mutableSetOf()

    operator fun Plugin.unaryPlus() {
        plugins += this
    }

    internal fun toPluginSet(): Set<Plugin> = plugins.toSet()

    internal fun isNotEmpty(): Boolean = plugins.isNotEmpty()
}

@PackDsl
class NamespaceScope internal constructor(private val namespace: String) {
    private val resourceMap: MutableMap<PackResourceType<*>, MutableSet<Any>> = mutableMapOf()

    fun equipment(block: ResourceScope<PackEquipmentModel>.() -> Unit) =
        register(PackResourceTypes.EQUIPMENT_MODEL, block)

    fun font(block: ResourceScope<PackFont>.() -> Unit) = register(PackResourceTypes.FONT, block)

    fun items(block: ResourceScope<PackItemModel>.() -> Unit) = register(PackResourceTypes.ITEM_MODEL, block)

    fun models(block: ResourceScope<PackModel>.() -> Unit) = register(PackResourceTypes.MODEL, block)

    fun sounds(block: ResourceScope<PackSoundEvent>.() -> Unit) = register(PackResourceTypes.SOUND_EVENT, block)

    internal fun toMap(): Map<PackResourceType<*>, Set<*>> = resourceMap.toMap()

    private fun <R : Any> register(type: PackResourceType<KeyedPackResource<R>>, block: ResourceScope<R>.() -> Unit) {
        val scope = ResourceScope<R>(namespace).apply(block)
        val bucket = resourceMap.getOrPut(type) { mutableSetOf() }
        bucket.addAll(scope.toSet())
    }
}

@PackDsl
class ResourceScope<R : Any>(private val scopeNamespace: String) {
    private val resources: MutableMap<Key, R> = mutableMapOf()

    operator fun String.invoke(value: R) {
        put(Key.key(scopeNamespace, this), value)
    }

    operator fun Key.invoke(value: R) {
        require(namespace() == scopeNamespace) {
            "Key namespace mismatch: ${namespace()} (expected: $scopeNamespace)"
        }
        put(this, value)
    }

    private fun put(key: Key, value: R) {
        check(resources.putIfAbsent(key, value) == null) { "Duplicate resource key: $key" }
    }

    internal fun toSet(): Set<KeyedPackResource<R>> =
        resources.map { (key, value) -> KeyedPackResource(key, value) }.toSet()
}
