package com.pij.lottieshow.interactor;

import android.support.annotation.NonNull;

import com.pij.lottieshow.model.LottieFile;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Transformer;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.FuncN;

import static java.util.Arrays.asList;
import static org.apache.commons.collections4.CollectionUtils.addAll;
import static org.apache.commons.collections4.IterableUtils.forEach;
import static org.apache.commons.collections4.IterableUtils.transformedIterable;
import static rx.Observable.combineLatest;

public class SourceFunnel implements LottieSource {

    private final Observable<Iterable<LottieFile>> lotties;

    @Inject
    @SuppressWarnings("WeakerAccess")
    public SourceFunnel(@NonNull Iterable<LottieSource> sources) {
        this.lotties = listInOrder(sources);
    }

    @Override
    public Observable<Iterable<LottieFile>> getLottieFiles() {
        return lotties;
    }

    @NonNull
    private Observable<Iterable<LottieFile>> listInOrder(@NonNull Iterable<LottieSource> sources) {
        FuncN<Iterable<LottieFile>> collector = untypedLists -> {
            @SuppressWarnings("unchecked")
            Transformer<Object, Iterable<LottieFile>> transformer = input -> (Iterable<LottieFile>)input;
            Iterable<Iterable<LottieFile>> allLists = transformedIterable(asList(untypedLists), transformer);
            Collection<LottieFile> result = new ArrayList<>();
            forEach(allLists, input -> addAll(result, input));
            return result;
        };
        Iterable<Observable<Iterable<LottieFile>>> lotties = transformedIterable(sources, LottieSource::getLottieFiles);
        return combineLatest(lotties, collector).doOnNext(list -> System.out.println(
                "PJC Emitting " + IterableUtils.size(list) + " items"));
    }

}
