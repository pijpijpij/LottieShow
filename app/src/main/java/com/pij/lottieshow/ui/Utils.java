package com.pij.lottieshow.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.pij.lottieshow.R;

/**
 * <p>Created on 12/04/2017.</p>
 * @author Pierrejean
 */
public class Utils {

    public static void notifyError(@NonNull Throwable error, @NonNull View snackbarRelative) {
        error.printStackTrace();
        //TODO add a dialog to display detail of exception stack.
        Snackbar.make(snackbarRelative, "Error: " + error, Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_show_error, null)
                .show();
    }
}
