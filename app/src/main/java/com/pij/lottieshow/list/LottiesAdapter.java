package com.pij.lottieshow.list;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pij.lottieshow.model.LottieUi;

import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.IterableUtils.toList;

/**
 * <p>Created on 06/04/2017.</p>
 * @author Pierrejean
 */
class LottiesAdapter extends RecyclerView.Adapter<LottieViewHolder> {

    private final PublishSubject<LottieUi> itemClicked = PublishSubject.create();
    private final PublishSubject<LottieUi> contentNeeded = PublishSubject.create();
    private final int itemLayout;
    @NonNull
    private List<LottieUi> values = emptyList();

    LottiesAdapter(@LayoutRes int itemLayout) {
        this.itemLayout = itemLayout;
    }

    @Override
    public LottieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new LottieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LottieViewHolder holder, int position) {
        LottieUi item = values.get(position);
        holder.setItem(item);
        if (item.content() == null) {
            contentNeeded.onNext(item);
        }
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    @Override
    public void onViewAttachedToWindow(LottieViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.setClickListener(itemClicked);
    }

    @Override
    public void onViewDetachedFromWindow(LottieViewHolder holder) {
        holder.resetClickListener();
        super.onViewDetachedFromWindow(holder);
    }

    @SuppressWarnings("WeakerAccess")
    public void setItems(Iterable<LottieUi> items) {
        values = toList(items);
        notifyDataSetChanged();
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<LottieUi> itemClicked() {
        return itemClicked.asObservable();
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<LottieUi> contentNeeded() {
        return contentNeeded.asObservable();
    }

}
