package com.pij.lottieshow.list

import com.pij.lottieshow.interactor.LottieSink
import com.pij.lottieshow.interactor.LottieSource
import com.pij.lottieshow.model.LottieFile
import rx.Observable
import rx.Observable.defer
import rx.subjects.PublishSubject

class LottiesViewModel(private val updatableSources: LottieSource, private val sink: LottieSink) {

    private val lotties: Observable<Iterable<LottieFile>> = defer { updatableSources.lottieFiles() }
    private val shouldShowLottie = PublishSubject.create<LottieFile>()

    fun shouldShowList(): Observable<Iterable<LottieFile>> {
        return lotties
    }

    fun addLottie(newFile: LottieFile) {
        sink.add(newFile)
    }

    fun select(lottie: LottieFile) {
        shouldShowLottie.onNext(lottie)
    }

    fun shouldShowLottie(): Observable<LottieFile> {
        return shouldShowLottie
    }

}
