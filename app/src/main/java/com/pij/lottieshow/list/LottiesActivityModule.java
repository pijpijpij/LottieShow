package com.pij.lottieshow.list;

import com.pij.dagger.ActivityScope;
import com.pij.lottieshow.interactor.LottieSink;
import com.pij.lottieshow.interactor.SourceFunnel;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * <p>Created on 08/04/2017.</p>
 * @author Pierrejean
 */
@Module
abstract class LottiesActivityModule {

    @Provides
    @ActivityScope
    static LottiesViewModel provideLottiesViewModel(SourceFunnel funnel, LottieSink sink) {
        return new LottiesViewModel(funnel, sink);
    }

    @Binds
    abstract LottieSink provideMemoryLottieSink(MemoryLottieStore implementation);

}
