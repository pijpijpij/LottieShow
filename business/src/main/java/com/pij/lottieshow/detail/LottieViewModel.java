package com.pij.lottieshow.detail;

import com.pij.lottieshow.interactor.Serializer;
import com.pij.lottieshow.model.LottieFile;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.Reader;

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
    public void loadLottie(LottieFile newFile) {
        lottie.onNext(newFile);
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<String> showAnimation() {
        return lottie.flatMap(input -> serializer.open(input)
                                                 .map(this::read)
                                                 .toObservable()
                                                 .doOnError(errors::onNext)
                                                 .onErrorResumeNext(empty()));
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<Throwable> showLoadingError() {
        return errors;
    }

    private String read(Reader input) {
        try {
            return IOUtils.toString(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
