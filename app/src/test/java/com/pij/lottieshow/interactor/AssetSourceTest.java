package com.pij.lottieshow.interactor;

import android.content.res.AssetManager;

import com.pij.lottieshow.model.LottieFile;

import org.apache.commons.collections4.IterableUtils;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.File;
import java.io.IOException;
import java.util.List;

import rx.observers.TestSubscriber;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;

/**
 * <p>Created on 11/04/2017.</p>
 * @author Pierrejean
 */
public class AssetSourceTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Mock
    AssetManager mockAssets;

    @InjectMocks
    AssetSource sut;

    @Test
    public void emitsEmptyListWhenNullSampleAssets() throws IOException {
        when(mockAssets.list("samples")).thenReturn(null);
        TestSubscriber<List<LottieFile>> subscriber = TestSubscriber.create();

        sut.getLottieFiles().map(IterableUtils::toList).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(emptyList());
    }

    @Test
    public void emitsEmptyListWhenAssetsThrowsIOException() throws IOException {
        when(mockAssets.list("samples")).thenThrow(new IOException());
        TestSubscriber<List<LottieFile>> subscriber = TestSubscriber.create();

        sut.getLottieFiles().map(IterableUtils::toList).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(emptyList());
    }

    @Test
    public void emitsEmptyListWhenNoSampleAssets() throws IOException {
        when(mockAssets.list("samples")).thenReturn(new String[0]);
        TestSubscriber<List<LottieFile>> subscriber = TestSubscriber.create();

        sut.getLottieFiles().map(IterableUtils::toList).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(emptyList());
    }

    @Test
    public void emitsSingletonListWhenOneSampleAssets() throws IOException {
        when(mockAssets.list("samples")).thenReturn(new String[]{ "a file" });
        TestSubscriber<List<LottieFile>> subscriber = TestSubscriber.create();

        sut.getLottieFiles().map(IterableUtils::toList).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(singletonList(LottieFile.create(new File("samples", "a file"))));
    }
}