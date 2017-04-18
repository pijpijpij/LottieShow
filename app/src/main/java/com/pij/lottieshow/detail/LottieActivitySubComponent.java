package com.pij.lottieshow.detail;

import com.pij.dagger.ActivityScope;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * @author Pierrejean
 */

@ActivityScope
@Subcomponent(modules = LottieActivityModule.class)
public interface LottieActivitySubComponent extends AndroidInjector<LottieActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<LottieActivity> { }
}
