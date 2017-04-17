package com.pij.lottieshow.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.pij.lottieshow.R;
import com.pij.lottieshow.detail.LottieActivity;
import com.pij.lottieshow.detail.LottieFragment;
import com.pij.lottieshow.model.Converter;
import com.pij.lottieshow.model.LottieFile;
import com.pij.lottieshow.model.LottieUi;
import com.pij.lottieshow.saf.SafClient;
import com.pij.lottieshow.ui.Utils;

import org.apache.commons.collections4.IterableUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

import static com.jakewharton.rxbinding.view.RxView.clicks;
import static rx.Observable.empty;
import static rx.Observable.from;
import static rx.Observable.just;

/**
 * An activity representing a list of Lotties. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link LottieActivity} representing
 * item label. On tablets, the activity presents the list of items and
 * item label side-by-side using two vertical panes.
 */
public class LottiesActivity extends DaggerAppCompatActivity {

    private static final int REQUEST_CODE_PICK = 24;

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
    @Inject
    SafClient saf;
    @Inject
    Converter converter;

    private Unbinder unbinder;
    private Snackbar progress;

    public LottiesActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie_list);
        unbinder = ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        progress = Snackbar.make(fab, "Loading...", Snackbar.LENGTH_INDEFINITE);

        LottiesAdapter adapter = new LottiesAdapter(R.layout.lottie_list_item);
        list.setAdapter(adapter);

        subscriptions.addAll(clicks(fab).subscribe(click -> pickJsonFile(), this::notifyError),
                             saf.analysed().subscribe(viewModel::addLottie, this::notifyError),
                             saf.inProgress().subscribe(this::showProgress, this::notifyError),

                             adapter.contentNeeded()
                                    .flatMap(this::toModel)
                                    .subscribe(viewModel::loadContent, this::notifyError),
                             adapter.itemClicked()
                                    .flatMap(this::toModel)
                                    .subscribe(viewModel::select, this::notifyError),

                             viewModel.shouldShowList()
                                      .map(IterableUtils::emptyIfNull)
                                      .flatMap(list -> from(list).flatMap(this::fromModel).toList())
                                      .subscribe(adapter::setItems, this::notifyError),

                             // The detail container view will be present only in the
                             // large-screen layouts (res/values-w900dp).
                             // If this view is present, then the
                             // activity should be in two-pane mode.
                             detailContainer == null
                             ? showInActivity(viewModel.shouldShowLottie())
                             : showInFragment(viewModel.shouldShowLottie()));
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
            case REQUEST_CODE_PICK:
                switch (resultCode) {
                    case RESULT_OK:
                        saf.analyse(data);
                        break;
                    default:
                        break;
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private Observable<LottieFile> toModel(LottieUi ui) {
        return converter.toModel(ui).toObservable();
    }

    private Observable<LottieUi> fromModel(LottieFile model) {
        return converter.fromModel(model).toObservable();
    }

    private void showProgress(boolean inProgress) {
        if (inProgress) { progress.show(); } else progress.dismiss();
    }

    private void notifyError(Throwable error) {
        Utils.notifyError(error, fab);
    }

    private void pickJsonFile() {
        saf.pickJsonFile(this, REQUEST_CODE_PICK);
    }

    private Subscription showInFragment(Observable<LottieFile> lottieFile) {
        return lottieFile.compose(mapWithoutError(LottieFragment::createInstance))
                         .subscribe(this::setDetailFragment, this::notifyError);
    }

    private Subscription showInActivity(Observable<LottieFile> lottieFile) {
        return lottieFile.compose(mapWithoutError(i -> LottieActivity.createIntent(this, i)))
                         .subscribe(this::startActivity, this::notifyError);
    }

    @NonNull
    private <T> Observable.Transformer<LottieFile, T> mapWithoutError(final Func1<LottieUi, T> mapper) {
        return lottie -> lottie.flatMap(this::fromModel).flatMap(item -> just(item).map(mapper)
                                                          .doOnError(this::notifyError)
                                                          .onErrorResumeNext(empty()));
    }

    private int setDetailFragment(LottieFragment fragment) {
        return getSupportFragmentManager().beginTransaction().replace(R.id.lottie_detail_container, fragment).commit();
    }
}
