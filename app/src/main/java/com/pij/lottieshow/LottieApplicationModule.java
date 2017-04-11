package com.pij.lottieshow;

import android.content.Context;

import com.pij.lottieshow.list.MemoryLottieStore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * <p>Created on 08/04/2017.</p>
 * @author Pierrejean
 */
@Module
class LottieApplicationModule {

    @Provides
    @Singleton
    static MemoryLottieStore provideMemoryLottieStore() {
        return new MemoryLottieStore();
    }

    @Provides
    Context provideContext(LottieApplication application) {
        return application.getApplicationContext();
    }

}