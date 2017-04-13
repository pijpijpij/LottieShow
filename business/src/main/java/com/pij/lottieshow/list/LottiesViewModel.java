package com.pij.lottieshow.list;

import android.support.annotation.NonNull;

import com.pij.lottieshow.interactor.LottieSink;
import com.pij.lottieshow.interactor.LottieSource;
import com.pij.lottieshow.model.LottieFile;

import java.net.URI;

import rx.Observable;

import static rx.Observable.defer;

public class LottiesViewModel {

    private final Observable<Iterable<LottieFile>> lotties;
    private final LottieSink sink;

    @SuppressWarnings("WeakerAccess")
    public LottiesViewModel(@NonNull LottieSource updatableSources, @NonNull LottieSink sink) {
        this.sink = sink;
        this.lotties = defer(updatableSources::lottieFiles);
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<Iterable<LottieFile>> shouldShowList() {
        return lotties;
    }

    @SuppressWarnings("WeakerAccess")
    public void addLottie(URI newFile) {
        sink.add(LottieFile.create(newFile));
    }
}
