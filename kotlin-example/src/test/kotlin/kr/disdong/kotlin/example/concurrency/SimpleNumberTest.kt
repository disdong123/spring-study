package kr.disdong.kotlin.example.concurrency

import kr.disdong.core.Tester
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

internal class SimpleNumberTest {

    @Test
    fun `단일스레드에서 정수값 + 1 연산을 1000번 한 결과는 1000이다`() {
        // given
        val number = SimpleNumber()

        // when
        Tester.request(1, 1000) {
            number.plusOne()
        }

        // then
        assertEquals(number.value, 1000)
    }

    @Test
    fun `멀티 스레드에서는 공유변수에 대한 경합상황이 발생할 수 있으므로 정수값 + 1 연산을 1000번 하더라도 결과가 1000이 아니다`() {
        // given
        val number = SimpleNumber()

        // when
        Tester.request(20, 1000) {
            number.plusOne()
        }

        // then
        assertNotEquals(number.value, 1000)
    }
}
