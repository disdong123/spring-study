package kr.disdong.spring.db.sql.function.store

import com.querydsl.core.annotations.QueryProjection
import java.math.BigDecimal

data class StoreQDto @QueryProjection constructor(
    val name: String,
    val distance: BigDecimal,
)
