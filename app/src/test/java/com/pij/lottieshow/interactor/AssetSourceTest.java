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

import java.io.IOException;
import java.net.URI;
import java.util.List;

import rx.Single;
import rx.observers.TestSubscriber;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
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
    @Mock
    AssetSerializer mockSerializer;
    @InjectMocks
    AssetSource sut;

    @Test
    public void emitsEmptyListWhenNullSampleAssets() throws IOException {
        when(mockAssets.list("samples")).thenReturn(null);
        TestSubscriber<List<LottieFile>> subscriber = TestSubscriber.create();

        sut.lottieFiles().map(IterableUtils::toList).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(emptyList());
    }

    @Test
    public void emitsEmptyListWhenAssetsThrowsIOException() throws IOException {
        when(mockAssets.list("samples")).thenThrow(new IOException());
        TestSubscriber<List<LottieFile>> subscriber = TestSubscriber.create();

        sut.lottieFiles().map(IterableUtils::toList).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(emptyList());
    }

    @Test
    public void emitsEmptyListWhenNoSampleAssets() throws IOException {
        when(mockAssets.list("samples")).thenReturn(new String[0]);
        TestSubscriber<List<LottieFile>> subscriber = TestSubscriber.create();

        sut.lottieFiles().map(IterableUtils::toList).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(emptyList());
    }

    @Test
    public void emitsSingletonListWhenOneSampleAssets() throws IOException {
        when(mockAssets.list("samples")).thenReturn(new String[]{ "a_file" });
        setupSerializerSameContentForAll();
        TestSubscriber<List<LottieFile>> subscriber = TestSubscriber.create();

        sut.lottieFiles().map(IterableUtils::toList).subscribe(subscriber);

        subscriber.assertNoErrors();
        assertThat(subscriber.getOnNextEvents().get(0)).containsExactly(LottieFile.create(URI.create(
                "file:///android_asset/samples/a_file")));
        subscriber.assertValue(singletonList(LottieFile.create(URI.create("file:///android_asset/samples/a_file"))));
    }

    @Test
    public void doesNotSupportSpacesInName() throws IOException {
        when(mockAssets.list("samples")).thenReturn(new String[]{ "a file" });
        setupSerializerSameContentForAll();
        TestSubscriber<List<LottieFile>> subscriber = TestSubscriber.create();

        sut.lottieFiles().map(IterableUtils::toList).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(emptyList());
    }

    private void setupSerializerSameContentForAll() {
        when(mockSerializer.open(any(URI.class))).thenReturn(Single.just("zap!"));
    }
}