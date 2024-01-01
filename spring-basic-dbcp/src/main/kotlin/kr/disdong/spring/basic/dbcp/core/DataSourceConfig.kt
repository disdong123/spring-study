package kr.disdong.spring.basic.dbcp.core

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import org.springframework.jdbc.support.JdbcTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.sql.Driver
import javax.sql.DataSource

@AutoConfiguration
@ConditionalOnClass(name = ["org.springframework.jdbc.core.JdbcOperations"])
@EnableConfigurationProperties(DataSourceProperties::class)
@EnableTransactionManagement // 정의한 jdbcTransactionManager 를 이용하여 @Transactional 을 사용할 수 있다
class DataSourceConfig {
    @Bean
    @ConditionalOnClass(HikariDataSource::class) // hikariDataSource 가 있을때만 이를 이용해서 빈을 등록하도록 한다
    fun hikariDataSource(properties: DataSourceProperties): DataSource {
        val dataSource = HikariDataSource()
        dataSource.driverClassName = properties.driverClassName
        dataSource.jdbcUrl = properties.url
        dataSource.username = properties.username
        dataSource.password = properties.password
        return dataSource
    }

    /**
     * 위의 hikariDataSource 가 생성되지 않았다면 아래의 dataSource 를 생성한다
     *
     * @param properties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    fun dataSource(properties: DataSourceProperties): DataSource {
        val dataSource = SimpleDriverDataSource()
        dataSource.setDriverClass(Class.forName(properties.driverClassName) as Class<out Driver>)
        dataSource.url = properties.url
        dataSource.username = properties.username
        dataSource.password = properties.password
        return dataSource
    }

    @Bean
    @ConditionalOnSingleCandidate(DataSource::class)
    fun jdbcTemplate(dataSource: DataSource): JdbcTemplate {
        return JdbcTemplate(dataSource)
    }

    @Bean
    @ConditionalOnSingleCandidate(DataSource::class)
    fun jdbcTransactionManager(dataSource: DataSource): JdbcTransactionManager {
        return JdbcTransactionManager(dataSource)
    }
}
