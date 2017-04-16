package com.pij.lottieshow.list;

import android.support.annotation.NonNull;

import com.pij.lottieshow.interactor.LottieSink;
import com.pij.lottieshow.interactor.LottieSource;
import com.pij.lottieshow.model.LottieFile;

import org.apache.commons.collections4.IterableUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * <p>Created on 11/04/2017.</p>
 * @author Pierrejean
 */

public class MemoryLottieStore implements LottieSource, LottieSink {

    private final BehaviorSubject<LottieFile> store = BehaviorSubject.create();
    private final Observable<Iterable<LottieFile>> lotties;

    public MemoryLottieStore() {
        lotties = store.asObservable()
                       .scan(new ArrayList<>(), this::append)
                       .replay(1)
                       .autoConnect()
                       .doOnNext(i -> System.out.println("PJC got " + IterableUtils.size(i) + " items:"))
                       .doOnNext(i -> System.out.println("PJC " + i));
    }

    @Override
    public Observable<Iterable<LottieFile>> lottieFiles() {
        return lotties;
    }

    @Override
    public void add(@NonNull LottieFile lottieFile) {
        store.onNext(lottieFile);
    }

    @NonNull
    private Iterable<LottieFile> append(Iterable<LottieFile> current, LottieFile newFile) {
        List<LottieFile> result = IterableUtils.toList(current);
        result.add(newFile);
        return result;
    }
}
