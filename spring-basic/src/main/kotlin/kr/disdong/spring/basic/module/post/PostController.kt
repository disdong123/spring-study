package kr.disdong.spring.basic.module.post

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PostController(
    private val postService: PostService
) {

    @GetMapping("/posts")
    fun getPosts(): String {
        return postService.getPosts()
    }
}
