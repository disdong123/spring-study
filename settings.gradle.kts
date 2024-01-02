rootProject.name = "spring-study"

include(
    "core",
    "kotlin-example",
    "spring-batch:jpa-example",
    "spring-batch:multi-datasource-example",
    "spring-reactive-test:core",
    "spring-reactive-test:mvc-server",
    "spring-reactive-test:server-test",
    "spring-reactive-test:webflux-server",
    "spring-jackson-with-reflection",
    "spring-http-connection-example",
    "spring-db:transactional-example",
    "spring-db:sql-function-example",
    "spring-db:dbcp-example",
    "web-server-example",
    "spring-basic"
)

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        maven {
            url = uri("https://maven.pkg.github.com/disdong123/version-catalog")
            credentials {
                // PAT, github username 을 환경변수 (.zshrc 등)로 저장해야합니다.
                username = System.getenv("DISDONG_USERNAME")
                password = System.getenv("DISDONG_TOKEN")
            }
        }
    }
    versionCatalogs {
        create("libs") {
            from("kr.disdong:spring-version-catalog:0.0.19")
        }
    }
}
