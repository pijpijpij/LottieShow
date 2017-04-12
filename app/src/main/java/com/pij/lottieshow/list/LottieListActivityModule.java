package com.pij.lottieshow.list;

import android.app.Activity;

import com.pij.lottieshow.interactor.LottieSink;
import com.pij.lottieshow.interactor.SourceFunnel;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * <p>Created on 08/04/2017.</p>
 * @author Pierrejean
 */

@Module(subcomponents = LottieListActivitySubComponent.class)
public abstract class LottieListActivityModule {

    @Provides
    static LottiesViewModel provideLottiesViewModel(SourceFunnel funnel, LottieSink sink) {
        return new LottiesViewModel(funnel, sink);
    }

    @Binds
    abstract LottieSink provideMemoryLottieSink(MemoryLottieStore implementation);

    /**
     * Required for automatic injection.
     */
    @Binds
    @IntoMap
    @ActivityKey(LottieListActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindLottieListActivityInjectorFactory(
            LottieListActivitySubComponent.Builder builder);

}
