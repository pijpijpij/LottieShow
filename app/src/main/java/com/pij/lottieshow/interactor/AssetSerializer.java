package com.pij.lottieshow.interactor;

import android.content.res.AssetManager;
import android.support.annotation.NonNull;

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

class AssetSerializer implements Serializer {

    static final String PREFIX = "file:///android_asset/";

    private final AssetManager assets;

    @Inject
    AssetSerializer(AssetManager assets) {
        this.assets = assets;
    }

    @Override
    public Single<String> open(URI input) {
        return just(input).map(URI::toString).map(this::assetFileName).map(this::open);
    }

    @NonNull
    private String assetFileName(String id) {
        if (!id.startsWith(PREFIX)) {
            throw new UnknownFileException(id);
        }
        return id.replace(PREFIX, "");

    }

    private String open(String asset) {
        try (InputStream input = assets.open(asset)) {
            return IOUtils.toString(input, defaultCharset());
        } catch (FileNotFoundException e) {
            throw new UnknownFileException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
