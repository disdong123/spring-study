package kr.disdong.gradle.multi.module.template.domain.service

import PostDoImpl
import UnsavedPostDoImpl
import kr.disdong.gradle.multi.module.template.domain.repository.PostDoRepository
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postDoRepository: PostDoRepository
) {

    /**
     *
     * @param title
     */
    fun save(title: String): PostDoImpl {
        val post = UnsavedPostDoImpl(title = title)

        return postDoRepository.save(post)
    }
}
