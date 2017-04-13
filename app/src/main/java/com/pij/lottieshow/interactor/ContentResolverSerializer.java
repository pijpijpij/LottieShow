package com.pij.lottieshow.interactor;

import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.pij.lottieshow.model.LottieFile;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;

import javax.inject.Inject;

import rx.Single;

import static java.nio.charset.Charset.defaultCharset;
import static rx.Single.just;
import static rx.Single.using;

/**
 * <p>Created on 13/04/2017.</p>
 * @author Pierrejean
 */

public class ContentResolverSerializer implements Serializer {

    private final ContentResolver contentResolver;

    @Inject
    public ContentResolverSerializer(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @Override
    public Single<Reader> open(LottieFile input) {
        return just(input).map(LottieFile::id)
                          .map(URI::toString)
                          .map(Uri::parse)
                          .flatMap(filename -> using(() -> open(filename), this::asReader, IOUtils::closeQuietly));
    }

    @NonNull
    private Single<InputStreamReader> asReader(InputStream stream) {
        return just(stream).zipWith(just(defaultCharset()), InputStreamReader::new);
    }

    private InputStream open(Uri uri) {
        try {
            return contentResolver.openInputStream(uri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
