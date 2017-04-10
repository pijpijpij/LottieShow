package com.pij.lottieshow.sources;

import com.pij.lottieshow.list.LottieSource;
import com.pij.lottieshow.model.LottieFile;

import java.io.File;

import rx.Observable;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * <p>Created on 10/04/2017.</p>
 * @author Pierrejean
 */
@SuppressWarnings("WeakerAccess")
public class DirectorySource implements LottieSource {

    private final Observable<File> storageRoot;

    public DirectorySource(Observable<File> storageRoot) {
        this.storageRoot = storageRoot;
    }

    @Override
    public Observable<Iterable<LottieFile>> getLottieFiles() {
        Observable<File[]> files = storageRoot.map(root -> root.isDirectory() ? root.listFiles() : null)
                                              .map(list -> defaultIfNull(list, new File[0]));
        return files.flatMap(list -> Observable.from(list)
                                               .map(LottieFile::create)
                                               .toList()
                                               .doOnError(Throwable::printStackTrace)
                                               .onErrorReturn(e -> emptyList()));
    }

}
