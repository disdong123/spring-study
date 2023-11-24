package kr.disdong.spring.sql.function.study.store

import java.io.Serializable
import java.math.BigDecimal

class MysqlFunctionAlias {

    companion object {
        @JvmStatic
        fun ST_DISTANCE_SPHERE(start: Point, end: Point): BigDecimal {
            return BigDecimal.ONE
        }

        @JvmStatic
        fun POINT(x: Double, y: Double): Point {
            return Point(x, y)
        }

        fun createStDistanceSphereAlias(): String {
            return """CREATE ALIAS IF NOT EXISTS ST_DISTANCE_SPHERE FOR "kr.disdong.spring.sql.function.study.store.MysqlFunctionAlias.ST_DISTANCE_SPHERE""""
        }

        fun createPointAlias(): String {
            return """CREATE ALIAS IF NOT EXISTS POINT FOR "kr.disdong.spring.sql.function.study.store.MysqlFunctionAlias.POINT""""
        }
    }
}

class Point(val x: Double, val y: Double) : Serializable
