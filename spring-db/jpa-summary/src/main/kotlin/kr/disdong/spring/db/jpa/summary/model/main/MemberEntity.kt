package kr.disdong.spring.db.jpa.summary.model.main

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository

@Entity(name = "member")

class MemberEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column
    var name: String,
) {
    override fun toString(): String {
        return "MemberEntity(id=$id, name='$name')"
    }
}

interface MemberRepository : JpaRepository<MemberEntity, Long>, CustomMemberRepository

interface CustomMemberRepository {
    fun findByName(name: String): MemberEntity?
}

class CustomMemberRepositoryImpl(
    private val mainJpaQueryFactory: JPAQueryFactory,
) : CustomMemberRepository {
    override fun findByName(name: String): MemberEntity? {
        return mainJpaQueryFactory
            .selectFrom(QMemberEntity.memberEntity)
            .where(QMemberEntity.memberEntity.name.eq(name))
            .fetchOne()
    }
}
