package spring.http.connection.example.restclient

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.client.ReactorNettyClientRequestFactory
import org.springframework.web.client.RestClient
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class RestClientConfig {

    @Bean
    fun defaultRestClient(): RestClient {
        // will use SimpleClientHttpRequestFactory
        return RestClient.builder().build()
    }

    @Bean
    fun httpComponentsClient(): RestClient {
        return RestClient.builder().requestFactory(HttpComponentsClientHttpRequestFactory()).build()
    }

    @Bean
    fun reactorClient(): RestClient {
        // need reactor-netty dependency for reactor.netty.http.client.HttpClient.
        // create reactor-http-nio thread pool and use it.
        return RestClient.builder().requestFactory(ReactorNettyClientRequestFactory()).build()
    }

    @Bean
    fun webclient(): WebClient {
        // need webflux dependency.
        // use reactor-netty thread pool
        return WebClient.builder().build()
    }
}
