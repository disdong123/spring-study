


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
import org.springframework.core.io.ClassPathResource
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.init.DataSourceInitializer
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.support.TransactionSynchronizationManager
import javax.sql.DataSource

object MainConstants {
    const val ENTITY_MANAGER_FACTORY = "mainEntityManagerFactory"
    const val TRANSACTION_MANAGER = "mainTransactionManager"
    const val BASE_PACKAGES = "kr.disdong.spring.db.jpa.summary.model.main"
}

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = MainConstants.ENTITY_MANAGER_FACTORY,
    transactionManagerRef = MainConstants.TRANSACTION_MANAGER,
    basePackages = [MainConstants.BASE_PACKAGES],
)
class MainDatabaseConfig {

    @ConfigurationProperties(prefix = "spring.master-datasource")
    @Bean("masterDataSource")
    fun masterDataSource(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
    }

    @ConfigurationProperties(prefix = "spring.slave-datasource")
    @Bean("slaveDataSource")
    fun slaveDataSource(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
    }

    @Bean("routingDataSource")
    fun routingDataSource(
        @Qualifier("masterDataSource")
        masterDataSource: DataSource,
        @Qualifier("slaveDataSource")
        slaveDataSource: DataSource
    ): DataSource {
        return DynamicRoutingDataSource().apply {
            setTargetDataSources(mapOf("master" to masterDataSource, "slave" to slaveDataSource))
            setDefaultTargetDataSource(masterDataSource)
        }
    }

    @Primary
    @Bean
    fun lazyDataSource(routingDataSource: DataSource): DataSource = LazyConnectionDataSourceProxy(routingDataSource)

    @Primary
    @Bean("mainEntityManagerFactory")
    fun mainEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("lazyDataSource")
        lazyDataSource: DataSource,
    ): LocalContainerEntityManagerFactoryBean {
        val build = builder
            .dataSource(lazyDataSource)
            .packages(MainConstants.BASE_PACKAGES)
            .persistenceUnit("mainEntityManager")
            .build()

        return build
    }

    @Primary
    @Bean("mainTransactionManager")
    fun mainTransactionManager(
        @Qualifier("mainEntityManagerFactory")
        mainEntityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager {
        return JpaTransactionManager(mainEntityManagerFactory)
    }

    /**
     * ddl-auto 는 EntityManagerFactory 가 생성될 때 수행됩니다.
     * default target 이 masterDataSource 이므로 masterDataSource 에 대해서만 수행됩니다.
     */
    @Bean
    fun dataSourceInitializer(
        @Qualifier("slaveDataSource")
        slaveDataSource: DataSource,
    ): DataSourceInitializer? {
        val initializer = DataSourceInitializer()
        initializer.setDataSource(slaveDataSource)
        initializer.setDatabasePopulator(databasePopulator())
        return initializer
    }

    private fun databasePopulator(): ResourceDatabasePopulator {
        val populator = ResourceDatabasePopulator()
        populator.addScript(ClassPathResource("sql/slave.sql"))
        populator.setContinueOnError(true)
        return populator
    }
}

internal class DynamicRoutingDataSource : AbstractRoutingDataSource() {
    override fun determineCurrentLookupKey(): Any {
        return when {
            TransactionSynchronizationManager.isCurrentTransactionReadOnly() -> "slave"
            else -> "master"
        }
    }
}
