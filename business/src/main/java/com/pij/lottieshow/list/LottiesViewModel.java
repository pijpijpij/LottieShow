package com.pij.lottieshow.list;

import android.support.annotation.NonNull;

import com.pij.lottieshow.interactor.LottieSink;
import com.pij.lottieshow.interactor.LottieSource;
import com.pij.lottieshow.model.LottieFile;

import rx.Observable;
import rx.subjects.PublishSubject;

import static rx.Observable.defer;

public class LottiesViewModel {

    private final Observable<Iterable<LottieFile>> lotties;
    private final LottieSink sink;
    private final PublishSubject<LottieFile> shouldShowLottie = PublishSubject.create();

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
    public void addLottie(LottieFile newFile) {
        sink.add(newFile);
    }

    @SuppressWarnings("WeakerAccess")
    public void select(LottieFile lottie) {
        shouldShowLottie.onNext(lottie);
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<LottieFile> shouldShowLottie() {
        return shouldShowLottie;
    }

    @SuppressWarnings("WeakerAccess")
    public void loadContent(LottieFile file) {
        System.err.println("loadContent([file]) not implemented yet");

    }
}
