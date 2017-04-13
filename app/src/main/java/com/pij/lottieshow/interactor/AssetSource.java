package com.pij.lottieshow.interactor;

import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import com.pij.lottieshow.model.LottieFile;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

import static org.apache.commons.collections4.IterableUtils.transformedIterable;
import static org.apache.commons.lang3.ArrayUtils.EMPTY_STRING_ARRAY;
import static org.apache.commons.lang3.ArrayUtils.nullToEmpty;
import static rx.Observable.empty;
import static rx.Observable.just;

/**
 * <p>Created on 10/04/2017.</p>
 * @author Pierrejean
 */
class AssetSource implements LottieSource {

    private final AssetManager assetManager;

    @Inject
    AssetSource(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * Always provide an initial sequence, an empty one at worst.
     */
    @Override
    public Observable<Iterable<LottieFile>> lottieFiles() {
        Observable<String> relative = just("samples");
        Observable<String> absolute = relative.map(s -> AssetSerializer.PREFIX + s);
        Observable<Iterable<URI>> files = relative.map(this::list).map(Arrays::asList).zipWith(absolute, this::asURI);
        // TODO specify somewhere how to open this file.
        return files.flatMap(list -> Observable.from(list)
                                               .map(LottieFile::create)
                                               .doOnError(Throwable::printStackTrace)
                                               .onErrorResumeNext(e -> empty())
                                               .toList());
    }

    @NonNull
    private Iterable<URI> asURI(List<String> shortNames, String absoluteFolder) {
        return transformedIterable(shortNames, input -> asURI(input, absoluteFolder));
    }

    @NonNull
    private URI asURI(String shortName, String absoluteFolder) {
        return URI.create(absoluteFolder + "/" + shortName);
    }

    @NonNull
    private String[] list(String folder) {
        try {
            return nullToEmpty(assetManager.list("samples"));
        } catch (IOException e) {
            e.printStackTrace();
            return EMPTY_STRING_ARRAY;
        }
    }

}
