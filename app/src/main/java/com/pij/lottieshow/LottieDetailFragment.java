package com.pij.lottieshow;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.pij.lottieshow.list.LottieListActivity;
import com.pij.lottieshow.model.LottieFile;
import com.pij.lottieshow.model.LottieUi;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.pij.lottieshow.model.LottieUi.create;

/**
 * A fragment representing a single Lottie detail screen.
 * This fragment is either contained in a {@link LottieListActivity}
 * in two-pane mode (on tablets) or a {@link LottieDetailActivity}
 * on handsets.
 */
@FragmentWithArgs
public class LottieDetailFragment extends Fragment {


    /**
     * The dummy content this fragment is presenting.
     */
    @Arg
    LottieUi mItem;
    private Unbinder unbinder;

    @NonNull
    public static LottieDetailFragment createInstance(LottieFile item) {
        return createInstance(create(item));
    }

    @NonNull
    public static LottieDetailFragment createInstance(LottieUi item) {
        return LottieDetailFragmentBuilder.newLottieDetailFragment(item);
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LottieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
        if (mItem != null) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout)activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lottie_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView)view.findViewById(R.id.lottie_detail)).setText(mItem.label());
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
