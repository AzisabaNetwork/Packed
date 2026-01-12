plugins {
    `maven-publish`
    kotlin("jvm") version libs.versions.kotlin.get()
    kotlin("plugin.serialization") version libs.versions.kotlin.get()
}

group = "com.tksimeji"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc"
    }
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    compileOnly(libs.io.papermc.paper.api)
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(8)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            artifact(tasks.jar)
        }
    }

    repositories {
        maven {
            name = "azisaba"
            url = if (project.version.toString().endsWith("-SNAPSHOT")) {
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

tasks.test {
    useJUnitPlatform()
}