package spring.http.connection.example.resttemplate

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class DefaultRestTemplateConfig {

    @Bean
    fun defaultRestTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean
    fun builderRestTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate {
        return restTemplateBuilder.build()
    }
}
