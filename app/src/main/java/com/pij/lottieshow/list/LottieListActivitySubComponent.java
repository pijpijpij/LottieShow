package com.pij.lottieshow.list;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * <p>Created on 08/04/2017.</p>
 * @author Pierrejean
 */

@Subcomponent(/*modules = ...*/)
public interface LottieListActivitySubComponent extends AndroidInjector<LottieListActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<LottieListActivity> { }
}
