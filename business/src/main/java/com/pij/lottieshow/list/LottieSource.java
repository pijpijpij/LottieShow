package com.pij.lottieshow.list;

import com.pij.lottieshow.model.LottieFile;

import rx.Observable;

/**
 * <p>Created on 08/04/2017.</p>
 * @author Pierrejean
 */

interface LottieSource {

    Observable<Iterable<LottieFile>> getLottieFiles();
}
