plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "packed"

include("modules:core")
include("modules:ktor-paper")
include("modules:resource-pack")
