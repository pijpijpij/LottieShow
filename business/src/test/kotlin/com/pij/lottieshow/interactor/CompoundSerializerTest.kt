package com.pij.lottieshow.interactor

import org.junit.Test
import rx.Single
import rx.observers.TestSubscriber
import java.net.URI

/**
 *
 * Created on 13/04/2017.
 * @author Pierrejean
 */
class CompoundSerializerTest {

    @Test fun `Emits UnknownFileException when empty delegates`() {
        val sut = CompoundSerializer(emptyList())

        val subscriber = TestSubscriber.create<String>()
        sut.open(URI.create("dummy.com")).subscribe(subscriber)

        subscriber.assertError(UnknownFileException::class.java)
    }

    @Test fun `Errors when singleton Serializer fails`() {
        val serializer = Serializer { Single.error<String>(RuntimeException()) }
        val sut = CompoundSerializer(listOf(serializer))

        val subscriber = TestSubscriber.create<String>()
        sut.open(URI.create("dummy.com")).subscribe(subscriber)

        subscriber.assertError(RuntimeException::class.java)
    }

    @Test fun `Emits reader when singleton Serializer succeeds`() {
        val serializer = Serializer { Single.just("whatever") }
        val sut = CompoundSerializer(listOf(serializer))

        val subscriber = TestSubscriber.create<String>()
        sut.open(URI.create("dummy.com")).subscribe(subscriber)

        subscriber.assertNoErrors()
        subscriber.assertValue("whatever")
    }

    @Test fun `Emits a reader when 1st Serializer does not know the file but the second does`() {
        val serializer1 = Serializer { Single.error<String>(UnknownFileException()) }
        val serializer2 = Serializer { Single.just("whatever") }
        val sut = CompoundSerializer(listOf(serializer1, serializer2))

        val subscriber = TestSubscriber.create<String>()
        sut.open(URI.create("dummy.com")).subscribe(subscriber)

        subscriber.assertNoErrors()
        subscriber.assertValue("whatever")
    }

    @Test fun `Emits exception when 1st Serializer fails and 2nd fails for a non-format reason`() {
        val serializer1 = Serializer { Single.error<String>(UnknownFileException()) }
        val serializer2 = Serializer { Single.error<String>(RuntimeException()) }
        val sut = CompoundSerializer(listOf(serializer1, serializer2))

        val subscriber = TestSubscriber.create<String>()
        sut.open(URI.create("dummy.com")).subscribe(subscriber)

        subscriber.assertError(RuntimeException::class.java)
    }

}