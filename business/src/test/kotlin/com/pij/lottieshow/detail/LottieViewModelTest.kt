package com.pij.lottieshow.detail

import com.pij.lottieshow.interactor.LottieSink
import com.pij.lottieshow.interactor.Serializer
import com.pij.lottieshow.model.LottieFile
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import rx.Single
import rx.observers.TestSubscriber
import java.io.IOException
import java.net.URI

/**
 *
 * Created on 13/04/2017.
 * @author Pierrejean
 */
class LottieViewModelTest {

    @get:Rule var mockito = MockitoJUnit.rule()

    @Mock lateinit var mockSerializer: Serializer
    @Mock lateinit var mockSink: LottieSink

    @InjectMocks lateinit var sut: LottieViewModel

    @Test fun `Emits loaded Lottie`() {
        `when`(mockSerializer.open(URI.create("zip.com"))).thenReturn(Single.just("Hello!"))
        sut.showAnimation().subscribe()

        sut.loadLottie(LottieFile.create(URI.create("zip.com")))

        verify<Serializer>(mockSerializer).open(URI.create("zip.com"))
    }

    @Test fun `Reads loaded Lottie off the Serializer`() {
        `when`(mockSerializer.open(URI.create("zip.com"))).thenReturn(Single.just("Hello!"))
        val subscriber = TestSubscriber.create<String>()
        sut.showAnimation().subscribe(subscriber)

        sut.loadLottie(LottieFile.create(URI.create("zip.com")))

        subscriber.assertNoErrors()
        subscriber.assertValue("Hello!")
    }

    @Test fun `Emits no animation when Serializer fails`() {
        `when`(mockSerializer.open(URI.create("zip.com"))).thenReturn(Single.error<String>(IOException("failed!")))
        val subscriber = TestSubscriber.create<String>()
        sut.showAnimation().subscribe(subscriber)

        sut.loadLottie(LottieFile.create(URI.create("zip.com")))

        subscriber.assertNoErrors()
        subscriber.assertNoValues()
    }

    @Test fun `Emits an error when the Serializer fails`() {
        `when`(mockSerializer.open(URI.create("zip.com"))).thenReturn(Single.error<String>(IOException("failed!")))
        val subscriber = TestSubscriber.create<Throwable>()
        sut.showLoadingError().subscribe(subscriber)
        sut.showAnimation().subscribe()

        sut.loadLottie(LottieFile.create(URI.create("zip.com")))

        subscriber.assertNoErrors()
        subscriber.assertValueCount(1)
    }

    @Test fun `Emits no animation when loaded Lottie is null`() {
        `when`(mockSerializer.open(null)).thenReturn(Single.error<String>(NullPointerException("failed!")))
        val subscriber = TestSubscriber.create<String>()
        sut.showAnimation().subscribe(subscriber)

        sut.loadLottie(null)

        subscriber.assertNoErrors()
        subscriber.assertNoValues()
    }

    @Test fun `Emits an error when loaded Lottie is null`() {
        `when`(mockSerializer.open(null)).thenReturn(Single.error<String>(NullPointerException("failed!")))
        val subscriber = TestSubscriber.create<Throwable>()
        sut.showLoadingError().subscribe(subscriber)
        sut.showAnimation().subscribe()

        sut.loadLottie(null)

        subscriber.assertNoErrors()
        subscriber.assertValueCount(1)
    }

    @Test fun `Adds added Lottie to the Sink`() {
        sut.showLottie().subscribe()

        sut.addLottie(LottieFile.create(URI.create("zip.com")))

        verify<LottieSink>(mockSink).add(LottieFile.create(URI.create("zip.com")))
    }

}