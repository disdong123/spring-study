plugins {
    id("application")
}
dependencies {
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.webflux)
    // https://mvnrepository.com/artifact/org.apache.httpcomponents.client5/httpclient5
    implementation("org.apache.httpcomponents.client5:httpclient5:5.3-alpha1")
    implementation("io.projectreactor.netty:reactor-netty:1.1.21")
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
