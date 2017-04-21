package com.pij.lottieshow.detail;

import android.support.annotation.Nullable;

import com.pij.lottieshow.interactor.Serializer;
import com.pij.lottieshow.model.LottieFile;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.PublishSubject;

import static rx.Observable.empty;

class LottieViewModel {

    private final Serializer serializer;
    private PublishSubject<LottieFile> lottie = PublishSubject.create();
    private PublishSubject<Throwable> errors = PublishSubject.create();

    @Inject
    @SuppressWarnings("WeakerAccess")
    public LottieViewModel(Serializer serializer) {
        this.serializer = serializer;
    }

    @SuppressWarnings("WeakerAccess")
    public void loadLottie(@Nullable LottieFile newFile) {
        lottie.onNext(newFile);
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<String> showAnimation() {
        return lottie.concatMap(input -> serializer.open(input)
                                                   .toObservable()
                                                   .doOnError(errors::onNext)
                                                   .onErrorResumeNext(empty()));
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<Throwable> showLoadingError() {
        return errors;
    }

}
