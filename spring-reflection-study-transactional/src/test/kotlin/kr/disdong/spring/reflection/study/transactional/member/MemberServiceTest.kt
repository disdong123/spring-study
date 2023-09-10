package kr.disdong.spring.reflection.study.transactional.member

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.aop.support.AopUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class MemberServiceTest {

    @Autowired
    private lateinit var memberService: MemberService

    @Autowired
    private lateinit var memberMixedService: MemberMixedService

    @Autowired
    private lateinit var memberTransactionService: MemberTransactionService

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @BeforeEach
    fun setup() {
        memberRepository.deleteAll()
    }

    @Nested
    @DisplayName("@Transactional 을 추가하면 프록시 객체가 생성되므로")
    inner class T1 {
        @Test
        fun `@Transactional 이 없는 경우 프록시 객체가 만들어지지 않는다`() {
            assertFalse(memberService.javaClass.toString().contains("CGLIB"))
            assertFalse(AopUtils.isAopProxy(memberService))
        }

        @Test
        fun `@Transactional 이 있는 경우 프록시 객체가 만들어진다 1`() {
            assertTrue(memberMixedService.javaClass.toString().contains("CGLIB"))
            assertTrue(AopUtils.isAopProxy(memberTransactionService))
        }

        @Test
        fun `@Transactional 이 있는 경우 프록시 객체가 만들어진다 2`() {
            assertTrue(memberTransactionService.javaClass.toString().contains("CGLIB"))
            assertTrue(AopUtils.isAopProxy(memberTransactionService))
        }
    }

    @Nested
    @DisplayName("@Transactional 로 인해 생성된 프록시 객체여도 정상적으로 트랜잭션 처리가 안될수도 있다.")
    inner class DescribeJoin {
        @Test
        fun `@Transactional 이 없으므로 록백되지 않는다`() {
            assertFalse(memberService.javaClass.toString().contains("CGLIB"))
            assertThrows(RuntimeException::class.java) {
                memberService.join()
            }
            assertFalse(memberRepository.findById(1).isEmpty)
        }

        @Test
        fun `join() 에서 내부 메서드 joinWithTransaction 을 호출하므로 프록시가 아닌 memberMixedService 의 joinWithTransaction 이 호출되어 롤백되지 않는다`() {
            assertTrue(memberMixedService.javaClass.toString().contains("CGLIB"))
            assertThrows(RuntimeException::class.java) {
                memberMixedService.join()
            }
            assertFalse(memberRepository.findById(1).isEmpty)
        }

        @Test
        fun `@Transactional 이 있으므로 롤백된다`() {
            assertTrue(memberTransactionService.javaClass.toString().contains("CGLIB"))
            assertThrows(RuntimeException::class.java) {
                memberTransactionService.joinCallNormalTransaction()
            }
            assertTrue(memberRepository.findById(1).isEmpty)
        }

        @Test
        fun `joinNewTransaction() 의 Propagation 이 NEW 로 하여 joinCallNewTransaction() 에서의 예외는 롤백이 안되는 것을 의도했지만, 실제로는 롤백이 된다`() {
            assertTrue(memberTransactionService.javaClass.toString().contains("CGLIB"))
            assertThrows(RuntimeException::class.java) {
                memberTransactionService.joinCallNewTransaction()
            }
            assertTrue(memberRepository.findById(1).isEmpty)
        }

        @Test
        fun `joinNewTransaction() 의 Propagation 이 NEW 이고, 다른 클래스인 memberMixedService 에서 호출하고 있으므로 정상적으로 프록시의 joinNewTransaction() 가 호출되어 의도대로 롤백이 되지 않는다`() {
            assertThrows(RuntimeException::class.java) {
                memberMixedService.joinWithTransactionCalljoinNewTransaction()
            }
            assertFalse(memberRepository.findById(1).isEmpty)
        }
    }
}
