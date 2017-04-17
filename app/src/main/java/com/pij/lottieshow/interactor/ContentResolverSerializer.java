package com.pij.lottieshow.interactor;

import android.content.ContentResolver;
import android.net.Uri;

import com.pij.lottieshow.model.LottieFile;

import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.inject.Inject;

import rx.Single;

import static java.nio.charset.Charset.defaultCharset;
import static rx.Single.just;

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
    public Single<String> open(LottieFile input) {
        return just(input).map(LottieFile::id).map(URI::toString).map(Uri::parse).map(this::open);
    }

    private String open(Uri uri) {
        try (InputStream input = contentResolver.openInputStream(uri)) {
            return IOUtils.toString(input, defaultCharset());
        } catch (FileNotFoundException e) {
            throw new UnknownFileException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
