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
    mainClass.set("kr.disdong.spring.reactive.study.mvc.server.master.MasterWebfluxServerApplicationKt")
}
