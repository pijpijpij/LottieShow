package com.pij.lottieshow;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasDispatchingActivityInjector;
import dagger.android.support.HasDispatchingSupportFragmentInjector;

/**
 * <p>Created on 08/04/2017.</p>
 * @author Pierrejean
 */

public class LottieApplication extends Application
        implements HasDispatchingActivityInjector, HasDispatchingSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        DaggerLottieApplicationComponent.builder().application(this).build().inject(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingFragmentInjector;
    }
}