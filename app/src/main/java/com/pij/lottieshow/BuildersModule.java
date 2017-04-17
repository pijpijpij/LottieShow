package com.pij.lottieshow;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.pij.lottieshow.detail.LottieFragment;
import com.pij.lottieshow.detail.LottieFragmentSubcomponent;
import com.pij.lottieshow.list.LottieListActivity;
import com.pij.lottieshow.list.LottieListActivitySubComponent;

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
    @ActivityKey(LottieListActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindLottieListActivityInjectorFactory(
            LottieListActivitySubComponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(LottieFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindLottieDetailFragmentInjectorFactory(
            LottieFragmentSubcomponent.Builder builder);
}
