package com.pij.lottieshow.interactor;

import com.pij.lottieshow.model.LottieFile;

import rx.Observable;

/**
 * <p>Created on 08/04/2017.</p>
 * @author Pierrejean
 */

public interface LottieSource {

    Observable<Iterable<LottieFile>> lottieFiles();
}
