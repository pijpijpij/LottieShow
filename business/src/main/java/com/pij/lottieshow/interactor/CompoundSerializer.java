package com.pij.lottieshow.interactor;

import android.support.annotation.NonNull;

import java.net.URI;
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
    public Single<String> open(URI input) {
        // emits all known formats
        Observable<String> contents = from(serializers).concatMap(serializer -> serializer.open(input)
                                                                                          .toObservable()
                                                                                          .onErrorResumeNext
                                                                                                  (this::absorbUnknownFile));
        return contents.take(1).toSingle().onErrorResumeNext(e -> transformNoSerializerFound(e, input));
    }

    private Single<? extends String> transformNoSerializerFound(Throwable e, URI file) {
        return (e instanceof NoSuchElementException) ? Single.error(new UnknownFileException(
                "Could not find a Serializer for " + file + ".",
                e)) : Single.error(e);
    }

    private Observable<String> absorbUnknownFile(Throwable e) {
        return (e instanceof UnknownFileException) ? empty() : error(e);
    }

}
