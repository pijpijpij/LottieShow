package com.pij.lottieshow.model;

import android.net.Uri;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;

/**
 * A dummy item representing a piece of content.
 */
@AutoValue
public abstract class LottieUi implements Parcelable {

    public static LottieUi create(LottieFile model) {
        return create(Uri.parse(model.id().toString()), model.label(), model.content());
    }

    public static LottieUi create(Uri id, String content, String label) {
        return new AutoValue_LottieUi(id, label, content);
    }

    public abstract Uri id();

    public abstract String label();

    public abstract String content();

}
