package kr.disdongspring.reactive.test.reactor.test.observer

import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MyPublisher<T>(
    private val elements: Iterable<T>,
    private var executor: Executor? = null,
) : Publisher<T> {

    /**
     * 그냥 subscriber 를 등록하는 register 메서드라 생각하자.
     */
    override fun subscribe(subscriber: Subscriber<in T>) {
        subscriber.onSubscribe(MySubscriptionImpl(subscriber, elements))
    }

    fun async(executor: Executor): MyPublisher<T> {
        this.executor = executor
        return this
    }

    inner class MySubscriptionImpl<T>(
        private val subscriber: Subscriber<T>,
        private val elements: Iterable<T>,
    ) : Subscription {

        private val iterator = elements.iterator()

        override fun request(n: Long) {
            if (executor == null) {
                requestSync(n)
            } else {
                executor!!.execute {
                    requestSync(n)
                }
            }
        }

        override fun cancel() {
            TODO("Not yet implemented")
        }

        private fun requestSync(n: Long) {
            var index = n
            if (index < 1) {
                throw IllegalArgumentException("$subscriber violated the Reactive Streams rule 3.9 by requesting a non-positive number of elements.")
            }

            while (index-- > 0) {
                if (iterator.hasNext()) {
                    subscriber.onNext(iterator.next())
                } else {
                    subscriber.onComplete()
                    break
                }
            }
        }
    }
}

class MySubscriber<T>(
    private val batchSize: Long,
) : Subscriber<T> {
    private var subscription: Subscription? = null
    private var processCount = 0L

    override fun onSubscribe(subscription: Subscription) {
        this.subscription = subscription
        subscription.request(batchSize)
    }

    override fun onNext(p0: T?) {
        if (subscription == null) {
            throw Error()
        }

        println("${Thread.currentThread().name} onNext $p0")
        ++processCount

        if (processCount >= batchSize) {
            processCount = 0
            subscription!!.request(batchSize)
        }
    }

    override fun onError(p0: Throwable?) {
        println("onError")
    }

    override fun onComplete() {
        println("${Thread.currentThread().name} onCompleted")
    }
}

fun test2() {
    val intPub = MyPublisher<Int>(listOf(1, 2, 3, 4, 5))
    val intSub = MySubscriber<Int>(2)
    val stringPub = MyPublisher<String>(listOf("123", "456", "asdf", "qwer", "zcxv", "zxcvdwasf"))
    val stringSub = MySubscriber<String>(2)

    intPub.subscribe(intSub)
    stringPub.async(Executors.newSingleThreadExecutor()).subscribe(stringSub)
}
