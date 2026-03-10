dependencies {
    compileOnly(project(":modules:core"))
    api(libs.ktor.server.core)
    api(libs.ktor.server.netty)
}
