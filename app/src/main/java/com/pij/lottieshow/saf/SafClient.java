package com.pij.lottieshow.saf;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;

import com.pij.lottieshow.interactor.ContentResolverSerializer;
import com.pij.lottieshow.interactor.Serializer;
import com.pij.lottieshow.model.LottieFile;

import java.net.URI;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.subjects.PublishSubject;

import static rx.Single.just;
import static rx.Single.using;

/**
 * <p>Created on 13/04/2017.</p>
 * @author Pierrejean
 */

public class SafClient {

    private final ContentResolver contentResolver;
    private final Serializer serializer;
    private final PublishSubject<Intent> jsonFilePicked = PublishSubject.create();
    private final PublishSubject<Boolean> inProgress = PublishSubject.create();

    @Inject
    @SuppressWarnings("WeakerAccess")
    public SafClient(ContentResolver contentResolver, ContentResolverSerializer serializer) {
        this.contentResolver = contentResolver;
        this.serializer = serializer;
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
                                                             .setType("application/json");
        launcher.startActivityForResult(pick, requestCode);
    }

    @SuppressWarnings("WeakerAccess")
    public void analyse(Intent picked) {
        jsonFilePicked.onNext(picked);
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<LottieFile> analysed() {
        return jsonFilePicked.map(Intent::getData)
                             .flatMapSingle(uri -> Single.zip(calculateURI(uri),
                                                              calculateLabel(uri),
                                                              calculateContent(uri),
                                                              LottieFile::create)
                                                         .doOnSubscribe(() -> inProgress.onNext(true))
                                                         .doAfterTerminate(() -> inProgress.onNext(false)));
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<Boolean> inProgress() {
        return inProgress;
    }

    private Single<URI> calculateURI(Uri uri) {
        return just(uri).map(Uri::toString).map(URI::create);
    }

    private Single<Single<String>> calculateContent(Uri uri) {
        return calculateURI(uri).map(serializer::open);
    }

    @NonNull
    private Single<String> calculateLabel(Uri uri) {
        return using(() -> queryDisplayName(uri), cursor -> just(cursor).map(c -> c.getString(0)), Cursor::close);
    }

    @NonNull
    private Cursor queryDisplayName(Uri uri) {
        Cursor result = contentResolver.query(uri, new String[]{ OpenableColumns.DISPLAY_NAME }, null, null, null);
        if (result == null || !result.moveToFirst()) throw new IllegalArgumentException("Unsupported Uri: " + uri);
        return result;
    }
}
