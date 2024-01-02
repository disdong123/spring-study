package kr.disdong.spring.db.dbcp.example.module.post

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class PostJdbcRepository(
    private val jdbcTemplate: JdbcTemplate,
) : PostRepository {
    override fun getPosts(): List<Post> {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT * FROM post"
            ) { resultSet, _ ->
                val posts = mutableListOf<Post>()
                while (true) {
                    posts.add(
                        Post(
                            id = resultSet.getInt("id"),
                            title = resultSet.getString("title"),
                        )
                    )
                    if (resultSet.next().not()) {
                        break
                    }
                }
                posts.toList()
            } ?: listOf()
        } catch (e: EmptyResultDataAccessException) {
            return listOf()
        }
    }
}
