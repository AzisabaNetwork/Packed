package net.azisaba.packed.dsl

import net.azisaba.packed.PackFormat
import net.azisaba.packed.PackMeta
import net.kyori.adventure.text.Component

@PackedDsl
class MetadataScope internal constructor() {
    private var minFormat: PackFormat? = null
    private var maxFormat: PackFormat? = null

    private var packFormat: Int? = null

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

    internal fun build(): PackMeta = PackMeta(
        PackMeta.Info(
            checkNotNull(minFormat) { "minFormat must be specified" },
            checkNotNull(maxFormat) { "maxFormat must be specified" },
            packFormat,
            checkNotNull(description) { "description must be specified" }
        )
    )
}