package kr.disdong.gradle.multi.module.template.domain.jpa.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EntityScan
@EnableJpaRepositories
class JpaConfig
