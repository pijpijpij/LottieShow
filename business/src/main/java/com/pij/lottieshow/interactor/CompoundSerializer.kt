package com.pij.lottieshow.interactor

import rx.Observable
import rx.Observable.combineLatest
import rx.Observable.empty
import rx.Observable.error
import rx.Observable.from
import rx.Observable.just
import rx.Single
import java.net.URI
import javax.inject.Inject

class CompoundSerializer @Inject constructor(private val serializers: Iterable<Serializer>) : Serializer {

    override fun open(input: URI): Single<String> {
        // emits all known formats
        val content = combineLatest(from(serializers), just(input), this::open).concatMap { it }
        return content.take(1).toSingle().onErrorResumeNext { transformNoSerializerFound(it, input) }
    }

    private fun open(opener: Serializer, input: URI): Observable<String> {
        return opener.open(input).toObservable().onErrorResumeNext { absorbUnknownFile(it) }
    }

    private fun transformNoSerializerFound(e: Throwable, file: URI): Single<String> {
        val result = if (e is NoSuchElementException) {
            UnknownFileException("Could not find a Serializer for $file.", e)
        } else {
            e
        }
        return Single.error(result)
    }

    private fun absorbUnknownFile(e: Throwable): Observable<String> {
        return if (e is UnknownFileException) empty() else error(e)
    }

}
