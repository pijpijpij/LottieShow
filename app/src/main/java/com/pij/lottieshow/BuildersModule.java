package com.pij.lottieshow;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.pij.lottieshow.detail.LottieActivity;
import com.pij.lottieshow.detail.LottieActivitySubComponent;
import com.pij.lottieshow.detail.LottieFragment;
import com.pij.lottieshow.detail.LottieFragmentSubcomponent;
import com.pij.lottieshow.list.LottiesActivity;
import com.pij.lottieshow.list.LottiesActivitySubComponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

/**
 * Required for automatic injection.
 * <p>Created on 08/04/2017.</p>
 * @author Pierrejean
 */
@Module
abstract class BuildersModule {

    @Binds
    @IntoMap
    @ActivityKey(LottiesActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindLottiesActivityInjectorFactory(
            LottiesActivitySubComponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(LottieActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindLottieActivityInjectorFactory(
            LottieActivitySubComponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(LottieFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindLottieDetailFragmentInjectorFactory(
            LottieFragmentSubcomponent.Builder builder);
}
