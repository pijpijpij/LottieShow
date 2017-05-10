package com.pij.lottieshow.interactor

import com.pij.lottieshow.model.LottieFile
import com.pij.lottieshow.model.toLottie
import org.apache.commons.collections4.IterableUtils
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit
import rx.Observable.just
import rx.observers.TestSubscriber
import java.io.File
import java.util.Arrays.asList
import java.util.Collections.emptyList

/**
 *
 * Created on 09/04/2017.
 * @author Pierrejean
 */

class SourceFunnelTest {

    @get:Rule var mockito = MockitoJUnit.rule()

    @Test fun `A single source with an empty list emit an empty list`() {
        val lottieSource = LottieSource { just(emptyList<LottieFile>()) }
        val sut = createDefaultSut(listOf(lottieSource))
        val subscriber = TestSubscriber.create<List<LottieFile>>()

        sut.lottieFiles().map { IterableUtils.toList(it) }.subscribe(subscriber)

        subscriber.assertNoErrors()
        subscriber.assertValue(emptyList<LottieFile>())
    }

    @Test fun `A single source with a singleton list emits a singleton list`() {
        val lottie = File("parent", "label").toURI().toLottie()
        val lottieSource = LottieSource { just(listOf(lottie)) }
        val sut = createDefaultSut(listOf(lottieSource))
        val subscriber = TestSubscriber.create<List<LottieFile>>()

        sut.lottieFiles().map { IterableUtils.toList(it) }.subscribe(subscriber)

        subscriber.assertNoErrors()
        subscriber.assertValue(listOf(File("parent", "label").toURI().toLottie()))
    }

    @Test fun `2 sources, one empty, one singleton emits a singleton list`() {
        val lottieSource1 = LottieSource { just(emptyList<LottieFile>()) }
        val lottie = File("parent", "label").toURI().toLottie()
        val lottieSource2 = LottieSource { just(listOf(lottie)) }
        val sut = createDefaultSut(listOf(lottieSource1, lottieSource2))
        val subscriber = TestSubscriber.create<List<LottieFile>>()

        sut.lottieFiles().map { IterableUtils.toList(it) }.subscribe(subscriber)

        subscriber.assertNoErrors()
        subscriber.assertValue(listOf(File("parent", "label").toURI().toLottie()))
    }

    @Test fun `2 singleton sources emits list with 2 items`() {
        val lottie1 = File("parent1", "label1").toURI().toLottie()
        val lottieSource1 = LottieSource { just(listOf(lottie1)) }
        val lottie2 = File("parent2", "label2").toURI().toLottie()
        val lottieSource2 = LottieSource { just(listOf(lottie2)) }
        val sut = createDefaultSut(listOf(lottieSource1, lottieSource2))
        val subscriber = TestSubscriber.create<List<LottieFile>>()

        sut.lottieFiles().map { IterableUtils.toList(it) }.subscribe(subscriber)

        subscriber.assertNoErrors()
        subscriber.assertValue(asList(File("parent1", "label1").toURI().toLottie(),
                                      File("parent2", "label2").toURI().toLottie()))
    }

    private fun createDefaultSut(lottieSources: List<LottieSource>): SourceFunnel {
        return SourceFunnel(lottieSources)
    }
}
