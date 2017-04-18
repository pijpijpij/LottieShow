package com.pij.lottieshow.detail;

import com.pij.dagger.ActivityScope;
import com.pij.lottieshow.interactor.LottieSink;
import com.pij.lottieshow.interactor.SourceFunnel;
import com.pij.lottieshow.list.LottiesViewModel;
import com.pij.lottieshow.list.MemoryLottieStore;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * @author Pierrejean
 */
@Module
abstract class LottieActivityModule {

    @Provides
    @ActivityScope
    static LottiesViewModel provideLottiesViewModel(SourceFunnel funnel, LottieSink sink) {
        return new LottiesViewModel(funnel, sink);
    }

    @Binds
    abstract LottieSink provideMemoryLottieSink(MemoryLottieStore implementation);

}
