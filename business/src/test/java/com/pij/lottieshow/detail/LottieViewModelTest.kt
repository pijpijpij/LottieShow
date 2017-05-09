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
//    @Before fun setUp() {
//        mockSerializer = mock(Serializer::class.java)
//        mockSink = mock(LottieSink::class.java)
//
//
//        sut = LottieViewModel(mockSink, mockSerializer)
//    }

    @Test
    fun emitsLoadedLottie() {
        `when`(mockSerializer.open(URI.create("zip.com"))).thenReturn(Single.just("Hello!"))
        sut.showAnimation().subscribe()

        sut.loadLottie(LottieFile.create(URI.create("zip.com")))

        verify<Serializer>(mockSerializer).open(URI.create("zip.com"))
    }

    @Test
    fun readsLoadedLottieOffSerializer() {
        `when`(mockSerializer.open(URI.create("zip.com"))).thenReturn(Single.just("Hello!"))
        val subscriber = TestSubscriber.create<String>()
        sut.showAnimation().subscribe(subscriber)

        sut.loadLottie(LottieFile.create(URI.create("zip.com")))

        subscriber.assertNoErrors()
        subscriber.assertValue("Hello!")
    }

    @Test
    fun emitsNoAnimationWhenSerializerFails() {
        `when`(mockSerializer.open(URI.create("zip.com"))).thenReturn(Single.error<String>(IOException("failed!")))
        val subscriber = TestSubscriber.create<String>()
        sut.showAnimation().subscribe(subscriber)

        sut.loadLottie(LottieFile.create(URI.create("zip.com")))

        subscriber.assertNoErrors()
        subscriber.assertNoValues()
    }

    @Test
    fun emitsAnErrorWhenSerializerFails() {
        `when`(mockSerializer.open(URI.create("zip.com"))).thenReturn(Single.error<String>(IOException("failed!")))
        val subscriber = TestSubscriber.create<Throwable>()
        sut.showLoadingError().subscribe(subscriber)
        sut.showAnimation().subscribe()

        sut.loadLottie(LottieFile.create(URI.create("zip.com")))

        subscriber.assertNoErrors()
        subscriber.assertValueCount(1)
    }

    @Test
    fun emitsNoAnimationWhenLoadedLottieIsNull() {
        `when`(mockSerializer.open(null)).thenReturn(Single.error<String>(NullPointerException("failed!")))
        val subscriber = TestSubscriber.create<String>()
        sut.showAnimation().subscribe(subscriber)

        sut.loadLottie(null)

        subscriber.assertNoErrors()
        subscriber.assertNoValues()
    }

    @Test
    fun emitsAnErrorWhenLoadedLottieIsNull() {
        `when`(mockSerializer.open(null)).thenReturn(Single.error<String>(NullPointerException("failed!")))
        val subscriber = TestSubscriber.create<Throwable>()
        sut.showLoadingError().subscribe(subscriber)
        sut.showAnimation().subscribe()

        sut.loadLottie(null)

        subscriber.assertNoErrors()
        subscriber.assertValueCount(1)
    }

    @Test
    fun addsAddedLottieToSink() {
        sut.showLottie().subscribe()

        sut.addLottie(LottieFile.create(URI.create("zip.com")))

        verify<LottieSink>(mockSink).add(LottieFile.create(URI.create("zip.com")))
    }

}