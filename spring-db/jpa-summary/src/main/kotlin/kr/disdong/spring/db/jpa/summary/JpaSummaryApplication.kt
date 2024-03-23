package kr.disdong.spring.db.jpa.summary

import kr.disdong.spring.db.jpa.summary.model.main.MemberRepository
import kr.disdong.spring.db.jpa.summary.model.main.TestService
import kr.disdong.spring.db.jpa.summary.model.sub.CarRepository
import kr.disdong.spring.db.jpa.summary.model.sub.CarService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JpaSummaryApplication(
    private val testService: TestService,
    private val carRepository: CarRepository,
    private val memberRepository: MemberRepository,
    private val carService: CarService,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        println("Hello, JPA!")
        // println(testService.create())
        //
        // println(testService.getOneWithNo())
        // println(testService.getOneWithMaster())
        // println(testService.getOneWithSlave())
    }
}

fun main(args: Array<String>) {
    runApplication<JpaSummaryApplication>(*args)
}
