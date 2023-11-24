package kr.disdong.spring.sql.function.study.common.util

import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.NumberTemplate
import java.math.BigDecimal

object SqlFunctionUtil {

    fun ST_DISTANCE_SPHERE(latitudeColumn: NumberPath<Double>, longitudeColumn: NumberPath<Double>, latitude: Double, longitude: Double): NumberTemplate<BigDecimal> {
        return Expressions.numberTemplate(
            BigDecimal::class.java,
            "ST_DISTANCE_SPHERE({0}, {1})",
            Expressions.stringTemplate("POINT({0}, {1})", longitudeColumn, latitudeColumn),
            Expressions.stringTemplate("POINT({0}, {1})", longitude, latitude),
        )
    }
}
