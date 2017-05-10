package com.pij.lottieshow.detail

import com.pij.lottieshow.interactor.LottieSink
import com.pij.lottieshow.interactor.Serializer
import com.pij.lottieshow.model.LottieFile
import rx.Observable
import rx.Observable.empty
import rx.Observable.merge
import rx.subjects.PublishSubject
import java.net.URI

class LottieViewModel(private val sink: LottieSink, private val serializer: Serializer) {

    private val lottieToLoad = PublishSubject.create<LottieFile>()
    private val lottieToAdd = PublishSubject.create<LottieFile>()
    private val errors = PublishSubject.create<Throwable>()
    private val lottieToShow: Observable<LottieFile>
    private val animationToShow: Observable<String>

    init {

        val addedLottie = lottieToAdd.flatMap {
            Observable.just(it)
                    .doOnNext { sink.add(it) }
                    .doOnError { errors.onNext(it) }
                    .onErrorResumeNext(empty<LottieFile>())
        }

        val loadedLottie = lottieToLoad
        lottieToShow = merge(loadedLottie, addedLottie)

        animationToShow = lottieToShow.map<URI> { it?.id() }
                .concatMap {
                    serializer.open(it)
                            .toObservable()
                            .doOnError { errors.onNext(it) }
                            .onErrorResumeNext(empty<String>())
                }
    }

    fun loadLottie(newFile: LottieFile?) {
        lottieToLoad.onNext(newFile)
    }

    fun showAnimation(): Observable<String> {
        return animationToShow
    }

    fun showLoadingError(): Observable<Throwable> {
        return errors
    }

    fun addLottie(newFile: LottieFile) {
        lottieToAdd.onNext(newFile)
    }

    fun showLottie(): Observable<LottieFile> {
        return lottieToShow
    }

}
