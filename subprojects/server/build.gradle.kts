dependencies {
    implementation(libs.spring.boot.starter.web)
    implementation(project(":subprojects:domain"))
}

tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}
