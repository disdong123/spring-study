package spring.http.connection.study

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
        // println(restTemplate.getForObject("http://localhost:8080/default", String::class.java))

        mockMvc.perform(get("/default"))
            .andExpect(status().isOk)
            .andExpect(content().string("hello"))
    }

    @Test
    fun `BasicHttpClientConnectionManager 은 각 요청에 대해 새로운 커넥션을 생성한다`() {
        // val restTemplate = RestTemplate()
        // println(restTemplate.getForObject("http://localhost:8080/basic", String::class.java))

        mockMvc.perform(get("/basic"))
            .andExpect(status().isOk)
            .andExpect(content().string("hello"))
    }

    @Test
    fun `PoolingHttpClientConnectionManager 를 이용하면 커넥션 풀을 관리할 수 있다`() {
        // val restTemplate = RestTemplate()
        // println(restTemplate.getForObject("http://localhost:8080/pool", String::class.java))

        mockMvc.perform(get("/pool"))
            .andExpect(status().isOk)
            .andExpect(content().string("hello"))
    }

    @Test
    fun `RestTemplateBuilder 는 기본적으로 HttpComponentsClientHttpRequestFactory 을 이용하며 커넥션 풀을 갖는다`() {
        // val restTemplate = RestTemplate()
        // println(restTemplate.getForObject("http://localhost:8080/builder", String::class.java))

        mockMvc.perform(get("/builder"))
            .andExpect(status().isOk)
            .andExpect(content().string("hello"))
    }
}
