package com.pij.lottieshow.model;

import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

/**
 * A dummy item representing a piece of content.
 */
@AutoValue
public abstract class LottieUi implements Parcelable {

    public static LottieUi create(Uri id, String label, String content) {
        return new AutoValue_LottieUi(id, label, content);
    }

    public abstract Uri id();

    public abstract String label();

    @Nullable
    public abstract String content();

}
