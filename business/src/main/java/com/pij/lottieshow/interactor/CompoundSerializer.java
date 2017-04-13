package com.pij.lottieshow.interactor;

import android.support.annotation.NonNull;

import com.pij.lottieshow.model.LottieFile;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.NoSuchElementException;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;

import static rx.Observable.empty;
import static rx.Observable.error;
import static rx.Observable.from;

public class CompoundSerializer implements Serializer {

    private final Iterable<Serializer> serializers;

    @Inject
    @SuppressWarnings("WeakerAccess")
    public CompoundSerializer(@NonNull Iterable<Serializer> sources) {
        this.serializers = sources;
    }

    @Override
    public Single<Reader> open(LottieFile input) {
        // emits all known formats
        Observable<Reader> readers = from(serializers).concatMap(serializer -> serializer.open(input)
                                                                                         .toObservable()
                                                                                         .onErrorResumeNext(this::absorbUnknownFormat)
                                                                                         .onErrorResumeNext
                                                                                                 (this::absorbFileNotFound));
        return readers.take(1).toSingle().onErrorResumeNext(e -> transformNoSerializerFound(e, input));
    }

    private Single<? extends Reader> transformNoSerializerFound(Throwable e, LottieFile file) {
        return (e instanceof NoSuchElementException) ? Single.error(new UnsupportedFormatException(
                "Could not find a Serializer for " + file + ".",
                e)) : Single.error(e);
    }

    private Observable<Reader> absorbUnknownFormat(Throwable e) {
        return (e instanceof UnsupportedFormatException) ? empty() : error(e);
    }

    private Observable<Reader> absorbFileNotFound(Throwable e) {
        return (e instanceof FileNotFoundException) ? empty() : error(e);
    }

}
