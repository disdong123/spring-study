package kr.disdong.spring.db.jpa.summary.model.main

import kr.disdong.spring.db.jpa.summary.model.sub.CarEntity
import kr.disdong.spring.db.jpa.summary.model.sub.CarRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TestService(
    private val carRepository: CarRepository,
    private val memberRepository: MemberRepository
) {

    @Transactional
    fun noTxManager(changeName: String) {
        val member = memberRepository.save(MemberEntity(name = "member"))
        member.name = changeName

        val car = carRepository.save(CarEntity(name = "car"))
        car.name = changeName
    }

    @Transactional
    fun masterTxManager(changeName: String) {

        val member = memberRepository.save(MemberEntity(name = "member"))
        member.name = changeName

        val car = carRepository.save(CarEntity(name = "car"))
        car.name = changeName
    }

    @Transactional(readOnly = true)
    fun slaveTxManager(changeName: String) {

        val member = memberRepository.save(MemberEntity(name = "member"))
        member.name = changeName

        val car = carRepository.save(CarEntity(name = "car"))
        car.name = changeName
    }

    @Transactional("subTransactionManager")
    fun subTxManager(changeName: String) {

        val member = memberRepository.save(MemberEntity(name = "member"))
        member.name = changeName

        val car = carRepository.save(CarEntity(name = "car"))
        car.name = changeName
    }
}
