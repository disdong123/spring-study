rootProject.name = "spring-study"

include(
    "spring-batch-study:jpa-example",
    "spring-batch-study:multi-datasource-example",
    "spring-reactive-study:core",
    "spring-reactive-study:mvc-server",
    "spring-reactive-study:server-test",
    "spring-reactive-study:webflux-server",
    "spring-reflection-study-jackson-test",
    "spring-http-connection-study",
    "spring-jpa-study:transactional-test",
    "spring-jpa-study:sql-function-example"
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
