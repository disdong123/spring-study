package kr.disdong.gradle.multi.module.template.domain.repository

import PostDoImpl
import UnsavedPostDoImpl
import kr.disdong.gradle.multi.module.template.domain.jpa.repository.PostEntityRepository
import org.springframework.stereotype.Component

@Component
class PostDoRepository(
    private val postEntityRepository: PostEntityRepository
) {

    fun save(post: UnsavedPostDoImpl): PostDoImpl {
        return PostDoImpl(postEntityRepository.save(post.entity))
    }

    fun hello(): List<PostDoImpl> {
        return postEntityRepository.hello()
            .map {
                PostDoImpl(it)
            }
    }

    fun findAll(): List<PostDoImpl> {
        return postEntityRepository.findAll()
            .map {
                PostDoImpl(it)
            }
    }
}
