import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("application")
}
dependencies {
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.kotlinx.coroutines.core)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
    implementation("io.projectreactor.netty:reactor-netty:1.1.21")
}

tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

application {
    mainClass.set("kr.disdong.spring.reactive.mvc.server.master.MasterMvcServerApplicationKt")
}

tasks.register<BootRun>("bootRunApp1") {
    group = "application"
    description = "Run Application1"
    mainClass.set("kr.disdong.spring.reactive.mvc.server.master.MasterMvcServerApplicationKt")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<BootRun>("bootRunApp2") {
    group = "application"
    description = "Run Application2"
    mainClass.set("kr.disdong.spring.reactive.mvc.server.slave.SlaveMvcServerApplicationKt")
    classpath = sourceSets["main"].runtimeClasspath
}
