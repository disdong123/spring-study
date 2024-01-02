package kr.disdong.spring.db.dbcp.example.module.post

import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository
) {
    fun getPosts(): List<Post> {
        return postRepository.getPosts()
    }
}
