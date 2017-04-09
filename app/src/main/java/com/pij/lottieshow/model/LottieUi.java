package com.pij.lottieshow.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import java.io.File;

/**
 * A dummy item representing a piece of content.
 */
@AutoValue
public abstract class LottieUi implements Parcelable {

    public static LottieUi create(LottieFile model) {
        return create(model.id(), model.label(), model.content());
    }

    public static LottieUi create(File id, String content, String label) {
        return new AutoValue_LottieUi(id, label, content);
    }

    public abstract File id();

    public abstract String label();

    public abstract String content();

}
