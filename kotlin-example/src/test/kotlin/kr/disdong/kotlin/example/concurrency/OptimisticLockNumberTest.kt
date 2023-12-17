package kr.disdong.kotlin.example.concurrency

import kr.disdong.core.Tester
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class OptimisticLockNumberTest {
    @Test
    fun `Lock-free 을 이용한 동시성 이슈 해결`() {
        // given
        val number = OptimisticLockNumber()

        // when
        Tester.request(20, 1000) {
            number.plusOne()
        }

        // then
        assertEquals(number.getValue(), 1000)
    }
}
