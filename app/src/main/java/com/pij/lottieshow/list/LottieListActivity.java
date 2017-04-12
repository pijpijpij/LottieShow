package com.pij.lottieshow.list;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.pij.lottieshow.LottieDetailActivity;
import com.pij.lottieshow.LottieDetailFragment;
import com.pij.lottieshow.R;
import com.pij.lottieshow.model.LottieFile;

import java.net.URI;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjection;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

import static com.jakewharton.rxbinding.view.RxView.clicks;
import static rx.Observable.empty;
import static rx.Observable.just;

/**
 * An activity representing a list of Lotties. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link LottieDetailActivity} representing
 * item label. On tablets, the activity presents the list of items and
 * item label side-by-side using two vertical panes.
 */
public class LottieListActivity extends AppCompatActivity {

    private static final int REQUESTCODE_PICK = 24;

    private final CompositeSubscription subscriptions = new CompositeSubscription();
    @Nullable
    @BindView(R.id.lottie_detail_container)
    View detailContainer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.lottie_list)
    RecyclerView list;
    @Inject
    LottiesViewModel viewModel;
    private Unbinder unbinder;
    private PublishSubject<Intent> jsonFilePicked = PublishSubject.create();

    public LottieListActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie_list);
        unbinder = ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        LottieAdapter adapter = new LottieAdapter(R.layout.lottie_list_item);
        list.setAdapter(adapter);

        subscriptions.addAll(clicks(fab).subscribe(click -> pickJsonFile(), this::notifyError),
                             jsonFilePicked.map(Intent::getData)
                                           .map(Uri::toString)
                                           .map(URI::create)
                                           .subscribe(viewModel::addLottie, this::notifyError),


                             viewModel.shouldShowList().subscribe(adapter::setItems, this::notifyError),

                             // The detail container view will be present only in the
                             // large-screen layouts (res/values-w900dp).
                             // If this view is present, then the
                             // activity should be in two-pane mode.
                             detailContainer == null
                             ? showInActivity(adapter.itemClicked())
                             : showInFragment(adapter.itemClicked()));
    }

    @Override
    protected void onDestroy() {
        subscriptions.clear();
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_PICK:
                switch (resultCode) {
                    case RESULT_OK:
                        jsonFilePicked.onNext(data);
                        break;
                    default:
                        break;
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void notifyError(Throwable error) {
        error.printStackTrace();
        //TODO add a dialog to display detail of exception stack.
        Snackbar.make(fab, "Error: " + error, Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_show_error, null)
                .show();
    }

    /**
     * <b>Implementation note:</b> It is not clear why some type work better than others:<ul>
     * <li><code>{@literal *}/{@literal *}</code> is too lax</li>
     * <li><code>{@literal *}/json</code> does not work</li>
     * <li><code>application/json</code> does not work</li>
     * <li><code>application/{@literal *}</code> leaves .json files selectable, so that's what we use.</li>
     * </ul>
     */
    private void pickJsonFile() {
        Intent pick = new Intent(Intent.ACTION_OPEN_DOCUMENT).setType("application/*");
        startActivityForResult(pick, REQUESTCODE_PICK);
    }

    private Subscription showInFragment(Observable<LottieFile> lottieFile) {
        return lottieFile.compose(mapWithoutError(LottieDetailFragment::createInstance))
                         .subscribe(this::setDetailFragment, this::notifyError);
    }

    private Subscription showInActivity(Observable<LottieFile> lottieFile) {
        return lottieFile.compose(mapWithoutError(i -> LottieDetailActivity.createIntent(this, i)))
                         .subscribe(this::startActivity, this::notifyError);
    }

    @NonNull
    private <T> Observable.Transformer<LottieFile, T> mapWithoutError(final Func1<LottieFile, T> mapper) {
        return lottieFile -> lottieFile.flatMap(item -> just(item).map(mapper)
                                                                  .doOnError(this::notifyError)
                                                                  .onErrorResumeNext(empty()));
    }

    private int setDetailFragment(LottieDetailFragment fragment) {
        return getSupportFragmentManager().beginTransaction().replace(R.id.lottie_detail_container, fragment).commit();
    }
}
