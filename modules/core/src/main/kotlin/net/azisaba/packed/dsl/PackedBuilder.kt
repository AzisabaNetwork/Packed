package net.azisaba.packed.dsl

import net.azisaba.packed.*
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

    fun <T : Any> component(factory: (PackedEntries<T>) -> PackedComponent<T>, block: ComponentScope<T>.() -> Unit) {
        val scope = ComponentScope(factory)
        scope.block()
        components.add(scope.build())
    }

    fun includeDirectory(path: Path, sourceSubPath: String = "", targetSubPath: String = "") =
        otherSource(PackedSource.directory(path, sourceSubPath, targetSubPath))

    fun includeZip(path: Path, sourceSubPath: String = "", targetSubpath: String = "") =
        otherSource(PackedSource.zip(path, sourceSubPath, targetSubpath))

    fun <T : Any> includeJavaResources(kClass: KClass<T>, sourceSubPath: String = "assets", targetSubPath: String = "assets") =
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
