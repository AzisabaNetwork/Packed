plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "net.azisaba"
version = System.getenv("VERSION") ?: "0.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") { name = "papermc" }
}

dependencies {
    api(libs.ktor.server.core)
    api(libs.ktor.server.netty)
    compileOnly(libs.io.papermc.paper.api)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(kotlin("test"))
}

kotlin { jvmToolchain(8) }
java { toolchain.languageVersion.set(JavaLanguageVersion.of(21)) }

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = group.toString()
            artifactId = project.name
            version = version.toString()
        }
    }
    repositories {
        maven {
            name = "azisaba"
            url =
                if (version.toString().endsWith("-SNAPSHOT")) {
                    uri("https://repo.azisaba.net/repository/maven-snapshots/")
                } else {
                    uri("https://repo.azisaba.net/repository/maven-releases/")
                }
            credentials {
                username = System.getenv("REPO_USERNAME")
                password = System.getenv("REPO_PASSWORD")
            }
        }
    }
}
