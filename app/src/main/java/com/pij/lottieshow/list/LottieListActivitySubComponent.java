package com.pij.lottieshow.list;

import com.pij.dagger.ActivityScope;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * <p>Created on 08/04/2017.</p>
 * @author Pierrejean
 */

@ActivityScope
@Subcomponent(modules = LottieListActivityModule.class)
public interface LottieListActivitySubComponent extends AndroidInjector<LottieListActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<LottieListActivity> { }
}
