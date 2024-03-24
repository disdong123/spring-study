package kr.disdong.spring.db.jpa.summary

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.PersistenceUnit
import jakarta.transaction.Transactional
import kr.disdong.spring.db.jpa.summary.model.main.MemberEntity
import kr.disdong.spring.db.jpa.summary.model.main.MemberRepository
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PersistenceContextTest {

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @PersistenceUnit(unitName = "mainEntityManager")
    private lateinit var entityManagerFactory: EntityManagerFactory

    @Nested
    inner class EntityManagerTest {
        @Test
        fun `주입된 entityManager instance 로 트랜잭션을 직접 관리할 수 없다`() {
            val member = MemberEntity(name = "name")
            assertThrows(IllegalStateException::class.java) {
                entityManager.transaction
            }
        }
    }

    @Nested
    inner class `EntityManagerFactory 로 생성한 EntityManager 를 사용한다` {
        @Test
        fun `entityManager 는 persistence context 를 유지한다 - debug 를 걸어보면 SessionImpl 아래에 존재한다`() {
            val member = MemberEntity(name = "name")
            val em = entityManagerFactory.createEntityManager()
            val tx = em.transaction

            em.persist(member)

            tx.rollback()
        }
    }

    @Nested
    open inner class `@Transactional 로 확인한다` {
        @Test
        @Transactional
        open fun `persistence context 를 유지한다 - debug 를 걸고 TransactionSynchronizationManager getResourceMap() 를 호출하면 EntityManager 를 확인할 수 있다`() {
            val member = MemberEntity(name = "name")

            // 디버그 모드에서 TransactionSynchronizationManager.getResourceMap() 확인
            memberRepository.save(member)
        }
    }
}
