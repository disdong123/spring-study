package kr.disdong.spring.batch.study.multi.datasource.config

import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import org.hibernate.cfg.AvailableSettings
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.hibernate5.SpringBeanContainer
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "slaveEntityManager",
    transactionManagerRef = "slaveTransactionManager",
    basePackages = ["kr.disdong.spring.batch.study.multi.datasource.domain.slave"],
)
class SlaveDatabaseConfig {
    @ConfigurationProperties(prefix = "spring.slave-datasource")
    @Bean("slaveDataSource")
    fun slaveDataSource(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
    }

    @Bean
    fun slaveEntityManager(
        builder: EntityManagerFactoryBuilder,
        beanFactory: ConfigurableListableBeanFactory,
    ): LocalContainerEntityManagerFactoryBean {
        val build = builder
            .dataSource(slaveDataSource())
            .packages("kr.disdong.spring.batch.study.multi.datasource.domain.slave")
            .persistenceUnit("slaveEntityManager")
            .build()

        build.jpaPropertyMap[AvailableSettings.BEAN_CONTAINER] = SpringBeanContainer(beanFactory)

        return build
    }

    @Bean
    fun slaveTransactionManager(slaveEntityManager: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager(slaveEntityManager)
    }
}
