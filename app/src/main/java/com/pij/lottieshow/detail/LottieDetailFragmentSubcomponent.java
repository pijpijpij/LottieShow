package com.pij.lottieshow.detail;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * <p>Created on 12/04/2017.</p>
 * @author Pierrejean
 */

@Subcomponent
public interface LottieDetailFragmentSubcomponent extends AndroidInjector<LottieDetailFragment> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<LottieDetailFragment> { }
}
