package kr.disdong.spring.basic.module.post

import org.springframework.stereotype.Service

@Service
class PostService {

    fun getPosts(): String {
        return "posts"
    }
}
