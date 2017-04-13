package com.pij.lottieshow.interactor;

import com.pij.lottieshow.model.LottieFile;

import java.io.File;

import rx.Observable;

import static java.util.Collections.emptyList;
import static rx.Observable.just;

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

    /**
     * Always provide an initial sequence, an empty one at worst.
     */
    @Override
    public Observable<Iterable<LottieFile>> lottieFiles() {
        Observable<File[]> files = storageRoot.map(root -> root.isDirectory() ? root.listFiles() : new File[0]);
        Observable<Iterable<LottieFile>> lotties = files.flatMap(list -> Observable.from(list).map(File::toURI)
                                                                                   .map(LottieFile::create)
                                                                                   .toList()
                                                                                   .doOnError
                                                                                           (Throwable::printStackTrace)
                                                                                   .onErrorReturn(e -> emptyList()));
        return lotties.startWith(just(emptyList())).distinctUntilChanged();
    }

}
