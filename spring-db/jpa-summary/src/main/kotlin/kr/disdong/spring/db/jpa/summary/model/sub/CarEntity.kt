package kr.disdong.spring.db.jpa.summary.model.sub

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository

@Entity(name = "car")
class CarEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column
    var name: String,
)

interface CarRepository : JpaRepository<CarEntity, Long>, CustomCarRepository

interface CustomCarRepository {
    fun findByName(name: String): CarEntity?
}

class CustomCarRepositoryImpl(
    private val subJpaQueryFactory: JPAQueryFactory,
) : CustomCarRepository {
    override fun findByName(name: String): CarEntity? {
        return subJpaQueryFactory
            .selectFrom(QCarEntity.carEntity)
            .where(QCarEntity.carEntity.name.eq(name))
            .fetchOne()
    }
}
