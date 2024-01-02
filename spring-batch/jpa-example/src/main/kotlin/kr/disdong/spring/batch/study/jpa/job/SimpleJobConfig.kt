package kr.disdong.spring.batch.study.jpa.job

import kr.disdong.core.Clogger
import kr.disdong.spring.batch.study.jpa.domain.Member
import kr.disdong.spring.batch.study.jpa.domain.MemberRepository
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.AbstractPagingItemReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.PlatformTransactionManager

class MemberRepositoryItemReader(
    private val memberRepository: MemberRepository,
    pageSize: Int
) : AbstractPagingItemReader<Member>() {
    private val logger = Clogger<MemberRepositoryItemReader>()

    init {
        setPageSize(pageSize)
    }

    override fun doReadPage() {
        if (results == null) {
            results = ArrayList()
        } else {
            results.clear()
        }

        val members = memberRepository.findAll(PageRequest.of(this.page, this.pageSize))
        logger.info("[reader] memberSize: ${members.content.size}")
        results.addAll(members)
    }
}

@Configuration
class SimpleJobConfig(
    private val jobRepository: JobRepository,
    private val platformTransactionManager: PlatformTransactionManager,
    private val memberRepository: MemberRepository,
) {
    private val logger = Clogger<SimpleJobConfig>()
    private val CHUNK_SIZE = 1000
    private val PAGE_SIZE = 2

    @Bean
    fun simpleJob(): Job {
        return JobBuilder("simpleJob", jobRepository)
            .flow(simpleStep())
            .end()
            .incrementer(RunIdIncrementer())
            .build()
    }

    @Bean
    fun simpleStep(): Step {
        return StepBuilder("simpleStep", jobRepository)
            .chunk<Member, Member>(CHUNK_SIZE, platformTransactionManager)
            .reader(MemberRepositoryItemReader(memberRepository, PAGE_SIZE))
            .processor {
                logger.info("[processor] start. member: $it")
                it
            }
            .writer(writer())
            // .writer(writerWithSave())
            .allowStartIfComplete(true)
            .transactionManager(platformTransactionManager)
            .build()
    }

    private fun writer(): ItemWriter<Member> {
        logger.info("[writer] start")
        return ItemWriter<Member> { list: Chunk<out Member> ->
            for (member in list.items) {
                logger.info("[writer] current member: $member")
                member.name = member.name + " test"
            }
        }
    }

    private fun writerWithSave(): ItemWriter<Member> {
        logger.info("[writer] start")
        return ItemWriter<Member> { list: Chunk<out Member> ->
            for (member in list.items) {
                logger.info("[writer] current member: $member")
                member.name = member.name + " test"
                memberRepository.save(member)
            }
        }
    }
}
