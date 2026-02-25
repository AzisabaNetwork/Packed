package net.azisaba.packed

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists

fun Pack.launchServer(
    host: String = "0.0.0.0",
    port: Int = 8080,
): EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration> {
    require(port in 1..65535) { "port must be in 1..65535" }
    val tempDirectory = Files.createTempDirectory("packed-server-")
    val packFile = tempDirectory.resolve("pack.zip")
    tempDirectory.createDirectories()
    buildZip(packFile)

    val server = embeddedServer(Netty, host = host, port = port) {
        configureRouting(packFile)
    }
    server.start(wait = false)

    Runtime.getRuntime().addShutdownHook(
        Thread {
            packFile.deleteIfExists()
            tempDirectory.toFile().deleteRecursively()
        },
    )

    return server
}

private fun Application.configureRouting(packFile: Path) {
    routing {
        get("/") {
            call.respondFile(packFile.toFile())
        }
    }
}
