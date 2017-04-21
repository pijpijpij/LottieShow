package com.pij.lottieshow.detail;

import com.pij.dagger.ActivityScope;
import com.pij.lottieshow.interactor.CompoundSerializer;
import com.pij.lottieshow.interactor.LottieSink;

import dagger.Module;
import dagger.Provides;

/**
 * @author Pierrejean
 */
@Module
abstract class LottieActivityModule {

    @Provides
    @ActivityScope
    static LottieViewModel provideLottieViewModel(LottieSink sink, CompoundSerializer serializer) {
        return new LottieViewModel(sink, serializer);
    }

}
