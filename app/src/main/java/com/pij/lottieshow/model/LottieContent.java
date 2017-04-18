package com.pij.lottieshow.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Single;

/**
 * A dummy item representing a piece of content.
 */
@AutoValue
public abstract class LottieContent {

    public static LottieContent create(Single<String> content) {
        return new AutoValue_LottieContent(content.map(LottieContent::asJSON));
    }

    @Nullable
    private static JSONObject asJSON(@Nullable String input) {
        if (input == null) return null;
        try {
            return new JSONObject(input);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Can emit <code>null</code>.
     */
    public abstract Single<JSONObject> content();

    /**
     * @throws NullPointerException if there's not content
     */
    public Single<String> version() {
        return content().map(content -> content.optString("v"));
    }

}
