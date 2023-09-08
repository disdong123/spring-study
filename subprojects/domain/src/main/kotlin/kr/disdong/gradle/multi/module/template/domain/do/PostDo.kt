import kr.disdong.gradle.multi.module.template.domain.jpa.domain.PostEntity

typealias PostId = Long

interface PostDo : UnsavedPostDo {
    val id: PostId
}

interface UnsavedPostDo {
    val title: String
}

data class UnsavedPostDoImpl(
    val entity: PostEntity
) : UnsavedPostDo {
    constructor(title: String) : this(
        PostEntity(title = title)
    )
    override val title: String
        get() = entity.title
}

class PostDoImpl(
    val entity: PostEntity
) : PostDo {
    override val id: PostId
        get() = entity.id!!
    override val title: String
        get() = entity.title
}
