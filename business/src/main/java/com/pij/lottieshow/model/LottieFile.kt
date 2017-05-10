package com.pij.lottieshow.model


import rx.Single
import rx.Single.just
import java.io.File
import java.net.URI

fun URI.toLottie(): LottieFile {
    return LottieFile(this)
}

data class LottieFile(val id: URI, val label: String? = null, val content: Single<String>? = null) {

    companion object {

        fun create(source: LottieFile, content: String): LottieFile = create(source, just(content))

        fun create(source: LottieFile, content: Single<String>): LottieFile = LottieFile(source.id, source.label,
                                                                                         content)

        fun create(id: File): LottieFile {
            return id.toURI().toLottie()
        }

        fun create(id: URI): LottieFile {
            return id.toLottie()
        }

        //        @JvmOverloads fun create(id: URI, label: String? = null, content: String? = null): LottieFile {
        //            return LottieFile(id, label, just(content))
        //        }
        //
        fun create(id: URI, label: String, content: Single<String>): LottieFile {
            return LottieFile(id, label, content)
        }
    }
}
