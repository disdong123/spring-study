rootProject.name = "spring-study"

include(
    "spring-batch-study-jpa",
    "spring-batch-study-multi-datasource",
    "spring-reactive-study-core",
    "spring-reactive-study-mvc-server",
    "spring-reactive-study-server-test",
    "spring-reactive-study-webflux-server",
    "spring-reflection-study-jackson",
    "spring-reflection-study-transactional",
    "spring-http-connection-study",
    "spring-sql-function-study"
)

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositories {
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
            from("kr.disdong:spring-version-catalog:0.0.15")
        }
    }
}
