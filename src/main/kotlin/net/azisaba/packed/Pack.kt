package net.azisaba.packed

data class Pack(
    val metadata: PackMetadata,
    val resourceMap: Map<PackResourceType<*>, Set<*>> = emptyMap(),
)
