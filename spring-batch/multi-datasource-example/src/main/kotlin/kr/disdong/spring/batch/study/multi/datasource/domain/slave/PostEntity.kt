package kr.disdong.spring.batch.study.multi.datasource.domain.slave

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "post")
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "title", nullable = false)
    var title: String,
) {

    override fun toString(): String {
        return "Post(id=$id, title='$title')"
    }
}

interface PostRepository : org.springframework.data.jpa.repository.JpaRepository<Post, Int>
