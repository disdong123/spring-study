## AutoConfiguration
SpringBoot 에서는 추가한 의존성의 빈 설정과 생성을 자동으로 해주는 AutoConfiguration 기능을 제공합니다.

spring-boot 의 [spring-boot-autoconfigure](https://github.com/spring-projects/spring-boot/tree/main/spring-boot-project/spring-boot-autoconfigure) 에는 jdbc, batch 등 여러 AutoConfiguration 이 구현되어 있습니다.

spring boot 관련 의존성을 추가하게되면 org.springframework.boot.autoconfigure 도 함께 추가되는데, 여기의 META-INF/spring.factories 에서 자동구성 대상을 확인할 수 있습니다.

자동구성 대상 객체에 의한 빈 생성을 위해서는 @EnableAutoConfiguration 가 필요한데, @SpringBootApplication 은 @EnableAutoConfiguration 을 포함하고 있습니다.

AutoConfiguration 대상을 직접 만드려면 적절한 Bean 생성과 관련된 적절한 어노테이션을 이용하여 @AutoConfiguration 객체를 생성하면 됩니다.  

- @ConditionalOnMissingBean: 특정 Bean 이 정의되지 않을 때 조건을 만족한다.
- @ConditionalOnClass: 특정 클래스가 classpath 에 존재할 때 조건을 만족한다.
- @ConditionalOnSingleCandidate: 특정 타입의 Bean 이 하나만 존재할 때 조건을 만족한다.

위 어노테이션 이외에도 여러 어노테이션이 존재합니다.

## dbcp 테스트
- connection pool 을 사용하지 않는 경우
- connection pool 을 사용하는 경우
  - connection timeout
  - connection pool size
  - ...