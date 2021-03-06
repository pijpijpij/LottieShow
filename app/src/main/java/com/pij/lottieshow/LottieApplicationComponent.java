package com.pij.lottieshow;

import com.pij.lottieshow.interactor.AssetModule;
import com.pij.lottieshow.interactor.BuildTypeModule;
import com.pij.lottieshow.interactor.ExternalStorageModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * <p>Created on 08/04/2017.</p>
 * @author Pierrejean
 */
@Component(modules = {
        LottieApplicationModule.class,
        AssetModule.class,
        ExternalStorageModule.class,
        BuildTypeModule.class, AndroidSupportInjectionModule.class, BuildersModule.class
})
@Singleton
interface LottieApplicationComponent {

    void inject(LottieApplication target);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(LottieApplication application);

        LottieApplicationComponent build();
    }
}
