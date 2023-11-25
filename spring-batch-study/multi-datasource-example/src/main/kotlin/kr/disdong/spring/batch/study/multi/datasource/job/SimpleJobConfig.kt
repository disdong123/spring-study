package kr.disdong.spring.batch.study.multi.datasource.job

import kr.disdong.spring.batch.study.multi.datasource.domain.master.Member
import kr.disdong.spring.batch.study.multi.datasource.domain.master.MemberRepository
import kr.disdong.spring.batch.study.multi.datasource.domain.slave.PostRepository
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
        System.err.println("[reader] memberSize: ${members.content.size}")
        results.addAll(members)
    }
}

@Configuration
class SimpleJobConfig(
    private val jobRepository: JobRepository,
    private val platformTransactionManager: PlatformTransactionManager,
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository,
) {
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
                System.err.println("[processor] start. member: $it")
                updatePost()
                it
            }
            .writer(writer())
            .allowStartIfComplete(true)
            .transactionManager(platformTransactionManager)
            .build()
    }

    /**
     * TODO 업데이트가 안되는 이유?
     *
     */
    private fun updatePost() {
        postRepository.findAll()
            .map {
                System.err.println("[processor] updatePost. post: $it")
                it.title = it.title + " test"
                postRepository.save(it)
            }
    }

    private fun writer(): ItemWriter<Member> {
        System.err.println("[writer] start")
        return ItemWriter<Member> { list: Chunk<out Member> ->
            for (member in list.items) {
                System.err.println("[writer] current member: $member")
                member.name = member.name + " test"
            }
        }
    }
}
