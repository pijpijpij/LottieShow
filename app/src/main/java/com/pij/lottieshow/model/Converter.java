package com.pij.lottieshow.model;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.pij.lottieshow.interactor.LottieSource;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Single;

import static rx.Observable.defer;
import static rx.Single.just;

/**
 * <p>Created on 12/04/2017.</p>
 * @author Pierrejean
 */

public class Converter {

    private final Observable<Map<LottieUi, LottieFile>> files;

    private static LottieUi create(LottieFile model) {
        String shortName = StringUtils.isEmpty(model.label()) ? defaultLabel(model.id()) : model.label();
        return LottieUi.create(Uri.parse(model.id().toString()), shortName);
    }

    @NonNull
    private static String defaultLabel(URI id) {
        String path = id.getPath();
        int lastSlash = path.lastIndexOf('/');
        String name = path.substring(lastSlash + 1);
        int extension = name.lastIndexOf(".json");
        return name.substring(0, extension > 0 ? extension : name.length());
    }

    public Converter(LottieSource source) {
        files = defer(source::lottieFiles).map(this::mapFromModel);
    }


    @NonNull
    public Single<LottieUi> fromModel(@NonNull LottieFile file) {
        return just(file).map(Converter::create);
    }

    @NonNull
    public Single<LottieFile> toModel(@NonNull LottieUi ui) {
        return just(ui).flatMap(i -> files.map(map -> map.get(i)).take(1).filter(item -> item != null).toSingle());
    }

    @NonNull
    private Map<LottieUi, LottieFile> mapFromModel(Iterable<LottieFile> list) {
        Map<LottieUi, LottieFile> result = new HashMap<>();
        MapUtils.populateMap(result, list, Converter::create);
        return result;
    }
}
