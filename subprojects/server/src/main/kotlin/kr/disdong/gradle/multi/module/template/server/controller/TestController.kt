package kr.disdong.gradle.multi.module.template.server.controller

import kr.disdong.gradle.multi.module.template.domain.repository.PostDoRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
    private val postDoRepository: PostDoRepository
) {

    @GetMapping("/t1")
    fun a() {
        println(postDoRepository.findAll().size)
        println(postDoRepository.hello().size)
    }
}
