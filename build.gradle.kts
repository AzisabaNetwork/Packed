plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm") version libs.versions.kotlin.get()
    kotlin("plugin.serialization") version libs.versions.kotlin.get()
}

group = "net.azisaba"
version = "0.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") { name = "papermc" }
}

dependencies {
    compileOnly(libs.io.papermc.paper.api)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
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
            url = if (version.toString().endsWith("-SNAPSHOT"))
                uri("https://repo.azisaba.net/repository/maven-snapshots/")
            else
                uri("https://repo.azisaba.net/repository/maven-releases/")
            credentials {
                username = System.getenv("REPO_USERNAME")
                password = System.getenv("REPO_PASSWORD")
            }
        }
    }
}