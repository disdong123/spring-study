@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.plugin.jpa)
}

dependencies {
    implementation(libs.spring.boot.starter.web)
    api(libs.spring.boot.starter.data.jpa)
    api(libs.hibernate.types)
    api(libs.infobip.spring.data.jpa.querydsl.boot.starter)
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
    runtimeOnly(libs.mysql.connector.java)
    testImplementation(libs.h2.database)
}

tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}
