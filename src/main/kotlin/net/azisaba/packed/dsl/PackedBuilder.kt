package net.azisaba.packed.dsl

import net.azisaba.packed.PackMeta
import net.azisaba.packed.Packed
import net.azisaba.packed.PackedComponent
import net.azisaba.packed.PackedEntries
import net.azisaba.packed.PackedSource
import net.azisaba.packed.equipment.PackEquipmentModel
import net.azisaba.packed.font.PackFont
import net.azisaba.packed.items.PackItemModel
import net.azisaba.packed.lang.PackLanguage
import net.azisaba.packed.models.PackModel
import net.azisaba.packed.sounds.PackSoundEvent
import java.nio.file.Path
import kotlin.reflect.KClass

@PackedDsl
class PackedBuilder internal constructor() {
    private var metadata: PackMeta? = null
    private val components: MutableList<PackedComponent<*>> = mutableListOf()
    private val otherSources: MutableList<PackedSource> = mutableListOf()

    fun metadata(block: MetadataScope.() -> Unit) {
        val scope = MetadataScope()
        scope.block()
        metadata = scope.build()
    }

    fun equipment(block: ComponentScope<PackEquipmentModel>.() -> Unit) =
        component(PackedComponent.Companion::equipmentModel, block)

    fun font(block: ComponentScope<PackFont>.() -> Unit) =
        component(PackedComponent.Companion::font, block)

    fun items(block: ComponentScope<PackItemModel>.() -> Unit) =
        component(PackedComponent.Companion::itemModel, block)

    fun lang(block: ComponentScope<PackLanguage>.() -> Unit) =
        component(PackedComponent.Companion::language, block)

    fun models(block: ComponentScope<PackModel>.() -> Unit) =
        component(PackedComponent.Companion::model, block)

    fun sounds(block: ComponentScope<PackSoundEvent>.() -> Unit) =
        component(PackedComponent.Companion::soundEvent, block)

    fun <T : Any> component(factory: (PackedEntries<T>) -> PackedComponent<T>, block: ComponentScope<T>.() -> Unit) {
        val scope = ComponentScope(factory)
        scope.block()
        components.add(scope.build())
    }

    fun includeDirectory(path: Path, sourceSubPath: Path = Path.of(""), targetSubPath: Path = Path.of("")) =
        otherSource(PackedSource.directory(path, sourceSubPath, targetSubPath))

    fun includeZip(path: Path, sourceSubPath: Path = Path.of(""), targetSubpath: Path = Path.of("")) =
        otherSource(PackedSource.zip(path, sourceSubPath, targetSubpath))

    fun <T : Any> includeJavaResources(kClass: KClass<T>, sourceSubPath: Path = Path.of("assets"), targetSubPath: Path = Path.of("assets")) =
        otherSource(PackedSource.javaResources(kClass, sourceSubPath, targetSubPath))

    fun otherSource(otherSource: PackedSource) {
        otherSources.add(otherSource)
    }

    internal fun build(): Packed = Packed(
        checkNotNull(metadata) { "metadata must be specified" },
        components.toList(),
        otherSources.toList(),
    )
}
