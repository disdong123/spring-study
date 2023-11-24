package kr.disdong.spring.sql.function.study.store

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.disdong.spring.sql.function.study.common.util.SqlFunctionUtil
import org.springframework.data.jpa.repository.JpaRepository

interface StoreRepository : JpaRepository<StoreEntity, Long>, CustomStoreRepository

interface CustomStoreRepository {
    fun ST_DISTANCE_SPHERE(latitude: Double, longitude: Double): List<StoreQDto>
}

class StoreRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomStoreRepository {
    companion object {
        private const val DISTANCE = 1
    }

    private val storeEntity = QStoreEntity.storeEntity

    override fun ST_DISTANCE_SPHERE(latitude: Double, longitude: Double): List<StoreQDto> {
        val distance = SqlFunctionUtil.ST_DISTANCE_SPHERE(
            storeEntity.latitude, storeEntity.longitude, latitude, longitude
        )

        return jpaQueryFactory.select(QStoreQDto(storeEntity.name, distance))
            .from(storeEntity)
            .where(distance.goe(DISTANCE))
            .fetch()
    }
}
