package com.pij.lottieshow.detail;

import com.pij.dagger.FragmentScope;
import com.pij.lottieshow.interactor.CompoundSerializer;
import com.pij.lottieshow.interactor.LottieSink;

import dagger.Module;
import dagger.Provides;

/**
 * <p>Created on 12/04/2017.</p>
 * @author Pierrejean
 */
@Module
abstract class LottieFragmentModule {

    @FragmentScope
    @Provides
    static LottieViewModel provideLottieViewModel(LottieSink sink, CompoundSerializer serializer) {
        return new LottieViewModel(sink, serializer);
    }

}