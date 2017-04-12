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

    @Override
    public Observable<Iterable<LottieFile>> getLottieFiles() {
        return store.asObservable().scan(new ArrayList<>(), this::append);
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
