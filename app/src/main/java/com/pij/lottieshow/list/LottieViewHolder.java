package com.pij.lottieshow.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pij.lottieshow.model.LottieUi;

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
    @BindView(android.R.id.text1)
    TextView labelView;
    private LottieUi item;

    @SuppressWarnings("WeakerAccess")
    public LottieViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + labelView.getText() + "'";
    }

    @SuppressWarnings("WeakerAccess")
    public void setItem(LottieUi item) {
        this.item = item;
        labelView.setText(item.label());
    }

    @NonNull
    @SuppressWarnings("WeakerAccess")
    public Observable<LottieUi> viewClicked() {
        return clicks(itemView).map(click -> item);
    }

    @SuppressWarnings("WeakerAccess")
    public void setClickListener(Observer<LottieUi> observer) {
        resetClickListener();
        subscription.add(viewClicked().subscribe(observer));
    }

    @SuppressWarnings("WeakerAccess")
    public void resetClickListener() {subscription.clear();}
}
