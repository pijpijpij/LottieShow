package com.pij.lottieshow.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pij.lottieshow.model.LottieFile;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * <p>Created on 06/04/2017.</p>
 * @author Pierrejean
 */
class LottieViewHolder extends RecyclerView.ViewHolder {

    final CompositeSubscription subscription = new CompositeSubscription();
    LottieFile item;
    //    @BindView(R.id.label)
    @BindView(android.R.id.text1)
    TextView labelView;

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
    public void setItem(LottieFile item) {
        this.item = item;
        labelView.setText(item.label());
    }
}
