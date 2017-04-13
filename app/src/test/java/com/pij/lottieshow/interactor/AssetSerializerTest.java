package com.pij.lottieshow.interactor;

import android.content.res.AssetManager;

import com.pij.lottieshow.model.LottieFile;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;

import rx.observers.TestSubscriber;

import static org.mockito.Mockito.when;

/**
 * <p>Created on 13/04/2017.</p>
 * @author Pierrejean
 */
public class AssetSerializerTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Mock
    AssetManager mockAssets;

    @InjectMocks
    AssetSerializer sut;

    @Test
    public void emitsReaderWhenAssetExists() throws IOException {
        when(mockAssets.list("samples")).thenReturn(new String[]{ "a_file" });
        when(mockAssets.open("samples/a_file")).thenReturn(new ByteArrayInputStream(new byte[]{ 64, 64 }));

        TestSubscriber<Reader> subscriber = TestSubscriber.create();
        sut.open(LottieFile.create(URI.create("file:///android_asset/samples/a_file"))).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);
    }

    @Test
    public void emitsExceptionWhenAssetDoesNotExists() throws IOException {
        when(mockAssets.list("samples")).thenReturn(new String[]{ "a_file" });
        when(mockAssets.open("samples/a_file")).thenThrow(new FileNotFoundException());

        TestSubscriber<Reader> subscriber = TestSubscriber.create();
        sut.open(LottieFile.create(URI.create("file:///android_asset/samples/a_file"))).subscribe(subscriber);

        subscriber.assertError(RuntimeException.class);
    }
}