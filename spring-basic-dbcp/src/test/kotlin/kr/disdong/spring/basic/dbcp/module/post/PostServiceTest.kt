package kr.disdong.spring.basic.dbcp.module.post

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PostServiceTest {
    private val sut = PostService(object : PostRepository {
        override fun getPosts(): List<Post> {
            return listOf(
                Post(1, "title1"),
                Post(2, "title2"),
                Post(3, "title3"),
            )
        }
    })

    @Test
    fun `게시글 목록을 조회한다`() {
        assertEquals(sut.getPosts().size, 3)
    }
}
