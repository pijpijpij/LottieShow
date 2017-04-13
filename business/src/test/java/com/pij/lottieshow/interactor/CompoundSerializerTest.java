package com.pij.lottieshow.interactor;

import com.pij.lottieshow.model.LottieFile;

import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.net.URI;

import rx.Single;
import rx.observers.TestSubscriber;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.apache.commons.collections4.IterableUtils.emptyIterable;

/**
 * <p>Created on 13/04/2017.</p>
 * @author Pierrejean
 */
public class CompoundSerializerTest {

    @Test
    public void emitsUnsupportedFormatIfEmpty() {
        CompoundSerializer sut = new CompoundSerializer(emptyIterable());

        TestSubscriber<Reader> subscriber = TestSubscriber.create();
        sut.open(LottieFile.create(URI.create("dummy.com"))).subscribe(subscriber);

        subscriber.assertError(UnknownFileException.class);
    }

    @Test
    public void emitsExceptionIfSingletonSerializerFails() {
        Serializer serializer = input -> Single.error(new RuntimeException());
        CompoundSerializer sut = new CompoundSerializer(singletonList(serializer));

        TestSubscriber<Reader> subscriber = TestSubscriber.create();
        sut.open(LottieFile.create(URI.create("dummy.com"))).subscribe(subscriber);

        subscriber.assertError(RuntimeException.class);
    }

    @Test
    public void emitsReaderIfSingletonSerializerSucceeds() {
        StringReader reader = new StringReader("whatever");
        Serializer serializer = input -> Single.just(reader);
        CompoundSerializer sut = new CompoundSerializer(singletonList(serializer));

        TestSubscriber<Reader> subscriber = TestSubscriber.create();
        sut.open(LottieFile.create(URI.create("dummy.com"))).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(reader);
    }

    @Test
    public void emitsReaderIfFirstSerializerDoesNotKnownTheFileButSecondDoes() {
        Serializer serializer1 = input -> Single.error(new UnknownFileException());
        StringReader reader = new StringReader("whatever");
        Serializer serializer2 = input -> Single.just(reader);
        CompoundSerializer sut = new CompoundSerializer(asList(serializer1, serializer2));

        TestSubscriber<Reader> subscriber = TestSubscriber.create();
        sut.open(LottieFile.create(URI.create("dummy.com"))).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(reader);
    }

    @Test
    public void emitsExceptionIfFirstSerializerFailsAndSecondFailsForANonFormatReason() {
        Serializer serializer1 = input -> Single.error(new UnknownFileException());
        Serializer serializer2 = input -> Single.error(new RuntimeException());
        CompoundSerializer sut = new CompoundSerializer(asList(serializer1, serializer2));

        TestSubscriber<Reader> subscriber = TestSubscriber.create();
        sut.open(LottieFile.create(URI.create("dummy.com"))).subscribe(subscriber);

        subscriber.assertError(RuntimeException.class);
    }

}