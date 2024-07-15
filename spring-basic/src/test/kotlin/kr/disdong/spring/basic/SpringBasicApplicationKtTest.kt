package kr.disdong.spring.basic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatusCode

internal class SpringBasicApplicationKtTest {

    private val client = TestRestTemplate()

    @BeforeEach
    fun setUp() {
        server3()
    }

    @Disabled
    @Test
    fun `동작 테스트 - 8080 포트 필ㅇ`() {
        val response = client.getForEntity("http://localhost:8080/posts", String::class.java)
        assertEquals(response.statusCode, HttpStatusCode.valueOf(200))
        assertEquals(response.body, "posts")
    }
}
