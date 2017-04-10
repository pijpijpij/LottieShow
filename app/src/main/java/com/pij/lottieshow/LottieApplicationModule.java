package com.pij.lottieshow;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <p>Created on 08/04/2017.</p>
 * @author Pierrejean
 */
@Module
class LottieApplicationModule {

    @Provides
    Context provideContext(LottieApplication application) {
        return application.getApplicationContext();
    }

}