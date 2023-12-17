package kr.disdong.kotlin.example.concurrency

import kr.disdong.core.Tester
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PessimisticLockNumberTest {

    @Test
    fun `Lock 을 이용한 동시성 이슈 해결`() {
        // given
        val number = PessimisticLockNumber()

        // when
        Tester.request(20, 1000) {
            number.plusOne()
        }

        // then
        assertEquals(number.value, 1000)
    }
}
