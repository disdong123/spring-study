package kr.disdong.spring.batch.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository

@Entity
@Table(name = "member")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "name", nullable = false)
    var name: String,
) {

    override fun toString(): String {
        return "Member(id=$id, name='$name')"
    }
}

interface MemberRepository : JpaRepository<Member, Int>
