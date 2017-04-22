package com.pij.lottieshow.interactor;

import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import com.pij.lottieshow.model.LottieFile;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;

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
    private final Serializer serializer;

    @Inject
    AssetSource(AssetManager assetManager, AssetSerializer serializer) {
        this.assetManager = assetManager;
        this.serializer = serializer;
    }

    /**
     * Always provide an initial sequence, an empty one at worst.
     */
    @Override
    public Observable<Iterable<LottieFile>> lottieFiles() {
        Observable<String> relativeFolder = just("samples");
        Observable<Iterable<String>> shortFilenames = relativeFolder.map(this::listFiles).map(Arrays::asList);
        Observable<String> absoluteFolder = relativeFolder.map(s -> AssetSerializer.PREFIX + s);

        return shortFilenames.flatMap(list -> Observable.combineLatest(absoluteFolder,
                                                                       Observable.from(list),
                                                                       this::absoluteAssertName)
                                                        .map(URI::create)
                                                        .onErrorResumeNext(e -> empty())
                                                        .map(LottieFile::create)
                                                        .flatMapSingle(this::appendContent)
                                                        .toList());
    }

    @NonNull
    private Single<LottieFile> appendContent(LottieFile file) {
        return Single.zip(Single.just(file),
                          Single.just(file).map(LottieFile::id).flatMap(serializer::open),
                          LottieFile::create);
    }

    @NonNull
    private String absoluteAssertName(String absoluteFolder, String shortName) {
        return absoluteFolder + "/" + shortName;
    }

    @NonNull
    private String[] listFiles(String folder) {
        try {
            return nullToEmpty(assetManager.list(folder));
        } catch (IOException e) {
            e.printStackTrace();
            return EMPTY_STRING_ARRAY;
        }
    }

}
