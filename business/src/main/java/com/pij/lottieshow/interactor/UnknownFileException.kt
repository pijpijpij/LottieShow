package com.pij.lottieshow.interactor

/**
 *
 * Created on 13/04/2017.
 * @author Pierrejean
 */

internal class UnknownFileException : RuntimeException {

    constructor() : super() {}

    constructor(cause: Throwable) : super(cause) {}

    constructor(format: String) : super(format) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}
}
