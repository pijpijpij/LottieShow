package com.pij.lottieshow;

import com.pij.lottieshow.list.LottieListActivityModule;

import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * <p>Created on 08/04/2017.</p>
 * @author Pierrejean
 */
@Component(modules = {/*..., */AndroidSupportInjectionModule.class, LottieListActivityModule.class })
interface LottieApplicationComponent {

    void inject(LottieApplication target);
}
