package kr.disdong.spring.sql.function.study

import kr.disdong.spring.sql.function.study.store.StoreQDto
import kr.disdong.spring.sql.function.study.store.StoreRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class SqlFunctionStudyApplication(
    private val storeRepository: StoreRepository,
) {

    @GetMapping("/hello")
    fun hello(): List<StoreQDto> {
        return storeRepository.ST_DISTANCE_SPHERE(37.501025, 127.039595)
    }
}

fun main(args: Array<String>) {
    runApplication<SqlFunctionStudyApplication>(*args)
}
