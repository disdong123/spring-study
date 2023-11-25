package kr.disdong.spring.jpa.study.sql.function.store

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal

@TestConfiguration
class TestConfig {
    @Bean
    fun jpaQueryFactory(entityManager: EntityManager): JPAQueryFactory {
        return JPAQueryFactory(entityManager)
    }
}

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig::class)
internal class StoreRepositoryImplTest {
    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var sut: StoreRepository

    @BeforeEach
    fun setup() {
        sut.deleteAll()
        sut.saveAll(createEntities())
        entityManager.createNativeQuery(MysqlFunctionAlias.createPointAlias()).executeUpdate()
        entityManager.createNativeQuery(MysqlFunctionAlias.createStDistanceSphereAlias()).executeUpdate()
    }

    @Test
    fun `SELECT, WHERE 절에서 ST_DISTANCE_SPHERE 을 이용한다`() {
        val response = sut.ST_DISTANCE_SPHERE(37.501025, 127.039595)

        assertEquals(response.size, 2)
        response.map { assertEquals(it.distance, BigDecimal.ONE) }
    }

    private fun createEntities(): List<StoreEntity> {
        return listOf(
            StoreEntity(
                name = "스타벅스",
                latitude = 37.01001,
                longitude = 127.000111,
            ),
            StoreEntity(
                name = "이디야",
                latitude = 37.12312,
                longitude = 127.12312,
            ),
        )
    }
}
