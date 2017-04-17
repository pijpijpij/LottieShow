package com.pij.lottieshow.list;

import com.pij.dagger.ActivityScope;
import com.pij.lottieshow.R;
import com.pij.lottieshow.interactor.LottieSink;
import com.pij.lottieshow.interactor.SourceFunnel;
import com.pij.lottieshow.model.Converter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * <p>Created on 08/04/2017.</p>
 * @author Pierrejean
 */
@Module
abstract class LottieListActivityModule {

    @Provides
    @ActivityScope
    static LottieAdapter provideLottieAdapter(LottiesViewModel viewModel, Converter converter) {
        return new LottieAdapter(R.layout.lottie_list_item, viewModel, converter);
    }

    @Provides
    @ActivityScope
    static LottiesViewModel provideLottiesViewModel(SourceFunnel funnel, LottieSink sink) {
        return new LottiesViewModel(funnel, sink);
    }

    @Binds
    abstract LottieSink provideMemoryLottieSink(MemoryLottieStore implementation);

}
