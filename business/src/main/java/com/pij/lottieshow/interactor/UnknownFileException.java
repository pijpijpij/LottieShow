package com.pij.lottieshow.interactor;

/**
 * <p>Created on 13/04/2017.</p>
 * @author Pierrejean
 */

class UnknownFileException extends RuntimeException {

    @SuppressWarnings("WeakerAccess")
    public UnknownFileException() {
        super();
    }

    @SuppressWarnings("WeakerAccess")
    public UnknownFileException(Throwable cause) {
        super(cause);
    }

    @SuppressWarnings("WeakerAccess")
    public UnknownFileException(String format) {
        super(format);
    }

    @SuppressWarnings("WeakerAccess")
    public UnknownFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
