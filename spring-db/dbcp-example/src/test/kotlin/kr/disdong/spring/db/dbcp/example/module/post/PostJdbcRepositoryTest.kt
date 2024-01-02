package kr.disdong.spring.db.dbcp.example.module.post

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
internal class PostJdbcRepositoryTest {

    @Autowired
    lateinit var postRepository: PostRepository

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS post (id INT PRIMARY KEY, title VARCHAR(255))")
    }

    @Test
    fun `게시글 목록을 조회한다 1`() {
        val posts = postRepository.getPosts()
        assertEquals(0, posts.size)
    }

    @Test
    fun `게시글 목록을 조회한다 2`() {
        jdbcTemplate.update("INSERT INTO post VALUES (1, 'post')")
        val posts = postRepository.getPosts()
        assertEquals(1, posts.size)
    }
}
