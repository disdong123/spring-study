package kr.disdong.spring.db.jpa.summary.config.sub

import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

object SubConstants {
    const val ENTITY_MANAGER_FACTORY = "subEntityManagerFactory"
    const val TRANSACTION_MANAGER = "subTransactionManager"
    const val BASE_PACKAGES = "kr.disdong.spring.db.jpa.summary.model.sub"
}

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = SubConstants.ENTITY_MANAGER_FACTORY,
    transactionManagerRef = SubConstants.TRANSACTION_MANAGER,
    basePackages = [SubConstants.BASE_PACKAGES],
)
class Slave2DatabaseConfig {
    @ConfigurationProperties(prefix = "spring.sub-datasource")
    @Bean("subDataSource")
    fun subDataSource(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
    }

    @Bean("subEntityManagerFactory")
    fun subEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
    ): LocalContainerEntityManagerFactoryBean {
        val build = builder
            .dataSource(subDataSource())
            .packages(SubConstants.BASE_PACKAGES)
            .persistenceUnit("subEntityManager")
            .build()

        return build
    }

    @Bean
    fun subTransactionManager(
        @Qualifier("subEntityManagerFactory")
        subEntityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager {
        return JpaTransactionManager(subEntityManagerFactory)
    }
}
