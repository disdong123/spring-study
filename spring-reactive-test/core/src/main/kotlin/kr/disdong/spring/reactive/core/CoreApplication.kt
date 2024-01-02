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
            port = 3306,
            database = "reactive-study",
            username = "root",
            password = "root",
            maxActiveConnections = 100,
            maxIdleTime = 1000L,
            maxPendingQueries = 10000,
            connectionValidationInterval = 1000L,
        )

        return MySQLConnectionBuilder.createConnectionPool(configuration)
    }
}
