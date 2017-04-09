package com.pij.lottieshow.list;

import android.app.Activity;

import com.pij.lottieshow.model.LottieFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import dagger.multibindings.IntoSet;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static rx.Observable.just;

/**
 * <p>Created on 08/04/2017.</p>
 * @author Pierrejean
 */

@Module(subcomponents = LottieListActivitySubComponent.class)
public abstract class LottieListActivityModule {

    @Provides
    static LottiesViewModel provideLottiesViewModel(Set<LottieSource> sources) {
        return new LottiesViewModel(new ArrayList<>(sources));
    }

    @Provides
    @IntoSet
    static LottieSource provideEmptyLottieSource() {
        return () -> just(emptyList());
    }

    @Provides
    @IntoSet
    static LottieSource provideTestLottieSource() {
        return () -> just(singletonList(LottieFile.create(new File("asdfasdf", "the label"))));
    }

    /**
     * Required for automatic injection.
     */
    @Binds
    @IntoMap
    @ActivityKey(LottieListActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindLottieListActivityInjectorFactory(
            LottieListActivitySubComponent.Builder builder);

}
