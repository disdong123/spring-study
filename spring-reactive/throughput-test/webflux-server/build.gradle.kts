import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("application")
}
dependencies {
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.kotlinx.coroutines.core)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
    implementation(libs.kotlin.stdlib.jdk8)

    // m1
    implementation("io.netty:netty-resolver-dns-native-macos:4.1.68.Final:osx-aarch_64")

    implementation(project(":spring-reactive:throughput-test:core"))
}

tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

application {
    mainClass.set("kr.disdong.spring.reactive.webflux.server.master.MasterWebfluxServerApplicationKt")
}

tasks.register<BootRun>("bootRunApp1") {
    group = "application"
    description = "Run Application1"
    mainClass.set("kr.disdong.spring.reactive.webflux.server.master.MasterWebfluxServerApplicationKt")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<BootRun>("bootRunApp2") {
    group = "application"
    description = "Run Application2"
    mainClass.set("kr.disdong.spring.reactive.webflux.server.slave.SlaveWebfluxServerApplicationKt")
    classpath = sourceSets["main"].runtimeClasspath
}
