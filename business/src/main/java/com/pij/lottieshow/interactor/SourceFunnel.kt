package com.pij.lottieshow.interactor

import com.pij.lottieshow.model.LottieFile
import rx.Observable
import rx.Observable.empty
import rx.lang.kotlin.combineLatest
import javax.inject.Inject

class SourceFunnel @Inject
constructor(sources: Iterable<LottieSource>) : LottieSource {

    private val lotties: Observable<Iterable<LottieFile>> = listInOrder(sources)

    override fun lottieFiles(): Observable<Iterable<LottieFile>> {
        return lotties
    }

    private fun listInOrder(sources: Iterable<LottieSource>): Observable<Iterable<LottieFile>> {
        return sources.map { it.lottieFiles().onErrorResumeNext { empty() } }
                .combineLatest { files -> files.flatMap { it } }
    }

}
