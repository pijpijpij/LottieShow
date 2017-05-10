package com.pij.lottieshow.interactor

import com.pij.lottieshow.model.LottieFile
import rx.Observable
import rx.Observable.just
import rx.Single
import rx.lang.kotlin.toSingle
import java.io.File

/**
 * Created on 10/04/2017.
 * @author Pierrejean
 */
class DirectorySource(private val storageRoot: Observable<File>) : LottieSource {

    /**
     * Always provide an initial sequence, an empty one at worst.
     */
    override fun lottieFiles(): Observable<Iterable<LottieFile>> {
        val files = storageRoot.map { root -> if (root.isDirectory) root.listFiles() else arrayOf() }
        val lotties = files.flatMapSingle(this::lottieFiles)
        return lotties.startWith(just(listOf())).distinctUntilChanged()
    }

    private fun lottieFiles(files: Array<File>): Single<Iterable<LottieFile>>? {
        return files.map(File::toURI)
                .map(LottieFile::create)
                .toSingle()
    }

}
