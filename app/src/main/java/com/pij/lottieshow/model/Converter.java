package com.pij.lottieshow.model;

import android.support.annotation.NonNull;

import com.pij.lottieshow.interactor.LottieSource;

import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Single;

import static rx.Observable.just;

/**
 * <p>Created on 12/04/2017.</p>
 * @author Pierrejean
 */

public class Converter {

    private final Observable<Iterable<LottieFile>> sources;

    public Converter(LottieSource source) {
        this.sources = source.lottieFiles();
    }


    @NonNull
    public LottieUi fromModel(LottieFile file) {
        return LottieUi.create(file);
    }

    @NonNull
    public Single<LottieFile> toModel(LottieUi ui) {
        Observable<Map<LottieUi, LottieFile>> files = sources.map(this::mapFromModel);
        return just(ui).withLatestFrom(files, (value, map) -> map.get(value)).toSingle();
    }

    @NonNull
    private Map<LottieUi, LottieFile> mapFromModel(Iterable<LottieFile> list) {
        Map<LottieUi, LottieFile> result = new HashMap<>();
        MapUtils.populateMap(result, list, LottieUi::create);
        return result;
    }
}
