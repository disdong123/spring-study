package kr.disdong.spring.db.jpa.summary.model.main

import com.zaxxer.hikari.HikariDataSource
import kr.disdong.spring.db.jpa.summary.model.sub.CarEntity
import kr.disdong.spring.db.jpa.summary.model.sub.CarRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager

@Service
class TestService(
    private val carRepository: CarRepository,
    private val memberRepository: MemberRepository
) {

    fun check(size: Int, dbName: String,) {
        val map = TransactionSynchronizationManager.getResourceMap()
        assert((map.keys.size == size))

        map.keys.forEach { key ->
            if (key is HikariDataSource) {
                assert(key.jdbcUrl.contains(dbName))
            }
        }
    }

    @Transactional
    fun noTxManager(changeName: String) {
        check(2, "testdb-master")

        val member = memberRepository.save(MemberEntity(name = "member"))
        member.name = changeName
        check(2, "testdb-master")

        val car = carRepository.save(CarEntity(name = "car"))
        car.name = changeName
        check(2, "testdb-master")
    }

    @Transactional("masterTransactionManager")
    fun masterTxManager(changeName: String) {
        check(2, "testdb-master")

        val member = memberRepository.save(MemberEntity(name = "member"))
        member.name = changeName
        check(2, "testdb-master")

        val car = carRepository.save(CarEntity(name = "car"))
        car.name = changeName
        check(2, "testdb-master")
    }

    @Transactional("slaveTransactionManager", readOnly = true)
    fun slaveTxManager(changeName: String) {
        check(2, "testdb-slave")

        val member = memberRepository.save(MemberEntity(name = "member"))
        member.name = changeName
        check(2, "testdb-slave")

        val car = carRepository.save(CarEntity(name = "car"))
        car.name = changeName
        check(2, "testdb-slave")
    }

    @Transactional("subTransactionManager")
    fun subTxManager(changeName: String) {
        check(2, "testdb-sub")

        val member = memberRepository.save(MemberEntity(name = "member"))
        member.name = changeName
        check(2, "testdb-sub")

        val car = carRepository.save(CarEntity(name = "car"))
        car.name = changeName
        check(2, "testdb-sub")
    }
}
