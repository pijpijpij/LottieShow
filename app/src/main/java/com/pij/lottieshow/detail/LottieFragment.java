package com.pij.lottieshow.detail;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.pij.lottieshow.R;
import com.pij.lottieshow.list.LottieListActivity;
import com.pij.lottieshow.model.Converter;
import com.pij.lottieshow.model.LottieUi;
import com.pij.lottieshow.ui.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import rx.subscriptions.CompositeSubscription;

/**
 * A fragment representing a single Lottie detail screen.
 * This fragment is either contained in a {@link LottieListActivity}
 * in two-pane mode (on tablets) or a {@link LottieActivity}
 * on handsets.
 */
@FragmentWithArgs
public class LottieFragment extends DaggerFragment {

    @Arg
    LottieUi lottie;

    @Inject
    LottieViewModel viewModel;
    @Inject
    Converter converter;

    @BindView(R.id.lottie_detail)
    TextView label;
    @BindView(R.id.animation)
    LottieAnimationView animation;
    private Unbinder unbinder;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    @NonNull
    public static LottieFragment createInstance(LottieUi item) {
        return LottieFragmentBuilder.newLottieFragment(item);
    }

    public LottieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lottie_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentArgs.inject(this);
        unbinder = ButterKnife.bind(this, view);

        // TODO Move this code to LottieActivity
        updateToolbar();

        subscriptions.addAll(
                // Display the lottie
                viewModel.showAnimation().map(this::asJsonObject).subscribe(animation::setAnimation, this::notifyError),

                // Display errors
                viewModel.showLoadingError().subscribe(this::notifyError, this::notifyError));

        // display the content
        if (lottie != null) {
            converter.toModel(lottie).subscribe(viewModel::loadLottie, this::notifyError);
            label.setText(lottie.label());
        }
    }

    @Override
    public void onDestroyView() {
        subscriptions.clear();
        animation.cancelAnimation();
        unbinder.unbind();
        super.onDestroyView();
    }

    @NonNull
    private JSONObject asJsonObject(String json) {
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void notifyError(Throwable error) {
        Utils.notifyError(error, animation);
    }

    private void updateToolbar() {
        if (lottie != null) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout)activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(lottie.label());
            }
        }
    }
}
