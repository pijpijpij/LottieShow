package com.pij.lottieshow.detail;

import com.pij.lottieshow.model.LottieFile;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.PublishSubject;

import static rx.Observable.empty;
import static rx.Observable.just;

class LottieViewModel {

    private PublishSubject<LottieFile> lottie = PublishSubject.create();
    private PublishSubject<Throwable> errors = PublishSubject.create();

    @Inject
    @SuppressWarnings("WeakerAccess")
    public LottieViewModel() {
    }

    @SuppressWarnings("WeakerAccess")
    public void loadLottie(LottieFile newFile) {
        lottie.onNext(newFile);
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<String> showAnimation() {
        return lottie.map(LottieFile::id)
                     .flatMap(input -> just(read(input)).doOnError(errors::onNext).onErrorResumeNext(empty()));
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<Throwable> showLoadingError() {
        return errors;
    }

    private String read(URI input) {
        try {
            return IOUtils.toString(input, Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
