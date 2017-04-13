package com.pij.lottieshow.interactor;

import com.pij.lottieshow.model.LottieFile;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.File;

import rx.observers.TestSubscriber;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;
import static rx.Observable.just;
import static rx.Observable.never;

/**
 * <p>Created on 10/04/2017.</p>
 * @author Pierrejean
 */
public class DirectorySourceTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    File mockRoot;

    @Test
    public void emitsEmptyListWhenRootDoesNotEmit() {
        when(mockRoot.isDirectory()).thenReturn(false);
        DirectorySource sut = new DirectorySource(never());
        TestSubscriber<Iterable<LottieFile>> subscriber = TestSubscriber.create();

        sut.lottieFiles().subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(emptyList());
    }

    @Test
    public void emitsEmptyListWhenRootIsNotDirectory() {
        when(mockRoot.isDirectory()).thenReturn(false);
        DirectorySource sut = createDefaultSut(mockRoot);
        TestSubscriber<Iterable<LottieFile>> subscriber = TestSubscriber.create();

        sut.lottieFiles().subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(emptyList());
    }

    @Test
    public void emitsEmptyListWhenDirectoryEmpty() {
        when(mockRoot.isDirectory()).thenReturn(true);
        when(mockRoot.listFiles()).thenReturn(new File[0]);
        DirectorySource sut = createDefaultSut(mockRoot);
        TestSubscriber<Iterable<LottieFile>> subscriber = TestSubscriber.create();

        sut.lottieFiles().subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(emptyList());
    }

    @Test
    public void emitsSingletonListWhenDirectoryContainsOneFile() {
        when(mockRoot.isDirectory()).thenReturn(true);
        when(mockRoot.listFiles()).thenReturn(new File[]{ new File("zip", "zap") });
        DirectorySource sut = createDefaultSut(mockRoot);
        TestSubscriber<Iterable<LottieFile>> subscriber = TestSubscriber.create();

        sut.lottieFiles().subscribe(subscriber);

        subscriber.assertNoErrors();
        //noinspection unchecked
        subscriber.assertValues(emptyList(), singletonList(LottieFile.create(new File("zip", "zap"))));
    }

    private DirectorySource createDefaultSut(File root) {
        return new DirectorySource(just(root));
    }
}