package com.pij.lottieshow.list

import com.pij.lottieshow.interactor.LottieSink
import com.pij.lottieshow.interactor.LottieSource
import com.pij.lottieshow.model.LottieFile
import org.apache.commons.collections4.IterableUtils
import rx.Observable
import rx.subjects.BehaviorSubject

/**
 *
 * Created on 11/04/2017.
 * @author Pierrejean
 */

class MemoryLottieStore : LottieSource, LottieSink {

    private val store = BehaviorSubject.create<LottieFile>()
    private val lotties: Observable<Iterable<LottieFile>> = store.asObservable()
            .scan(listOf(), this::append)
            .replay(1)
            .refCount()
            .doOnNext { i -> println("PJC got ${IterableUtils.size(i)} items:") }
            .doOnNext { i -> println("PJC $i") }


    override fun lottieFiles(): Observable<Iterable<LottieFile>> {
        return lotties
    }

    override fun add(lottieFile: LottieFile) {
        store.onNext(lottieFile)
    }

    private fun append(current: Iterable<LottieFile>, newFile: LottieFile): Iterable<LottieFile> {
        return current.plus(newFile)
    }
}
