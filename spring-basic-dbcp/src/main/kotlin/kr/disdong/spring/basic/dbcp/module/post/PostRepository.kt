package kr.disdong.spring.basic.dbcp.module.post

interface PostRepository {
    fun getPosts(): List<Post>
}
