package com.pij.lottieshow.sources;

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

/**
 * <p>Created on 10/04/2017.</p>
 * @author Pierrejean
 */
public class FileSystemSourceTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    File mockRoot;

    @Test
    public void emitsEmptyListWhenRootIsNotDirectory() {
        when(mockRoot.isDirectory()).thenReturn(false);
        FileSystemSource sut = createDefaultSut(mockRoot);
        TestSubscriber<Iterable<LottieFile>> subscriber = TestSubscriber.create();

        sut.getLottieFiles().subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(emptyList());
    }

    @Test
    public void emitsEmptyListWhenDirectoryEmpty() {
        when(mockRoot.isDirectory()).thenReturn(true);
        when(mockRoot.listFiles()).thenReturn(new File[0]);
        FileSystemSource sut = createDefaultSut(mockRoot);
        TestSubscriber<Iterable<LottieFile>> subscriber = TestSubscriber.create();

        sut.getLottieFiles().subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(emptyList());
    }

    @Test
    public void emitsSingletonListWhenDirectoryContainsOneFile() {
        when(mockRoot.isDirectory()).thenReturn(true);
        when(mockRoot.listFiles()).thenReturn(new File[]{ new File("zip", "zap") });
        FileSystemSource sut = createDefaultSut(mockRoot);
        TestSubscriber<Iterable<LottieFile>> subscriber = TestSubscriber.create();

        sut.getLottieFiles().subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(singletonList(LottieFile.create(new File("zip", "zap"))));
    }

    private FileSystemSource createDefaultSut(File root) {
        return new FileSystemSource(just(root));
    }
}