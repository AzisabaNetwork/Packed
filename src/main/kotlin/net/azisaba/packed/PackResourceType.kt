package net.azisaba.packed

import kotlinx.serialization.json.Json
import net.azisaba.packed.util.PackPathResolver

fun interface PackResourceType<R : Any> {
    fun build(json: Json, pathResolver: PackPathResolver, resourceSet: Set<R>)
}
