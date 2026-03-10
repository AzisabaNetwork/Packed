repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(project(":modules:core"))
    compileOnly(libs.paper.api)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
}
