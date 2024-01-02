package kr.disdong.spring.db.transactional.member

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository

@Entity(name = "member")
class MemberEntity(
    @Id
    val id: Long = 0,
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val name: String,
)

interface MemberRepository : JpaRepository<MemberEntity, Long>
