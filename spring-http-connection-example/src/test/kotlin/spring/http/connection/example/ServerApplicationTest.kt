package spring.http.connection.example

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
internal class ServerApplicationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `RestTemplate 은 기본적으로 각 요청에 대해 새로운 커넥션을 생성한다`() {
        // val restTemplate = RestTemplate()
        // logger.info(restTemplate.getForObject("http://localhost:8080/default", String::class.java))

        mockMvc.perform(get("/default"))
            .andExpect(status().isOk)
            .andExpect(content().string("hello"))
    }

    @Test
    fun `BasicHttpClientConnectionManager 은 각 요청에 대해 새로운 커넥션을 생성한다`() {
        // val restTemplate = RestTemplate()
        // logger.info(restTemplate.getForObject("http://localhost:8080/basic", String::class.java))

        mockMvc.perform(get("/basic"))
            .andExpect(status().isOk)
            .andExpect(content().string("hello"))
    }

    @Test
    fun `PoolingHttpClientConnectionManager 를 이용하면 커넥션 풀을 관리할 수 있다`() {
        // val restTemplate = RestTemplate()
        // logger.info(restTemplate.getForObject("http://localhost:8080/pool", String::class.java))

        mockMvc.perform(get("/pool"))
            .andExpect(status().isOk)
            .andExpect(content().string("hello"))
    }

    @Test
    fun `RestTemplateBuilder 는 기본적으로 HttpComponentsClientHttpRequestFactory 을 이용하며 커넥션 풀을 갖는다`() {
        // val restTemplate = RestTemplate()
        // logger.info(restTemplate.getForObject("http://localhost:8080/builder", String::class.java))

        mockMvc.perform(get("/builder"))
            .andExpect(status().isOk)
            .andExpect(content().string("hello"))
    }

    @Test
    fun `응답을 인터셉터하여 response info 를 확인한다`() {
        /**
         * 아래는 호출한 결과로, 순서대로 request, route, connection-endpoint 정보를 출력합니다.
         * 결론적으로, route 는 hostname:port 를 의미하고, 같은 route 에 대해서는 커넥션을 재사용합니다.
         *
         * ============ response ============
         * GET https://www.socar.kr/
         * {s}->https://www.socar.kr:443
         * 192.168.0.118:63383<->13.225.131.56:443
         *
         * ============ response ============
         * GET https://www.socar.kr/guide
         * {s}->https://www.socar.kr:443
         * 192.168.0.118:63383<->13.225.131.56:443
         *
         * ============ response ============
         * GET https://www.naver.com/
         * {s}->https://www.naver.com:443
         * 192.168.0.118:63384<->223.130.195.200:443
         *
         * ============ response ============
         * GET https://www.socar.kr/
         * {s}->https://www.socar.kr:443
         * 192.168.0.118:63383<->13.225.131.56:443
         *
         * ============ response ============
         * GET https://www.naver.com/
         * {s}->https://www.naver.com:443
         * 192.168.0.118:63384<->223.130.195.200:443
         *
         * ============ response ============
         * GET https://map.naver.com/
         * {s}->https://map.naver.com:443
         * 192.168.0.118:63385<->110.93.151.164:443
         *
         * ============ response ============
         * GET https://map.naver.com/p/
         * {s}->https://map.naver.com:443
         * 192.168.0.118:63385<->110.93.151.164:443
         */

        mockMvc.perform(get("/connection-test"))
            .andExpect(status().isOk)
            .andExpect(content().string("hello"))
    }
}
