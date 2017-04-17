package com.pij.lottieshow.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.pij.lottieshow.R;
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
    @BindView(R.id.image)
    LottieAnimationView image;
    private Observable<LottieUi> clicks;

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
        if (item.content() == null) {
            // TODO insert a placeholder image
        } else {
            try {
                image.setAnimation(new JSONObject(item.content()));
            } catch (JSONException e) {
                // TODO deal with error/ show broken image
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void setClickListener(Observer<LottieUi> observer) {
        resetClickListener();
        subscription.add(clicks.subscribe(observer));
    }

    @SuppressWarnings("WeakerAccess")
    public void resetClickListener() {
        subscription.clear();
    }
}
