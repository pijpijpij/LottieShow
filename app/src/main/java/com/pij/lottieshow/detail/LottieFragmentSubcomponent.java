package com.pij.lottieshow.detail;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * <p>Created on 12/04/2017.</p>
 * @author Pierrejean
 */

@Subcomponent(/*modules = ...*/)
public interface LottieFragmentSubcomponent extends AndroidInjector<LottieFragment> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<LottieFragment> { }
}
