package com.pij.lottieshow.detail;

import com.pij.dagger.FragmentScope;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * <p>Created on 12/04/2017.</p>
 * @author Pierrejean
 */
@FragmentScope
@Subcomponent(modules = LottieFragmentModule.class)
public interface LottieFragmentSubcomponent extends AndroidInjector<LottieFragment> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<LottieFragment> { }
}
