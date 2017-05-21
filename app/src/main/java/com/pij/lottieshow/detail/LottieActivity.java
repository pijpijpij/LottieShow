package com.pij.lottieshow.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.pij.lottieshow.R;
import com.pij.lottieshow.list.LottiesActivity;
import com.pij.lottieshow.model.Converter;
import com.pij.lottieshow.model.LottieUi;
import com.pij.lottieshow.saf.SafClient;
import com.pij.lottieshow.ui.LibraryString;
import com.pij.lottieshow.ui.Utils;

import javax.inject.Inject;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import activitystarter.MakeActivityStarter;
import activitystarter.Optional;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerAppCompatActivity;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * An activity representing a single Lottie detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item label are presented side-by-side with a list of items
 * in a {@link LottiesActivity}.
 */
@MakeActivityStarter
public class LottieActivity extends DaggerAppCompatActivity {

    private final CompositeSubscription subscriptions = new CompositeSubscription();
    private final PublishSubject<LottieUi> internalLottie = PublishSubject.create();
    @Arg
    @Optional
    @Nullable
    LottieUi file;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @Inject
    LibraryString libraryString;
    @Inject
    LottieViewModel viewModel;
    @Inject
    Converter converter;
    @Inject
    SafClient safClient;
    private Unbinder unbinder;

    @NonNull
    public static Intent createIntent(Context context, LottieUi item) {
        return LottieActivityStarter.getIntent(context, item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie_detail);
        ActivityStarter.fill(this);
        unbinder = ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        // Show the Up button in the action bar.
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                                               .setAction("Action", null)
                                               .show());

        // 1) listen to that one  being created
        subscriptions.addAll(
                // Display whichever lottie is to be displayed
                viewModel.showLottie()
                         .flatMapSingle(converter::fromModel)
                         .map(LottieFragment::createInstance)
                         .subscribe(this::setDetailFragment, this::notifyError),
                // Add the external Lottie if it is provided
                safClient.analysed().subscribe(viewModel::addLottie, this::notifyError),
                // load the internal lottie if it is provided
                internalLottie.filter(file -> file != null)
                              .flatMapSingle(converter::toModel)
                              .subscribe(viewModel::loadLottie, this::notifyError));

        if (savedInstanceState == null) {
            if (file == null) {
                // No internal lottie, let's look for an external one.
                safClient.analyse(getIntent());
            } else {
                internalLottie.onNext(file);
            }
        }
    }

    @Override
    protected void onDestroy() {
        subscriptions.clear();
        unbinder.unbind();
        super.onDestroy();
    }

    /**
     * Put the library version in the menu.
     * We don't do anything when the _only_ menu is clicked.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lotties_menu, menu);
        libraryString.configure(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateUpTo(LottiesActivity.Companion.createIntent(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void notifyError(Throwable error) {
        Utils.notifyError(error, fab);
    }

    private void setDetailFragment(LottieFragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, fragment).commit();
    }
}
