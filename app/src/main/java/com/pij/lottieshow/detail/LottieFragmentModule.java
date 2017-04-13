package com.pij.lottieshow.detail;

import android.support.v4.app.Fragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

/**
 * <p>Created on 12/04/2017.</p>
 * @author Pierrejean
 */
@Module(subcomponents = LottieFragmentSubcomponent.class)
public abstract class LottieFragmentModule {

    @Binds
    @IntoMap
    @FragmentKey(LottieFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindLottieDetailFragmentInjectorFactory(
            LottieFragmentSubcomponent.Builder builder);
}