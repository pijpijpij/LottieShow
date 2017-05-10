package com.pij.lottieshow.list

import com.pij.lottieshow.interactor.LottieSink
import com.pij.lottieshow.interactor.LottieSource
import com.pij.lottieshow.model.LottieFile
import com.pij.lottieshow.model.toLottie
import org.apache.commons.collections4.IterableUtils
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
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

class LottiesViewModelTest {

    @get:Rule var mockito = MockitoJUnit.rule()

    @Mock lateinit var mockSink: LottieSink
    @Mock lateinit var mockSource: LottieSource
    @InjectMocks lateinit var sut: LottiesViewModel

    @Test fun `Emits an empty list when the source has an empty list`() {
        `when`(mockSource.lottieFiles()).thenReturn(just(listOf()))
        val subscriber = TestSubscriber.create<List<LottieFile>>()

        sut.shouldShowList().map { IterableUtils.toList(it) }.subscribe(subscriber)

        subscriber.assertNoErrors()
        subscriber.assertValue(emptyList<LottieFile>())
    }

    @Test fun `Emits a singleton list when the source emimts a list with a single item`() {
        val lottie = File("parent", "label").toURI().toLottie()
        `when`(mockSource.lottieFiles()).thenReturn(just(listOf(lottie)))
        val subscriber = TestSubscriber.create<List<LottieFile>>()

        sut.shouldShowList().map { IterableUtils.toList(it) }.subscribe(subscriber)

        subscriber.assertNoErrors()
        subscriber.assertValue(listOf(File("parent", "label").toURI().toLottie()))
    }

    @Test fun `Emits a 2 item list when the source emits a 2 item list`() {
        val lottie1 = File("parent1", "label1").toURI().toLottie()
        val lottie2 = File("parent2", "label2").toURI().toLottie()
        `when`(mockSource.lottieFiles()).thenReturn(just(listOf(lottie1, lottie2)))
        val subscriber = TestSubscriber.create<List<LottieFile>>()

        sut.shouldShowList().map { IterableUtils.toList(it) }.subscribe(subscriber)

        subscriber.assertNoErrors()
        subscriber.assertValue(asList(File("parent1", "label1").toURI().toLottie(),
                                      File("parent2", "label2").toURI().toLottie()))
    }

}
