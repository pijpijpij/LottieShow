package com.pij.lottieshow.sources;

import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;

import javax.inject.Inject;

import rx.Observable;

import static rx.Observable.empty;
import static rx.Observable.just;

/**
 * <p>Created on 10/04/2017.</p>
 * @author Pierrejean
 */

class ExternalStorage {

    @Inject
    ExternalStorage() {
    }

    @SuppressWarnings("WeakerAccess")
    @NonNull
    public Observable<File> root() {
        return isReadable() ? just(getRoot()) : empty();
    }

    @SuppressWarnings("WeakerAccess")
    public File getRoot() {
        return Environment.getExternalStorageDirectory();
    }

    /**
     * Checks if external storage is available to at least read
     */
    private boolean isReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}
