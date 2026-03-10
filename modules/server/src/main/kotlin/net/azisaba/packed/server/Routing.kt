package net.azisaba.packed.server

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.nio.file.Path

internal fun Application.configureRouting(zipPath: Path) {
    routing {
        get("/") {
            call.response.headers.append(HttpHeaders.ContentType, "application/json")
            call.respondFile(zipPath.toFile())
        }
    }
}
