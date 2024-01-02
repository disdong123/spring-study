package kr.disdong.spring.jackson.with.reflection

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.util.StopWatch
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

fun test(loopCount: Int, request: () -> Unit) {
    val logger = LoggerFactory.getLogger("test")
    val functionTimer = StopWatch()

    functionTimer.start()
    for (i in 1..loopCount) {
        request()
    }
    functionTimer.stop()

    logger.info("Total: ${functionTimer.totalTimeSeconds}")
}

class PerformanceTest {
    @Test
    fun `멤버변수가 적을 때 reflection 을 사용한 경우`() {
        test(100000) {
            val memberClass: KClass<SmallMember> = SmallMember::class
            val constructor = memberClass.primaryConstructor!!

            val parameters = constructor.parameters.map { parameter ->
                when (parameter.type.classifier) {
                    Int::class -> 0
                    String::class -> ""
                    Double::class -> 0.0
                    else -> throw IllegalArgumentException("Unknown parameter type: ${parameter.type}")
                }
            }

            constructor.call(*parameters.toTypedArray())
        }
    }

    @Test
    fun `멤버변수가 많을 때 reflection 을 사용한 경우`() {
        test(100000) {
            val memberClass: KClass<MediumMember> = MediumMember::class
            val constructor = memberClass.primaryConstructor!!

            val parameters = constructor.parameters.map { parameter ->
                when (parameter.type.classifier) {
                    Int::class -> 0
                    String::class -> ""
                    Double::class -> 0.0
                    else -> throw IllegalArgumentException("Unknown parameter type: ${parameter.type}")
                }
            }

            constructor.call(*parameters.toTypedArray())
        }
    }

    @Test
    fun `멤버변수가 매우 많을 때 reflection 을 사용한 경우`() {
        test(100000) {
            val memberClass: KClass<BigMember> = BigMember::class
            val constructor = memberClass.primaryConstructor!!

            val parameters = constructor.parameters.map { parameter ->
                when (parameter.type.classifier) {
                    Int::class -> 0
                    String::class -> ""
                    Double::class -> 0.0
                    else -> throw IllegalArgumentException("Unknown parameter type: ${parameter.type}")
                }
            }

            constructor.call(*parameters.toTypedArray())
        }
    }

    @Test
    fun `reflection 을 사용하지 않은 경우`() {
        test(100000) {
            BigMember(0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0, 0, "", 0.0)
        }
    }
}

data class SmallMember(
    val a1: Int,
    val b1: String,
    val c1: Double,
    val d1: Int,
    val e1: String,
)

data class MediumMember(
    val a1: Int,
    val b1: String,
    val c1: Double,
    val d1: Int,
    val e1: String,
    val f1: Double,
    val g1: Int,
    val h1: String,
    val i1: Double,
    val j1: Int,
    val k1: String,
    val l1: Double,
    val m1: Int,
    val n1: String,
    val o1: Double,
    val p1: Int,
    val q1: String,
    val r1: Double,
    val s1: Int,
    val t1: String,
    val u1: Double,
    val v1: Int,
    val w1: String,
    val x1: Double,
    val y1: Int,
    val z1: String,
    val a2: Double,
    val b2: Int,
    val c2: String,
    val d2: Double,
    val e2: Int,
    val f2: String,
    val g2: Double,
    val h2: Int,
    val i2: String,
    val j2: Double,
    val k2: Int,
    val l2: String,
    val m2: Double,
    val n2: Int,
)

data class BigMember(
    val a1: Int,
    val b1: String,
    val c1: Double,
    val d1: Int,
    val e1: String,
    val f1: Double,
    val g1: Int,
    val h1: String,
    val i1: Double,
    val j1: Int,
    val k1: String,
    val l1: Double,
    val m1: Int,
    val n1: String,
    val o1: Double,
    val p1: Int,
    val q1: String,
    val r1: Double,
    val s1: Int,
    val t1: String,
    val u1: Double,
    val v1: Int,
    val w1: String,
    val x1: Double,
    val y1: Int,
    val z1: String,
    val a2: Double,
    val b2: Int,
    val c2: String,
    val d2: Double,
    val e2: Int,
    val f2: String,
    val g2: Double,
    val h2: Int,
    val i2: String,
    val j2: Double,
    val k2: Int,
    val l2: String,
    val m2: Double,
    val n2: Int,
    val o2: String,
    val p2: Double,
    val q2: Int,
    val r2: String,
    val s2: Double,
    val t2: Int,
    val u2: String,
    val v2: Double,
    val w2: Int,
    val x2: String,
    val y2: Double,
    val z2: Int,
    val a3: String,
    val b3: Double,
    val c3: Int,
    val d3: String,
    val e3: Double,
    val f3: Int,
    val g3: String,
    val h3: Double,
    val i3: Int,
    val j3: String,
    val k3: Double,
    val l3: Int,
    val m3: String,
    val n3: Double,
    val o3: Int,
    val p3: String,
    val q3: Double,
    val r3: Int,
    val s3: String,
    val t3: Double,
    val u3: Int,
    val v3: String,
    val w3: Double,
    val x3: Int,
    val y3: String,
    val z3: Double,
)
