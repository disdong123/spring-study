package kr.disdong.spring.db.jpa.summary.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QuerydslConfig(
    @PersistenceContext(unitName = "mainEntityManager")
    private val mainEntityManager: EntityManager,
    @PersistenceContext(unitName = "subEntityManager")
    private val subEntityManager: EntityManager,
) {

    @Bean("mainJpaQueryFactory")
    fun mainJpaQueryFactory(): JPAQueryFactory {
        return JPAQueryFactory(mainEntityManager)
    }

    @Bean("subJpaQueryFactory")
    fun subJpaQueryFactory(): JPAQueryFactory {
        return JPAQueryFactory(subEntityManager)
    }
}
