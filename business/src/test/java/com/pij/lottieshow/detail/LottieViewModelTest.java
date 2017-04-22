package com.pij.lottieshow.detail;

import com.pij.lottieshow.interactor.LottieSink;
import com.pij.lottieshow.interactor.Serializer;
import com.pij.lottieshow.model.LottieFile;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.net.URI;

import rx.Single;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>Created on 13/04/2017.</p>
 * @author Pierrejean
 */
public class LottieViewModelTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    Serializer mockSerializer;
    @Mock
    LottieSink mockSink;
    @InjectMocks
    LottieViewModel sut;

    @Test
    public void emitsLoadedLottie() {
        when(mockSerializer.open(URI.create("zip.com"))).thenReturn(Single.just("Hello!"));
        sut.showAnimation().subscribe();

        sut.loadLottie(LottieFile.create(URI.create("zip.com")));

        verify(mockSerializer).open(URI.create("zip.com"));
    }

    @Test
    public void readsLoadedLottieOffSerializer() {
        when(mockSerializer.open(URI.create("zip.com"))).thenReturn(Single.just("Hello!"));
        TestSubscriber<String> subscriber = TestSubscriber.create();
        sut.showAnimation().subscribe(subscriber);

        sut.loadLottie(LottieFile.create(URI.create("zip.com")));

        subscriber.assertNoErrors();
        subscriber.assertValue("Hello!");
    }

    @Test
    public void emitsNoAnimationWhenSerializerFails() {
        when(mockSerializer.open(URI.create("zip.com"))).thenReturn(Single.error(new IOException("failed!")));
        TestSubscriber<String> subscriber = TestSubscriber.create();
        sut.showAnimation().subscribe(subscriber);

        sut.loadLottie(LottieFile.create(URI.create("zip.com")));

        subscriber.assertNoErrors();
        subscriber.assertNoValues();
    }

    @Test
    public void emitsAnErrorWhenSerializerFails() {
        when(mockSerializer.open(URI.create("zip.com"))).thenReturn(Single.error(new IOException("failed!")));
        TestSubscriber<Throwable> subscriber = TestSubscriber.create();
        sut.showLoadingError().subscribe(subscriber);
        sut.showAnimation().subscribe();

        sut.loadLottie(LottieFile.create(URI.create("zip.com")));

        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);
    }

    @Test
    public void emitsNoAnimationWhenLoadedLottieIsNull() {
        when(mockSerializer.open(null)).thenReturn(Single.error(new NullPointerException("failed!")));
        TestSubscriber<String> subscriber = TestSubscriber.create();
        sut.showAnimation().subscribe(subscriber);

        sut.loadLottie(null);

        subscriber.assertNoErrors();
        subscriber.assertNoValues();
    }

    @Test
    public void emitsAnErrorWhenLoadedLottieIsNull() {
        when(mockSerializer.open(null)).thenReturn(Single.error(new NullPointerException("failed!")));
        TestSubscriber<Throwable> subscriber = TestSubscriber.create();
        sut.showLoadingError().subscribe(subscriber);
        sut.showAnimation().subscribe();

        sut.loadLottie(null);

        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);
    }

    @Test
    public void addsAddedLottieToSink() {
        sut.showLottie().subscribe();

        sut.addLottie(LottieFile.create(URI.create("zip.com")));

        verify(mockSink).add(LottieFile.create(URI.create("zip.com")));
    }

}