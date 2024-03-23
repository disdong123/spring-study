package kr.disdong.spring.db.jpa.summary.model.main

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityManager
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PersistenceContext
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
    fun create(member: MemberEntity): Long
}

class CustomMemberRepositoryImpl(
    private val mainJpaQueryFactory: JPAQueryFactory,
    @PersistenceContext(unitName = "masterEntityManager")
    private val entityManager: EntityManager
) : CustomMemberRepository {
    override fun create(member: MemberEntity): Long {
        return entityManager.createNativeQuery("INSERT INTO member (name) VALUES (:name)")
            .setParameter("name", member.name)
            .executeUpdate()
            .toLong()
    }
}
