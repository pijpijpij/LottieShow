package com.pij.lottieshow;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;

import com.pij.lottieshow.detail.LottieFragmentSubcomponent;
import com.pij.lottieshow.interactor.CompoundSerializer;
import com.pij.lottieshow.interactor.ContentResolverSerializer;
import com.pij.lottieshow.interactor.LottieSource;
import com.pij.lottieshow.interactor.Serializer;
import com.pij.lottieshow.interactor.SourceFunnel;
import com.pij.lottieshow.list.LottiesActivitySubComponent;
import com.pij.lottieshow.list.MemoryLottieStore;
import com.pij.lottieshow.model.Converter;

import java.util.Set;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

/**
 * <p>Created on 08/04/2017.</p>
 * @author Pierrejean
 */
@Module(subcomponents = { LottiesActivitySubComponent.class, LottieFragmentSubcomponent.class })
abstract class LottieApplicationModule {

    @Provides
    @Singleton
    static MemoryLottieStore provideMemoryLottieStore() {
        return new MemoryLottieStore();
    }

    @Provides
    @Singleton
    static SourceFunnel provideSourceFunnel(Set<LottieSource> sources) {
        return new SourceFunnel(sources);
    }

    @Provides
    @Singleton
    static CompoundSerializer provideCompoundSerializer(Set<Serializer> sources) {
        return new CompoundSerializer(sources);
    }

    @Provides
    static Context provideContext(LottieApplication application) {
        return application.getApplicationContext();
    }

    @Provides
    static AssetManager provideAssetManager(Context context) {
        return context.getAssets();
    }

    @Provides
    static ContentResolver provideContentResolver(Context context) {
        return context.getContentResolver();
    }

    @Provides
    @Singleton
    static Converter provideConverter(SourceFunnel source) {
        return new Converter(source);
    }

    @Provides
    @IntoSet
    static Serializer provideContentResolverSerializer(ContentResolver contentResolver) {
        return new ContentResolverSerializer(contentResolver);
    }

    @Binds
    @IntoSet
    abstract LottieSource provideMemoryLottieSource(MemoryLottieStore implementation);
}