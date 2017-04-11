package com.pij.lottieshow.list;

import android.support.annotation.NonNull;

import com.pij.lottieshow.interactor.LottieSink;
import com.pij.lottieshow.interactor.LottieSource;
import com.pij.lottieshow.model.LottieFile;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Transformer;

import java.net.URI;
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

public class LottiesViewModel {

    private final Observable<Iterable<LottieFile>> lotties;
    private final LottieSink sink;

    @Inject
    @SuppressWarnings("WeakerAccess")
    public LottiesViewModel(@NonNull Iterable<LottieSource> sources, @NonNull LottieSink sink) {
        this.sink = sink;
        FuncN<Iterable<LottieFile>> collector = untypedLists -> {
            @SuppressWarnings("unchecked")
            Transformer<Object, Iterable<LottieFile>> transformer = input -> (Iterable<LottieFile>)input;
            Iterable<Iterable<LottieFile>> allLists = transformedIterable(asList(untypedLists), transformer);
            Collection<LottieFile> result = new ArrayList<>();
            forEach(allLists, input -> addAll(result, input));
            return result;
        };
        Iterable<Observable<Iterable<LottieFile>>> lotties = transformedIterable(sources, LottieSource::getLottieFiles);
        this.lotties = combineLatest(lotties, collector).doOnNext(list -> System.out.println(
                "PJC Emitting " + IterableUtils.size(list) + " items"));
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<Iterable<LottieFile>> shouldShowList() {
        return lotties;
    }

    @SuppressWarnings("WeakerAccess")
    public void addLottie(URI newFile) {
        sink.add(LottieFile.create(newFile));
    }
}
