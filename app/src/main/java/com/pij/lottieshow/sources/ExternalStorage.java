package com.pij.lottieshow.sources;

import android.os.Environment;

import java.io.File;

import javax.inject.Inject;

/**
 * <p>Created on 10/04/2017.</p>
 * @author Pierrejean
 */

class ExternalStorage {

    @Inject
    ExternalStorage() {
    }

    /**
     * Checks if external storage is available to at least read
     */
    public boolean isReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    @SuppressWarnings("WeakerAccess")
    public File getRoot() {
        return Environment.getExternalStorageDirectory();
    }
}
