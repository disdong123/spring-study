package kr.disdong.spring.batch.study.multi.datasource

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MultiDatasourceApplication

fun main(args: Array<String>) {
    runApplication<MultiDatasourceApplication>(*args)
}
