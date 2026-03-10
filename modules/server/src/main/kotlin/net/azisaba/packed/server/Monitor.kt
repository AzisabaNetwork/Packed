package net.azisaba.packed.server

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopped
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.deleteIfExists
import kotlin.use

internal fun Application.configureMonitor(tempDirectory: Path) {
    monitor.subscribe(ApplicationStopped) {
        Files.walk(tempDirectory).use { stream ->
            stream.sorted(Comparator.reverseOrder())
                .forEach(Path::deleteIfExists)
        }
    }
}
