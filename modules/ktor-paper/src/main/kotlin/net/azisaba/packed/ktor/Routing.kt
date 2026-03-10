package net.azisaba.packed.ktor

import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.response.respondFile
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import java.nio.file.Path

internal fun Application.configureRouting(zipPath: Path) {
    routing {
        get("/") {
            call.response.headers.append(HttpHeaders.ContentType, "application/json")
            call.respondFile(zipPath.toFile())
        }
    }
}
