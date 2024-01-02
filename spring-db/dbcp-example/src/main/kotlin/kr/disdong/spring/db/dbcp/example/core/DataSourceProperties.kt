package kr.disdong.spring.db.dbcp.example.core

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "disdong.spring.datasource")
class DataSourceProperties(
    val driverClassName: String,
    val url: String,
    val username: String,
    val password: String
)
