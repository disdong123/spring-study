@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.plugin.jpa)
}

dependencies {
    implementation(project(":core"))
    implementation(libs.spring.boot.starter.batch)
    implementation(libs.spring.boot.starter.data.jpa)
    runtimeOnly(libs.mysql.connector.java)
    testImplementation(libs.h2.database)
}

tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}
