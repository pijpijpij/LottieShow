package com.pij.lottieshow.model;

import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import org.apache.commons.lang3.StringUtils;

import java.net.URI;

/**
 * A dummy item representing a piece of content.
 */
@AutoValue
public abstract class LottieUi implements Parcelable {

    public static LottieUi create(LottieFile model) {
        String shortName = StringUtils.isEmpty(model.label()) ? defaultLabel(model.id()) : model.label();
        return create(Uri.parse(model.id().toString()), shortName);
    }

    @NonNull
    private static String defaultLabel(URI id) {
        String path = id.getPath();
        int lastSlash = path.lastIndexOf('/');
        String name = path.substring(lastSlash + 1);
        int extension = name.lastIndexOf(".json");
        return name.substring(0, extension > 0 ? extension : name.length());
    }

    public static LottieUi create(Uri id, String label) {
        return new AutoValue_LottieUi(id, label);
    }

    public abstract Uri id();

    public abstract String label();

}
