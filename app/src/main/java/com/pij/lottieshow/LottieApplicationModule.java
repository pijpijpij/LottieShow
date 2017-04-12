package com.pij.lottieshow;

import android.content.Context;
import android.content.res.AssetManager;

import com.pij.lottieshow.interactor.LottieSource;
import com.pij.lottieshow.interactor.SourceFunnel;
import com.pij.lottieshow.list.MemoryLottieStore;
import com.pij.lottieshow.model.Converter;

import java.util.Set;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

/**
 * <p>Created on 08/04/2017.</p>
 * @author Pierrejean
 */
@Module
abstract class LottieApplicationModule {

    @Provides
    @Singleton
    static MemoryLottieStore provideMemoryLottieStore() {
        return new MemoryLottieStore();
    }

    @Provides
    @Singleton
    static SourceFunnel provideSourceFunnel(Set<LottieSource> sources) {
        return new SourceFunnel(sources);
    }

    @Provides
    static Context provideContext(LottieApplication application) {
        return application.getApplicationContext();
    }

    @Provides
    static AssetManager provideAssetManager(Context context) {
        return context.getAssets();
    }

    @Provides
    @Singleton
    static Converter provideConverter(SourceFunnel source) {
        return new Converter(source);
    }

    @Binds
    @IntoSet
    abstract LottieSource provideMemoryLottieSource(MemoryLottieStore implementation);
}