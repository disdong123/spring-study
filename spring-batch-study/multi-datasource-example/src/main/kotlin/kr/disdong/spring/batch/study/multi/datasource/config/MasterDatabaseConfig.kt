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
import org.springframework.context.annotation.Primary
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
    entityManagerFactoryRef = "masterEntityManager",
    transactionManagerRef = "masterTransactionManager",
    basePackages = ["kr.disdong.spring.batch.study.multi.datasource.domain.master"],
)
class MasterDatabaseConfig {
    @Primary
    @ConfigurationProperties(prefix = "spring.master-datasource")
    @Bean("masterDataSource")
    fun masterDataSource(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
    }

    @Primary
    @Bean
    fun masterEntityManager(
        builder: EntityManagerFactoryBuilder,
        beanFactory: ConfigurableListableBeanFactory,
    ): LocalContainerEntityManagerFactoryBean {
        val build = builder
            .dataSource(masterDataSource())
            .packages("kr.disdong.spring.batch.study.multi.datasource.domain.master")
            .persistenceUnit("masterEntityManager")
            .build()

        build.jpaPropertyMap[AvailableSettings.BEAN_CONTAINER] = SpringBeanContainer(beanFactory)

        return build
    }

    @Primary
    @Bean
    fun masterTransactionManager(masterEntityManager: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager(masterEntityManager)
    }
}
