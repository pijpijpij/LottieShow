package com.pij.lottieshow.list;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;

import com.pij.lottieshow.model.LottieFile;

import java.net.URI;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.subjects.PublishSubject;

import static rx.Single.just;
import static rx.Single.using;
import static rx.Single.zip;

/**
 * <p>Created on 13/04/2017.</p>
 * @author Pierrejean
 */

class SafClient {

    private final ContentResolver contentResolver;
    private final PublishSubject<Intent> jsonFilePicked = PublishSubject.create();

    @SuppressWarnings("WeakerAccess")
    @Inject
    public SafClient(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    /**
     * <b>Implementation note:</b> It is not clear why some type work better than others:<ul>
     * <li><code>{@literal *}/{@literal *}</code> is too lax</li>
     * <li><code>{@literal *}/json</code> does not work</li>
     * <li><code>application/json</code> does not work</li>
     * <li><code>application/{@literal *}</code> leaves .json files selectable, so that's what we use.</li>
     * </ul>
     */
    @SuppressWarnings("WeakerAccess")
    public void pickJsonFile(Activity launcher, int requestCode) {
        Intent pick = new Intent(Intent.ACTION_OPEN_DOCUMENT).addCategory(Intent.CATEGORY_OPENABLE)
                                                             .setType("application/*");
        launcher.startActivityForResult(pick, requestCode);
    }

    @SuppressWarnings("WeakerAccess")
    public void analyse(Intent picked) {
        jsonFilePicked.onNext(picked);
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<LottieFile> analysed() {
        return jsonFilePicked.map(Intent::getData).flatMap(uri -> createLottie(uri).toObservable());
    }

    @NonNull
    private Single<LottieFile> createLottie(Uri uri) {
        return zip(just(uri).map(Uri::toString).map(URI::create),
                   using(() -> queryDisplayName(uri), cursor -> just(cursor).map(c -> c.getString(0)), Cursor::close),
                   LottieFile::create);
    }

    @NonNull
    private Cursor queryDisplayName(Uri uri) {
        Cursor result = contentResolver.query(uri, new String[]{ OpenableColumns.DISPLAY_NAME }, null, null, null);
        if (result == null || !result.moveToFirst()) throw new IllegalArgumentException("Unsupported Uri: " + uri);
        return result;
    }
}
