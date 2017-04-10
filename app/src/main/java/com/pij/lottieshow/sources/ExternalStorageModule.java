package com.pij.lottieshow.sources;

import com.pij.lottieshow.list.LottieSource;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

import static rx.Observable.just;

/**
 * <p>Created on 08/04/2017.</p>
 * @author Pierrejean
 */

@Module
public abstract class ExternalStorageModule {

    @Provides
    @IntoSet
    static LottieSource provideExternalSource(ExternalStorage storage) {
        return new FileSystemSource(just(storage.getRoot()));
    }
}
