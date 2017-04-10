package com.pij.lottieshow.sources;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;

import rx.Observable;

/**
 * <p>Created on 10/04/2017.</p>
 * @author Pierrejean
 */

class RemovableExternalStorage {

    private final Observable<Intent> events;

    RemovableExternalStorage(Observable<Intent> events) {
        this.events = events;
    }

    /**
     * Emits whenever the state of the external storage changes (becoming available or <em>un</em>available).
     * TODO emit whenever a file is changed. added or deleted in this directory
     * @return the shared external storage.
     */
    @SuppressWarnings("WeakerAccess")
    @NonNull
    public Observable<File> root() {
        return events.map(ignored -> Environment.getExternalStorageState())
                     .map(state -> Environment.MEDIA_MOUNTED.equals(state) ||
                                   Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
                     .distinctUntilChanged()
                     .map(readable -> Environment.getExternalStorageDirectory());
    }

}
