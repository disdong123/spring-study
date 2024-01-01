package kr.disdong.spring.basic.dbcp.core

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.annotation.Transactional
import javax.sql.DataSource

@SpringBootTest
@Transactional
internal class DataSourceConfigTest {

    @Autowired
    lateinit var dataSource: DataSource

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS post (id INT PRIMARY KEY, title VARCHAR(255))")
    }

    @Test
    fun `datasource 가 등록되었는지 확인한다`() {
        val conn = dataSource.connection
        conn.close()
    }
}
