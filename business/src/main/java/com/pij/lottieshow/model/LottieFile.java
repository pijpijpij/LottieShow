package com.pij.lottieshow.model;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.io.File;
import java.net.URI;

import rx.Single;

import static rx.Single.just;

@AutoValue
public abstract class LottieFile {

    private Single<String> content;

    public static LottieFile create(LottieFile source, String content) {
        return create(source, just(content));
    }

    public static LottieFile create(LottieFile source, @NonNull Single<String> content) {
        return create(source.id(), source.label(), content);
    }

    public static LottieFile create(File id) {
        return create(id.toURI());
    }

    public static LottieFile create(URI id) {
        return create(id, null);
    }

    public static LottieFile create(URI id, String label) {
        return create(id, label, (String)null);
    }

    public static LottieFile create(URI id, String label, String content) {
        return create(id, label, just(content));
    }

    public static LottieFile create(URI id, String label, @NonNull Single<String> content) {
        LottieFile result = new AutoValue_LottieFile(id, label);
        result.content = content;
        return result;
    }

    public abstract URI id();

    @Nullable
    public abstract String label();

    @NonNull
    public final Single<String> content() {
        return content;
    }
}
