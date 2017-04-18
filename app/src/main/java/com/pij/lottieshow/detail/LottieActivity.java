package com.pij.lottieshow.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.pij.lottieshow.R;
import com.pij.lottieshow.list.LottiesActivity;
import com.pij.lottieshow.model.LottieUi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.subscriptions.CompositeSubscription;
import se.emilsjolander.intentbuilder.Extra;
import se.emilsjolander.intentbuilder.IntentBuilder;

/**
 * An activity representing a single Lottie detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item label are presented side-by-side with a list of items
 * in a {@link LottiesActivity}.
 */
@IntentBuilder
public class LottieActivity extends AppCompatActivity {

    private final CompositeSubscription subscriptions = new CompositeSubscription();
    @Extra
    LottieUi file;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private Unbinder unbinder;

    @NonNull
    public static Intent createIntent(Context context, LottieUi item) {
        return new LottieActivityIntentBuilder(item).build(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LottieActivityIntentBuilder.inject(getIntent(), this);
        setContentView(R.layout.activity_lottie_detail);
        unbinder = ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        // Show the Up button in the action bar.
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                                               .setAction("Action", null)
                                               .show());

        if (savedInstanceState == null) {
            setDetailFragment(LottieFragment.createInstance(file));
        }
    }

    @Override
    protected void onDestroy() {
        subscriptions.clear();
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateUpTo(LottiesActivity.createIntent(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int setDetailFragment(LottieFragment fragment) {
        return getSupportFragmentManager().beginTransaction().replace(R.id.lottie_detail_container, fragment).commit();
    }
}
