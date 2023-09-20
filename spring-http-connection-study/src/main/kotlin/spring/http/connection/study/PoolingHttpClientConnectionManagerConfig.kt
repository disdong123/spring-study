package spring.http.connection.study

import org.apache.hc.client5.http.classic.HttpClient
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager
import org.apache.hc.core5.util.Timeout
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class PoolingHttpClientConnectionManagerConfig {
    @Bean
    fun poolingHttpClientConnectionManager(): PoolingHttpClientConnectionManager {
        val connectionManager = PoolingHttpClientConnectionManager()
        connectionManager.maxTotal = 100
        connectionManager.defaultMaxPerRoute = 5
        return connectionManager
    }

    @Bean
    fun poolingRequestConfig(): RequestConfig {
        return RequestConfig.custom()
            .setConnectionRequestTimeout(Timeout.of(Duration.ofMillis(1000L)))
            .build()
    }

    @Bean
    fun poolingHttpClient(
        poolingHttpClientConnectionManager: PoolingHttpClientConnectionManager,
        poolingRequestConfig: RequestConfig
    ): CloseableHttpClient {
        return HttpClientBuilder
            .create()
            .setConnectionManager(poolingHttpClientConnectionManager)
            .setDefaultRequestConfig(poolingRequestConfig)
            .build()
    }

    @Bean
    fun poolingRestTemplate(poolingHttpClient: HttpClient): RestTemplate {
        val requestFactory = HttpComponentsClientHttpRequestFactory()
        requestFactory.httpClient = poolingHttpClient
        return RestTemplate(requestFactory)
    }
}
