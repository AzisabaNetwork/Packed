package net.azisaba.packed

import net.azisaba.packed.util.PackMetadata

data class Pack(
    val metadata: PackMetadata,
    val resourceMap: Map<PackResourceType<*>, Set<*>> = emptyMap(),
)
