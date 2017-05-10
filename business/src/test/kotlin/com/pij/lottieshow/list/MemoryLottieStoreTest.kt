package com.pij.lottieshow.list

import com.pij.lottieshow.model.LottieFile
import com.pij.lottieshow.model.toLottie
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

        sut.add(File("aa", "bbb").toURI().toLottie())

        subscriber.assertNoErrors()

        subscriber.assertValues(listOf(), listOf(File("aa", "bbb").toURI().toLottie()))
    }

    @Test fun `Emits a list with the latest appended`() {
        val sut = MemoryLottieStore()
        val subscriber = TestSubscriber.create<List<LottieFile>>()
        sut.lottieFiles().map { IterableUtils.toList(it) }.subscribe(subscriber)

        sut.add(File("11", "111").toURI().toLottie())
        sut.add(File("22", "222").toURI().toLottie())

        subscriber.assertNoErrors()

        subscriber.assertValues(listOf(),
                                listOf(File("11", "111").toURI().toLottie()),
                                listOf(File("11", "111").toURI().toLottie(), File("22", "222").toURI().toLottie()))
    }

    @Test fun `Emits the last list for a second subscriber`() {
        val sut = MemoryLottieStore()
        sut.lottieFiles().subscribe()
        sut.add(File("11", "111").toURI().toLottie())
        sut.add(File("22", "222").toURI().toLottie())

        val subscriber = TestSubscriber.create<List<LottieFile>>()
        sut.lottieFiles().map { IterableUtils.toList(it) }.subscribe(subscriber)

        subscriber.assertNoErrors()
        subscriber.assertValues(listOf(File("11", "111").toURI().toLottie(), File("22", "222").toURI().toLottie()))
    }
}