import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "net.azisaba.packed"
version = System.getenv("VERSION") ?: "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin { jvmToolchain(8) }
java { toolchain.languageVersion.set(JavaLanguageVersion.of(21)) }

val adventure = libs.adventure
val joml = libs.joml
val kotlinx = libs.kotlinx

subprojects {
    group = rootProject.group
    version = rootProject.version

    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

    repositories {
        mavenCentral()
    }

    dependencies {
        compileOnly(adventure.api)
        compileOnly(adventure.text.serializer.gson)
        compileOnly(joml)
        implementation(kotlinx.serialization.json)
        testImplementation(kotlin("test"))
    }

    configure<PublishingExtension> {
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
                    if (version.toString().contains("SNAPSHOT")) {
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
}
