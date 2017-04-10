package com.pij.lottieshow.sources;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.cantrowitz.rxbroadcast.RxBroadcast;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

/**
 * <p>Created on 08/04/2017.</p>
 * @author Pierrejean
 */

@Module
public abstract class ExternalStorageModule {

    @Provides
    @IntoSet
    static LottieSource provideExternalSource(RemovableExternalStorage storage) {
        return new DirectorySource(storage.root());
    }

    @Provides
    static IntentFilter provideExternalStorageIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        return filter;
    }

    @Provides
    static RemovableExternalStorage provideRemovableExternalStorage(Context context, IntentFilter intentFilter) {
        return new RemovableExternalStorage(RxBroadcast.fromBroadcast(context, intentFilter));
    }
}
