package com.pij.lottieshow.interactor

import com.pij.lottieshow.model.LottieFile
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import rx.Observable.just
import rx.Observable.never
import rx.observers.TestSubscriber
import java.io.File
import java.util.Collections.emptyList

/**
 *
 * Created on 10/04/2017.
 * @author Pierrejean
 */
class DirectorySourceTest {

    @get:Rule var mockito = MockitoJUnit.rule()
    @Mock internal lateinit var mockRoot: File

    @Test fun `Emits empty list when root does not emit`() {
        val sut = DirectorySource(never<File>())
        val subscriber = TestSubscriber.create<Iterable<LottieFile>>()

        sut.lottieFiles().subscribe(subscriber)

        subscriber.assertNoErrors()
        subscriber.assertValue(listOf())
    }

    @Test fun `Emits empty list when root is not directory`() {
        `when`(mockRoot.isDirectory).thenReturn(false)
        val sut = createDefaultSut(mockRoot)
        val subscriber = TestSubscriber.create<Iterable<LottieFile>>()

        sut.lottieFiles().subscribe(subscriber)

        subscriber.assertNoErrors()
        subscriber.assertValue(listOf())
    }

    @Test fun `Emits empty list when root is an empty directory`() {
        `when`(mockRoot.isDirectory).thenReturn(true)
        `when`(mockRoot.listFiles()).thenReturn(arrayOfNulls<File>(0))
        val sut = createDefaultSut(mockRoot)
        val subscriber = TestSubscriber.create<Iterable<LottieFile>>()

        sut.lottieFiles().subscribe(subscriber)

        subscriber.assertNoErrors()
        subscriber.assertValue(listOf())
    }

    @Test fun `Emits singleton list when root contains a single file`() {
        `when`(mockRoot.isDirectory).thenReturn(true)
        `when`(mockRoot.listFiles()).thenReturn(arrayOf(File("zip", "zap")))
        val sut = createDefaultSut(mockRoot)
        val subscriber = TestSubscriber.create<Iterable<LottieFile>>()

        sut.lottieFiles().subscribe(subscriber)

        subscriber.assertNoErrors()

        subscriber.assertValues(emptyList<LottieFile>(), listOf(LottieFile.create(File("zip", "zap"))))
    }

    private fun createDefaultSut(root: File): DirectorySource {
        return DirectorySource(just(root))
    }
}