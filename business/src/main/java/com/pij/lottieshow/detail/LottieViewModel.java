package com.pij.lottieshow.detail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pij.lottieshow.interactor.LottieSink;
import com.pij.lottieshow.interactor.Serializer;
import com.pij.lottieshow.model.LottieFile;

import rx.Observable;
import rx.subjects.PublishSubject;

import static rx.Observable.empty;
import static rx.Observable.merge;

class LottieViewModel {

    private final PublishSubject<LottieFile> lottieToLoad = PublishSubject.create();
    private final PublishSubject<LottieFile> lottieToAdd = PublishSubject.create();
    private final PublishSubject<Throwable> errors = PublishSubject.create();
    private final Observable<LottieFile> lottieToShow;
    private final Observable<String> animationToShow;

    @SuppressWarnings("WeakerAccess")
    public LottieViewModel(@NonNull LottieSink sink, @NonNull Serializer serializer) {

        Observable<LottieFile> addedLottie = lottieToAdd.flatMap(file -> Observable.just(file)
                                                                                   .doOnNext(sink::add)
                                                                                   .doOnError(errors::onNext)
                                                                                   .onErrorResumeNext(empty()));
        //noinspection UnnecessaryLocalVariable
        Observable<LottieFile> loadedLottie = lottieToLoad;
        lottieToShow = merge(loadedLottie, addedLottie);

        animationToShow = lottieToShow.map(file -> file == null ? null : file.id())
                                      .concatMap(input -> serializer.open(input)
                                                                    .toObservable()
                                                                    .doOnError(errors::onNext)
                                                                    .onErrorResumeNext(empty()));
    }

    @SuppressWarnings("WeakerAccess")
    public void loadLottie(@Nullable LottieFile newFile) {
        lottieToLoad.onNext(newFile);
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<String> showAnimation() {
        return animationToShow;
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
    public Observable<LottieFile> showLottie() {
        return lottieToShow;
    }

}
