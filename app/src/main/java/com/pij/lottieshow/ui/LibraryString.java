package com.pij.lottieshow.ui;

import android.content.res.Resources;
import android.view.Menu;
import android.view.MenuItem;

import com.pij.lottieshow.R;

import javax.inject.Inject;

/**
 * Manages the Library version menu.
 */
public class LibraryString {

    private final String label;

    @Inject
    @SuppressWarnings("WeakerAccess")
    public LibraryString(Resources resources) {
        String menuLibraryVersionValue = resources.getString(R.string.lottie_library_version);
        label = resources.getString(R.string.lottie_library_version_template, menuLibraryVersionValue);
    }

    public String getLabel() {
        return label;
    }

    /**
     * Convenience method.
     */
    public void configure(Menu menu) {
        MenuItem library = menu.findItem(R.id.library_version);
        library.setTitle(getLabel());
    }

}
