package com.pij.lottieshow.list

import com.pij.lottieshow.model.LottieFile
import org.apache.commons.collections4.IterableUtils
import org.junit.Test
import rx.observers.TestSubscriber
import java.io.File

/**
 *
 * Created on 11/04/2017.
 * @author Pierrejean
 */
class MemoryLottieStoreTest {

    @Test fun `Emits an empty list first`() {
        val sut = MemoryLottieStore()
        val subscriber = TestSubscriber.create<List<LottieFile>>()

        sut.lottieFiles().map { IterableUtils.toList(it) }.subscribe(subscriber)

        subscriber.assertNoErrors()
        subscriber.assertValue(listOf())
    }

    @Test fun `Emits singleton with 1st item after the initial empty list`() {
        val sut = MemoryLottieStore()
        val subscriber = TestSubscriber.create<List<LottieFile>>()
        sut.lottieFiles().map { IterableUtils.toList(it) }.subscribe(subscriber)

        sut.add(LottieFile.create(File("aa", "bbb")))

        subscriber.assertNoErrors()

        subscriber.assertValues(listOf(), listOf(LottieFile.create(File("aa", "bbb"))))
    }

    @Test fun `Emits a list with the latest appended`() {
        val sut = MemoryLottieStore()
        val subscriber = TestSubscriber.create<List<LottieFile>>()
        sut.lottieFiles().map { IterableUtils.toList(it) }.subscribe(subscriber)

        sut.add(LottieFile.create(File("11", "111")))
        sut.add(LottieFile.create(File("22", "222")))

        subscriber.assertNoErrors()

        subscriber.assertValues(listOf(),
                                listOf(LottieFile.create(File("11", "111"))),
                                listOf(LottieFile.create(File("11", "111")), LottieFile.create(File("22", "222"))))
    }

    @Test fun `Emits the last list for a second subscriber`() {
        val sut = MemoryLottieStore()
        sut.lottieFiles().subscribe()
        sut.add(LottieFile.create(File("11", "111")))
        sut.add(LottieFile.create(File("22", "222")))

        val subscriber = TestSubscriber.create<List<LottieFile>>()
        sut.lottieFiles().map { IterableUtils.toList(it) }.subscribe(subscriber)

        subscriber.assertNoErrors()
        subscriber.assertValues(listOf(LottieFile.create(File("11", "111")), LottieFile.create(File("22", "222"))))
    }
}