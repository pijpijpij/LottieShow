package com.pij.lottieshow.detail;

import com.pij.lottieshow.model.LottieFile;

import javax.inject.Inject;

class LottieDetailViewModel {

    @Inject
    @SuppressWarnings("WeakerAccess")
    public LottieDetailViewModel() {
    }

    @SuppressWarnings("WeakerAccess")
    public void loadLottie(LottieFile newFile) {
        //        sink.add(LottieFile.create(newFile));
    }
}
