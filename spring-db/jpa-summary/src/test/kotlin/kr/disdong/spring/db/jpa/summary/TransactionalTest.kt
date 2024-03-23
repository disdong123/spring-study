package kr.disdong.spring.db.jpa.summary

import kr.disdong.spring.db.jpa.summary.model.main.MemberRepository
import kr.disdong.spring.db.jpa.summary.model.main.TestService
import kr.disdong.spring.db.jpa.summary.model.sub.CarRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@DisplayName("multi-datasource 에서 Transactional 이 어떤 EntityManager 로 생성한 트랜잭션을 사용하는지 확인한다")
class TransactionalTest {

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var carRepository: CarRepository

    @Autowired
    private lateinit var testService: TestService

    @BeforeEach
    fun setup() {
        memberRepository.deleteAll()
        carRepository.deleteAll()
    }

    @Test
    fun `TxManager 를 명시하지 않으면 @Primary 인 Datasource 를 통해 생성된다 - 따라서 sub 의 트랜잭션은 없으므로 car 의 이름이 바뀌지 않는다`() {
        testService.noTxManager("hello")
        assertEquals("hello", memberRepository.findAll().first().name)
        assertNotEquals("hello", carRepository.findAll().first().name)
    }
    @Test
    fun `TxManager 를 명시하지 않으면 @Primary 인 Datasource 를 통해 생성된다 - 따라서 sub 의 트랜잭션은 없으므로 car 의 이름이 바뀌지 않는다2`() {
        testService.masterTxManager("hello")
        assertEquals("hello", memberRepository.findAll().first().name)
        assertNotEquals("hello", carRepository.findAll().first().name)
    }
    @Test
    fun `TxManager 를 명시하지 않으면 @Primary 인 Datasource 를 통해 생성된다 - 따라서 sub 의 트랜잭션은 없으므로 car 의 이름이 바뀌지 않는다3`() {
        testService.slaveTxManager("hello")
        assertEquals("hello", memberRepository.findAll().first().name)
        assertNotEquals("hello", carRepository.findAll().first().name)
    }
    @Test
    fun `TxManager 를 명시하지 않으면 @Primary 인 Datasource 를 통해 생성된다 - 따라서 sub 의 트랜잭션은 없으므로 car 의 이름이 바뀌지 않는다4`() {
        testService.subTxManager("hello")
        assertNotEquals("hello", memberRepository.findAll().first().name)
        assertEquals("hello", carRepository.findAll().first().name)
    }
}
