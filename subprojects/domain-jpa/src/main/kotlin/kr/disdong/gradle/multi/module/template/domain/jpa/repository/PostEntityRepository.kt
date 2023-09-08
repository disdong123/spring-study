package kr.disdong.gradle.multi.module.template.domain.jpa.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.disdong.gradle.multi.module.template.domain.jpa.domain.PostEntity
import kr.disdong.gradle.multi.module.template.domain.jpa.domain.QPostEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PostEntityRepository : JpaRepository<PostEntity, Long>, PostEntityRepositoryCustom

interface PostEntityRepositoryCustom {
    fun hello(): MutableList<PostEntity>
}

class PostEntityRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : PostEntityRepositoryCustom {

    override fun hello(): MutableList<PostEntity> {

        return jpaQueryFactory.selectFrom(QPostEntity.postEntity)
            .fetch()
    }
}
