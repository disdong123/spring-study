package spring.http.connection.example

import org.apache.hc.client5.http.classic.HttpClient
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager
import org.apache.hc.client5.http.protocol.HttpClientContext
import org.apache.hc.core5.http.EntityDetails
import org.apache.hc.core5.http.HttpRequest
import org.apache.hc.core5.http.HttpRequestInterceptor
import org.apache.hc.core5.http.HttpResponse
import org.apache.hc.core5.http.HttpResponseInterceptor
import org.apache.hc.core5.http.protocol.HttpContext
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

    @Bean
    fun connectionTestTemplate(): RestTemplate {
        val httpclient: CloseableHttpClient = HttpClients.custom()
            .addRequestInterceptorLast(object : HttpRequestInterceptor {
                override fun process(p0: HttpRequest?, p1: EntityDetails?, p2: HttpContext?) {
                    val context = HttpClientContext.adapt(p2)
                    println("request")
                }
            })
            .addResponseInterceptorLast(object : HttpResponseInterceptor {
                override fun process(p0: HttpResponse?, p1: EntityDetails?, p2: HttpContext?) {
                    val context = HttpClientContext.adapt(p2)
                    println("============ response ============")
                    println(context.getAttribute("http.request").toString())
                    println(context.getAttribute("http.route").toString())
                    println(context.getAttribute("http.connection-endpoint").toString())
                }
            })
            .build()

        val requestFactory = HttpComponentsClientHttpRequestFactory()
        requestFactory.httpClient = httpclient
        return RestTemplate(requestFactory)
    }
}
