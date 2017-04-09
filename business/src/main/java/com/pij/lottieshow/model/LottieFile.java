package com.pij.lottieshow.model;


import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.io.File;

@AutoValue
public abstract class LottieFile {

    public static LottieFile create(File id) {
        return create(id, null, id.getPath());
    }

    public static LottieFile create(File id, String content, String label) {
        return new AutoValue_LottieFile(id, label, content);
    }

    public abstract File id();

    public abstract String label();

    @Nullable
    public abstract String content();

}
