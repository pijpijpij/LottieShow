package com.pij.lottieshow.interactor;

import android.support.annotation.NonNull;

import com.pij.lottieshow.model.LottieFile;

/**
 * <p>Created on 11/04/2017.</p>
 * @author Pierrejean
 */

public interface LottieSink {

    void add(@NonNull LottieFile lottieFile);
}
