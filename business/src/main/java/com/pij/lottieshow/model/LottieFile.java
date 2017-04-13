package com.pij.lottieshow.model;


import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.io.File;
import java.net.URI;

@AutoValue
public abstract class LottieFile {

    public static LottieFile create(File id) {
        return create(id.toURI());
    }

    public static LottieFile create(URI id) {
        return create(id, null);
    }

    public static LottieFile create(URI id, String content) {
        return new AutoValue_LottieFile(id, content);
    }

    public abstract URI id();

    @Nullable
    public abstract String content();

}
