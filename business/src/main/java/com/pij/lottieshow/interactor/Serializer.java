package com.pij.lottieshow.interactor;

import java.net.URI;

import rx.Single;

/**
 * <p>Created on 13/04/2017.</p>
 * @author Pierrejean
 */

public interface Serializer {

    Single<String> open(URI input);
}
