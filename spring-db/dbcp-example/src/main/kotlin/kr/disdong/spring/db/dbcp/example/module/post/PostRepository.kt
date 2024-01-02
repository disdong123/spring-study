package kr.disdong.spring.db.dbcp.example.module.post

interface PostRepository {
    fun getPosts(): List<Post>
}
