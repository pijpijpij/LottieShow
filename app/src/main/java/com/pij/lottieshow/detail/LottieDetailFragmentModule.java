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
@Module(subcomponents = LottieDetailFragmentSubcomponent.class)
public abstract class LottieDetailFragmentModule {

    @Binds
    @IntoMap
    @FragmentKey(LottieDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindLottieDetailFragmentInjectorFactory(
            LottieDetailFragmentSubcomponent.Builder builder);
}