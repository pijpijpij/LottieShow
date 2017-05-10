package com.pij.lottieshow.interactor;

import com.pij.lottieshow.model.LottieFile;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

import static java.util.Collections.singletonList;
import static rx.Observable.just;

/**
 * <p>Created on 08/04/2017.</p>
 * @author Pierrejean
 */

@Module
public abstract class BuildTypeModule {

    @Provides
    @IntoSet
    static LottieSource provideTestLottieSource() {
        return () -> just(singletonList(LottieFile.Companion.create(new File("you should see", "at least that one"))));
    }

}
