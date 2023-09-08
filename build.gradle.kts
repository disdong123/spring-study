@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.kotlin.plugin.spring) apply false
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ktlint)
}

allprojects {
    group = "kr.disdong"
    version = "0.0.1-SNAPSHOT"
    repositories {
        mavenCentral()
        maven { url = uri("https://artifactory-oss.prod.netflix.net/artifactory/maven-oss-candidates") }
    }
}

subprojects {
    val libs = rootProject.libs
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    // 코틀린은 kotlinc 로 컴파일되므로, 기존 자바로 작성된 Annotation Process 로는 코틀린 annotation 을 처리할 수 없습니다.
    // Kotlin annotation processing tool 을 이용하면 코틀린이 자바 어노테이션을 처리할 때 코틀린 어노테이션 처리를 포함해줍니다.
    apply(plugin = "kotlin-kapt")
    // ktlint
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.1")
        }
    }

    dependencies {
        implementation(platform(libs.spring.cloud.dependencies))
        implementation(libs.kotlin.reflect)
        implementation(libs.kotlin.stdlib.jdk8)
        implementation(libs.jackson.module.kotlin)
        implementation(libs.spring.boot.configuration.processor)
        implementation(libs.springdoc.openapi.starter.webmvc.ui)

        testImplementation(libs.spring.boot.starter.test)
        testImplementation("io.mockk:mockk:1.13.4")
        testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:0.4.10")
    }

    java.sourceCompatibility = JavaVersion.VERSION_17

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.getByName("bootJar") {
        enabled = false
    }

    tasks.getByName("jar") {
        enabled = true
    }
}
