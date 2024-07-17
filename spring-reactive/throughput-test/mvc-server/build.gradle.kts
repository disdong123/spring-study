import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("application")
}
dependencies {
    implementation(libs.spring.boot.starter.web)
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
