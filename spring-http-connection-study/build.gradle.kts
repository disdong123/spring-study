plugins {
    id("application")
}
dependencies {
    implementation(libs.spring.boot.starter.web)
    // https://mvnrepository.com/artifact/org.apache.httpcomponents.client5/httpclient5
    implementation("org.apache.httpcomponents.client5:httpclient5:5.3-alpha1")
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
