package com.pij.lottieshow.interactor;

/**
 * <p>Created on 13/04/2017.</p>
 * @author Pierrejean
 */

class UnsupportedFormatException extends RuntimeException {

    @SuppressWarnings("WeakerAccess")
    public UnsupportedFormatException() {
        super();
    }

    @SuppressWarnings("WeakerAccess")
    public UnsupportedFormatException(Throwable cause) {
        super(cause);
    }

    @SuppressWarnings("WeakerAccess")
    public UnsupportedFormatException(String format) {
        super(format);
    }

    @SuppressWarnings("WeakerAccess")
    public UnsupportedFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
