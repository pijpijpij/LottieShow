package com.pij.lottieshow.list;

import android.os.Bundle;
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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjection;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.jakewharton.rxbinding.view.RxView.clicks;

/**
 * An activity representing a list of Lotties. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link LottieDetailActivity} representing
 * item label. On tablets, the activity presents the list of items and
 * item label side-by-side using two vertical panes.
 */
public class LottieListActivity extends AppCompatActivity {

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

    public LottieListActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie_list);
        unbinder = ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        subscriptions.add(clicks(fab).subscribe(view -> Snackbar.make(fab,
                                                                      "Replace with your own action",
                                                                      Snackbar.LENGTH_LONG)
                                                                .setAction("Action", null)
                                                                .show(), Throwable::printStackTrace));

        LottieAdapter adapter = new LottieAdapter(android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);
        subscriptions.add(viewModel.shouldShowList()
                                   .subscribe(adapter::setItems,
                                              Throwable::printStackTrace,
                                              () -> System.out.println("PJC completed")));

        // The detail container view will be present only in the
        // large-screen layouts (res/values-w900dp).
        // If this view is present, then the
        // activity should be in two-pane mode.
        subscriptions.add(detailContainer == null
                          ? showInActivity(adapter.itemClicked())
                          : showInFragment(adapter.itemClicked()));
    }

    @Override
    protected void onDestroy() {
        subscriptions.clear();
        unbinder.unbind();
        super.onDestroy();
    }

    private Subscription showInFragment(Observable<LottieFile> lottieFile) {
        return lottieFile.map(LottieDetailFragment::createInstance)
                         .subscribe(this::setDetailFragment, Throwable::printStackTrace);
    }

    private Subscription showInActivity(Observable<LottieFile> lottieFile) {
        return lottieFile.map(item -> LottieDetailActivity.createIntent(this, item))
                         .subscribe(this::startActivity, Throwable::printStackTrace);
    }

    private int setDetailFragment(LottieDetailFragment fragment) {
        return getSupportFragmentManager().beginTransaction().replace(R.id.lottie_detail_container, fragment).commit();
    }
}
