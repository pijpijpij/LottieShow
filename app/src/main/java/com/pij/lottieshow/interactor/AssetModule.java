package com.pij.lottieshow.interactor;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;

/**
 * <p>Created on 08/04/2017.</p>
 * @author Pierrejean
 */

@Module
public abstract class AssetModule {

    @Binds
    @IntoSet
    abstract LottieSource provideAssetSource(AssetSource implementation);

    @Binds
    @IntoSet
    abstract Serializer provideAssetSerializer(AssetSerializer implementation);
}
