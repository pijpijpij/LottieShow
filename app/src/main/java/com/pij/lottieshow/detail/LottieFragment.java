package com.pij.lottieshow.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.pij.lottieshow.R;
import com.pij.lottieshow.list.LottiesActivity;
import com.pij.lottieshow.model.Converter;
import com.pij.lottieshow.model.LottieContent;
import com.pij.lottieshow.model.LottieUi;
import com.pij.lottieshow.ui.Utils;

import javax.inject.Inject;

import activitystarter.Arg;
import activitystarter.MakeActivityStarter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import rx.Observable;
import rx.Single;
import rx.subscriptions.CompositeSubscription;

/**
 * A fragment representing a single Lottie detail screen.
 * This fragment is either contained in a {@link LottiesActivity}
 * in two-pane mode (on tablets) or a {@link LottieActivity}
 * on handsets.
 */
@MakeActivityStarter
public class LottieFragment extends DaggerFragment {

    @Arg
    LottieUi lottie;

    @Inject
    LottieViewModel viewModel;
    @Inject
    Converter converter;

    @BindView(R.id.animation)
    LottieAnimationView animation;
    @BindView(R.id.version)
    TextView version;
    private Unbinder unbinder;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    @NonNull
    public static LottieFragment createInstance(LottieUi item) {
        return LottieFragmentStarter.newInstance(item);
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
        LottieFragmentStarter.fill(this);
        unbinder = ButterKnife.bind(this, view);

        // TODO Move this code to LottieActivity
        updateToolbar();

        Observable<LottieContent> content = viewModel.showAnimation().map(Single::just).map(LottieContent::create);
        subscriptions.addAll(
                // Display the lottie
                content.flatMapSingle(LottieContent::content).subscribe(animation::setAnimation, this::notifyError),
                // Display its version
                content.flatMapSingle(LottieContent::version).subscribe(version::setText, this::notifyError),

                // Display errors
                viewModel.showLoadingError().subscribe(this::notifyError, this::notifyError));

        // display the content
        if (lottie != null) {
            subscriptions.add(converter.toModel(lottie).subscribe(viewModel::loadLottie, this::notifyError));
        }
    }

    @Override
    public void onDestroyView() {
        subscriptions.clear();
        animation.cancelAnimation();
        unbinder.unbind();
        super.onDestroyView();
    }

    private void notifyError(Throwable error) {
        Utils.notifyError(error, animation);
    }

    private void updateToolbar() {
        if (lottie != null) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            getActivity().setTitle(lottie.label());
        }
    }
}
