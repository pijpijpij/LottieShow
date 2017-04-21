package com.pij.lottieshow.detail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pij.lottieshow.interactor.LottieSink;
import com.pij.lottieshow.model.LottieFile;

import rx.Observable;
import rx.subjects.PublishSubject;

import static rx.Observable.empty;
import static rx.Observable.merge;

class LottieActivityViewModel {

    private PublishSubject<LottieFile> loadedLottie = PublishSubject.create();
    private PublishSubject<LottieFile> lottieToAdd = PublishSubject.create();
    private PublishSubject<Throwable> errors = PublishSubject.create();
    private Observable<LottieFile> addedLottie;

    @SuppressWarnings("WeakerAccess")
    public LottieActivityViewModel(@NonNull LottieSink sink) {
        addedLottie = lottieToAdd.asObservable()
                                 .flatMap(file -> Observable.just(file)
                                                            .doOnNext(sink::add)
                                                            .doOnError(errors::onNext)
                                                            .onErrorResumeNext(empty()))
                                 .doOnNext(f -> System.out.println("PJC lottie added " + f));
    }

    @SuppressWarnings("WeakerAccess")
    public void loadLottie(@Nullable LottieFile newFile) {
        loadedLottie.onNext(newFile);
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<Throwable> showLoadingError() {
        return errors;
    }

    @SuppressWarnings("WeakerAccess")
    public void addLottie(@NonNull LottieFile newFile) {
        lottieToAdd.onNext(newFile);
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<LottieFile> shouldShowLottie() {
        return merge(loadedLottie, addedLottie).doOnNext(f -> System.out.println("PJC shouldShowLottie " + f));
    }

}
