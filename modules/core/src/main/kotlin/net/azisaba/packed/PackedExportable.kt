package net.azisaba.packed

import kotlinx.serialization.json.Json

fun interface PackedExportable {
    fun export(context: PackedExportContext)
}

data class PackedExportContext(val pathResolver: PackedPathResolver, val json: Json)
