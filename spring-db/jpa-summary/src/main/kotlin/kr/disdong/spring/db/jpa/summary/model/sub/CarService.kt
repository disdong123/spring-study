package kr.disdong.spring.db.jpa.summary.model.sub

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CarService(
    private val carRepository: CarRepository
) {

    fun getOne(): CarEntity? {
        return carRepository.findById(1L).orElse(null)
    }

    @Transactional("subTransactionManager")
    fun create(): CarEntity {
        return carRepository.save(CarEntity(name = "car", id = 1))
    }
}
