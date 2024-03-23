package kr.disdong.spring.db.jpa.summary.config.main

import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

object MasterConstants {
    const val ENTITY_MANAGER_FACTORY = "masterEntityManagerFactory"
    const val TRANSACTION_MANAGER = "masterTransactionManager"
    const val BASE_PACKAGES = "kr.disdong.spring.db.jpa.summary.model.main"
}

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = MasterConstants.ENTITY_MANAGER_FACTORY,
    transactionManagerRef = MasterConstants.TRANSACTION_MANAGER,
    basePackages = [MasterConstants.BASE_PACKAGES],
)
class MainDatabaseConfig {
    @Primary
    @ConfigurationProperties(prefix = "spring.master-datasource")
    @Bean("masterDataSource")
    fun masterDataSource(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
    }

    @Primary
    @Bean("masterEntityManagerFactory")
    fun masterEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
    ): LocalContainerEntityManagerFactoryBean {
        val build = builder
            .dataSource(masterDataSource())
            .packages(MasterConstants.BASE_PACKAGES)
            .persistenceUnit("masterEntityManager")
            .build()

        return build
    }

    @Primary
    @Bean("masterTransactionManager")
    fun masterTransactionManager(
        @Qualifier("masterEntityManagerFactory")
        masterEntityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager {
        return JpaTransactionManager(masterEntityManagerFactory)
    }

    @ConfigurationProperties(prefix = "spring.slave-datasource")
    @Bean("slaveDataSource")
    fun slaveDataSource(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
    }

    @Bean("slaveEntityManagerFactory")
    fun slaveEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
    ): LocalContainerEntityManagerFactoryBean {
        val build = builder
            .dataSource(slaveDataSource())
            .packages(MasterConstants.BASE_PACKAGES)
            .persistenceUnit("slaveEntityManager")
            .build()

        return build
    }

    @Bean("slaveTransactionManager")
    fun slaveTransactionManager(
        @Qualifier("slaveEntityManagerFactory")
        slaveEntityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager {
        return JpaTransactionManager(slaveEntityManagerFactory)
    }
}
