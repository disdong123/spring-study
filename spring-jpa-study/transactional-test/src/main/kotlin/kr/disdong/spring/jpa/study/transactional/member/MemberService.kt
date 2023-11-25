package kr.disdong.spring.jpa.study.transactional.member

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager

@Service
class MemberService(
    val memberRepository: MemberRepository,
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun join() {
        logger.info("join(), ${TransactionSynchronizationManager.getCurrentTransactionName()}")
        MemberEntity(id = 1, name = "test").let(memberRepository::save)
        throw RuntimeException("No rollback because of no @Transactional")
    }
}

@Service
class MemberMixedService(
    val memberRepository: MemberRepository,
    val memberTransactionService: MemberTransactionService,
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun join() {
        logger.info("join(), ${TransactionSynchronizationManager.getCurrentTransactionName()}")
        joinWithTransaction()
    }

    @Transactional
    fun joinWithTransaction() {
        logger.info("joinWithTransaction(), ${TransactionSynchronizationManager.getCurrentTransactionName()}")
        MemberEntity(id = 1, name = "test").let(memberRepository::save)
        throw RuntimeException("No rollback ???")
    }

    @Transactional
    fun joinWithTransactionCalljoinNewTransaction() {
        logger.info("joinWithTransactionCalljoinNewTransaction(), ${TransactionSynchronizationManager.getCurrentTransactionName()}")
        memberTransactionService.joinNewTransaction()
        throw RuntimeException("No rollback ???")
    }
}

@Service
class MemberTransactionService(
    val memberRepository: MemberRepository
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Transactional
    fun joinCallNormalTransaction() {
        logger.info("joinCallNormalTransaction(), ${TransactionSynchronizationManager.getCurrentTransactionName()}")
        joinNormalTransaction()
    }

    @Transactional
    fun joinNormalTransaction() {
        logger.info("joinNormalTransaction(), ${TransactionSynchronizationManager.getCurrentTransactionName()}")
        MemberEntity(id = 1, name = "test").let(memberRepository::save)
        throw RuntimeException("Rollback because of @Transactional")
    }

    @Transactional
    fun joinCallNewTransaction() {
        logger.info("joinCallNewTransaction(), ${TransactionSynchronizationManager.getCurrentTransactionName()}")
        joinNewTransaction()
        throw RuntimeException("Rollback because of @Transactional")
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun joinNewTransaction() {
        logger.info("joinNewTransaction(), ${TransactionSynchronizationManager.getCurrentTransactionName()}")
        MemberEntity(id = 1, name = "test").let(memberRepository::save)
    }
}
