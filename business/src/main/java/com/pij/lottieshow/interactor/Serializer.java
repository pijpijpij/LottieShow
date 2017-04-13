package com.pij.lottieshow.interactor;

import com.pij.lottieshow.model.LottieFile;

import java.io.Reader;

import rx.Single;

/**
 * <p>Created on 13/04/2017.</p>
 * @author Pierrejean
 */

public interface Serializer {

    Single<Reader> open(LottieFile input);
}
