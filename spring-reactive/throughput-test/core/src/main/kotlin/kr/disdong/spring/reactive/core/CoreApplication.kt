package kr.disdong.spring.reactive.core

import com.github.jasync.sql.db.Connection
import com.github.jasync.sql.db.ConnectionPoolConfigurationBuilder
import com.github.jasync.sql.db.ResultSet
import com.github.jasync.sql.db.mysql.MySQLConnectionBuilder
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class PostRepository(
    private val connection: Connection
) {
    fun findAll(): ResultSet {
        return connection.sendPreparedStatement("SELECT * from post")
            .get()
            .rows
    }
}

@SpringBootApplication
class CoreApplication {

    @Bean
    fun jConnection(): Connection {
        val configuration = ConnectionPoolConfigurationBuilder(
            host = "localhost",
            port = 3316,
            database = "reactive_study",
            username = "test",
            password = "test",
        )

        return MySQLConnectionBuilder.createConnectionPool(configuration)
    }
}
