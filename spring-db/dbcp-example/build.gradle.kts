repositories {
    maven {
        url = uri("https://repo.clojars.org")
        name = "Clojars"
    }
}
dependencies {
    implementation(libs.spring.boot.starter.web)
    // https://mvnrepository.com/artifact/org.springframework/spring-jdbc
    implementation("org.springframework:spring-jdbc:6.1.2")
    runtimeOnly(libs.h2.database)
    implementation("hikari-cp:hikari-cp:3.0.1")
}
