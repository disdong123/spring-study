@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.plugin.jpa)
}

dependencies {
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.querydsl.jpa) {
        artifact {
            classifier = "jakarta"
        }
    }
    kapt(libs.querydsl.apt) {
        artifact {
            classifier = "jakarta"
        }
    }
    kapt(libs.jakarta.persistence.api)
    kapt(libs.jakarta.annotation.api)
    runtimeOnly(libs.mysql.connector.java)
    testImplementation(libs.h2.database)
}

tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}
