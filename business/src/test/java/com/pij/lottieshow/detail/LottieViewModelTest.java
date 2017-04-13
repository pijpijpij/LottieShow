package com.pij.lottieshow.detail;

import com.pij.lottieshow.interactor.Serializer;
import com.pij.lottieshow.model.LottieFile;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;

import rx.Single;
import rx.observers.TestSubscriber;

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
    @InjectMocks
    LottieViewModel sut;

    @Test
    public void readsLottieOffSerializer() {
        when(mockSerializer.open(LottieFile.create(URI.create("zip.com")))).thenReturn(Single.just(new StringReader(
                "Hello!")));
        TestSubscriber<String> subscriber = TestSubscriber.create();
        sut.showAnimation().subscribe(subscriber);

        sut.loadLottie(LottieFile.create(URI.create("zip.com")));

        subscriber.assertNoErrors();
        subscriber.assertValue("Hello!");
    }

    @Test
    public void emitsNoAnimationWhenSerializerFails() {
        setupSerializerFailure("zip.com");
        TestSubscriber<String> subscriber = TestSubscriber.create();
        sut.showAnimation().subscribe(subscriber);

        sut.loadLottie(LottieFile.create(URI.create("zip.com")));

        subscriber.assertNoErrors();
        subscriber.assertNoValues();
    }

    @Test
    public void emitsAnErrorWhenSerializerFails() {
        setupSerializerFailure("zip.com");
        TestSubscriber<Throwable> subscriber = TestSubscriber.create();
        sut.showLoadingError().subscribe(subscriber);
        sut.showAnimation().subscribe();

        sut.loadLottie(LottieFile.create(URI.create("zip.com")));

        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);
    }

    private void setupSerializerFailure(String s) {
        when(mockSerializer.open(LottieFile.create(URI.create(s)))).thenReturn(Single.error(new IOException
                                                                                                    ("failed!")));
    }
}