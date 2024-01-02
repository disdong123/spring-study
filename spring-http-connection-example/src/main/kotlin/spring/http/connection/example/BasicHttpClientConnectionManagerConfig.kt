package spring.http.connection.example

import org.apache.hc.client5.http.classic.HttpClient
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager
import org.apache.hc.core5.util.Timeout
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class BasicHttpClientConnectionManagerConfig {
    @Bean
    fun basicHttpClientConnectionManager(): BasicHttpClientConnectionManager {
        return BasicHttpClientConnectionManager()
    }

    @Bean
    fun basicRequestConfig(): RequestConfig {
        return RequestConfig.custom()
            .setConnectionRequestTimeout(Timeout.of(Duration.ofMillis(1000L)))
            .build()
    }

    @Bean
    fun basicHttpClient(
        basicHttpClientConnectionManager: BasicHttpClientConnectionManager,
        basicRequestConfig: RequestConfig
    ): CloseableHttpClient {
        return HttpClientBuilder
            .create()
            .setConnectionManager(basicHttpClientConnectionManager)
            .setDefaultRequestConfig(basicRequestConfig)
            .build()
    }

    @Bean
    fun basicRestTemplate(basicHttpClient: HttpClient): RestTemplate {
        val requestFactory = HttpComponentsClientHttpRequestFactory()
        requestFactory.httpClient = basicHttpClient
        return RestTemplate(requestFactory)
    }
}
