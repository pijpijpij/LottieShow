package com.pij.lottieshow.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.pij.lottieshow.R;
import com.pij.lottieshow.model.LottieContent;
import com.pij.lottieshow.model.LottieUi;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.subscriptions.CompositeSubscription;

import static com.jakewharton.rxbinding.view.RxView.clicks;

/**
 * <p>Created on 06/04/2017.</p>
 * @author Pierrejean
 */
class LottieViewHolder extends RecyclerView.ViewHolder {

    private final CompositeSubscription subscription = new CompositeSubscription();
    @BindView(R.id.label)
    TextView label;
    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.placeholder)
    ImageView placeholder;
    @BindView(R.id.image)
    LottieAnimationView image;
    private Observable<LottieUi> clicks;
    private LottieContent content;

    @SuppressWarnings("WeakerAccess")
    public LottieViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + label.getText() + "'";
    }

    @SuppressWarnings("WeakerAccess")
    public void setItem(LottieUi item) {
        clicks = clicks(itemView).map(click -> item);
        label.setText(item.label());
        placeholder.setVisibility(View.VISIBLE);
        image.setVisibility(View.INVISIBLE);
    }

    @SuppressWarnings("WeakerAccess")
    public void setClickListener(Observer<LottieUi> observer) {
        resetClickListener();
        subscription.addAll(clicks.subscribe(observer),
                            content.content().subscribe(this::setAnimation, this::showFailedLoad),
                            content.version().subscribe(this::setVersion, this::showFailedLoad));
    }

    @SuppressWarnings("WeakerAccess")
    public void resetClickListener() {
        subscription.clear();
    }

    public void setContent(@NonNull LottieContent content) {
        this.content = content;
    }

    private void setAnimation(@Nullable JSONObject content) {
        if (content == null) {
            showFailedContent();
        } else {
            image.setAnimation(content);
            image.setVisibility(View.VISIBLE);
            placeholder.setVisibility(View.INVISIBLE);
        }
    }

    private void setVersion(String newValue) {
        version.setText(newValue);
    }

    private void showFailedLoad(Throwable error) {
        error.printStackTrace();
        showFailedContent();
    }

    private void showFailedContent() {
        placeholder.setImageResource(R.drawable.ic_broken_image_black_24dp);
        placeholder.setVisibility(View.VISIBLE);
        image.setVisibility(View.INVISIBLE);
    }

    @Nullable
    private JSONObject asJSON(@Nullable String input) {
        if (input == null) return null;
        try {
            return new JSONObject(input);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
