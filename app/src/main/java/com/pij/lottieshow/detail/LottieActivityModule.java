package com.pij.lottieshow.detail;

import com.pij.dagger.ActivityScope;
import com.pij.lottieshow.interactor.CompoundSerializer;
import com.pij.lottieshow.interactor.LottieSink;
import com.pij.lottieshow.interactor.SourceFunnel;

import dagger.Module;
import dagger.Provides;

/**
 * @author Pierrejean
 */
@Module
abstract class LottieActivityModule {

    @Provides
    @ActivityScope
    static LottieActivityViewModel provideActivityLottieViewModel(CompoundSerializer serializer, SourceFunnel sources,
                                                                  LottieSink sink) {
        return new LottieActivityViewModel(sink);
    }

}
