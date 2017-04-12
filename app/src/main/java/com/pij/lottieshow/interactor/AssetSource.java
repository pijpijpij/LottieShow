package com.pij.lottieshow.interactor;

import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import com.pij.lottieshow.model.LottieFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.IterableUtils.transformedIterable;
import static org.apache.commons.lang3.ArrayUtils.EMPTY_STRING_ARRAY;
import static org.apache.commons.lang3.ArrayUtils.nullToEmpty;
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
    public Observable<Iterable<LottieFile>> getLottieFiles() {
        Observable<String> root = just("samples");
        Observable<Iterable<File>> files = root.map(this::list)
                                               .map(Arrays::asList)
                                               .zipWith(root.map(File::new), this::asFiles);
        // TODO specify somewhere how to open this file.
        return files.flatMap(list -> Observable.from(list)
                                               .map(LottieFile::create)
                                               .toList()
                                               .doOnError(Throwable::printStackTrace)
                                               .onErrorReturn(e -> emptyList()));
    }

    @NonNull
    private Iterable<File> asFiles(List<String> shortNames, File folder) {
        return transformedIterable(shortNames, input -> new File(folder, input));
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
