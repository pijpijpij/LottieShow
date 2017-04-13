package com.pij.lottieshow.detail;

import android.support.v4.app.Fragment;

import com.pij.lottieshow.interactor.CompoundSerializer;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

/**
 * <p>Created on 12/04/2017.</p>
 * @author Pierrejean
 */
@Module(subcomponents = LottieFragmentSubcomponent.class)
public abstract class LottieFragmentModule {

    @Provides
    static LottieViewModel provideLottieViewModel(CompoundSerializer serializer) {
        return new LottieViewModel(serializer);
    }

    @Binds
    @IntoMap
    @FragmentKey(LottieFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindLottieDetailFragmentInjectorFactory(
            LottieFragmentSubcomponent.Builder builder);
}