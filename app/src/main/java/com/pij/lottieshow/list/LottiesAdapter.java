package com.pij.lottieshow.list;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pij.lottieshow.model.LottieUi;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import rx.Observable;
import rx.Single;
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
    private List<Pair<LottieUi, Single<String>>> values = emptyList();

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
        Pair<LottieUi, Single<String>> item = values.get(position);
        holder.setItem(item.getLeft());
        holder.setContent(item.getRight());
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
    public void setItems(List<Pair<LottieUi, Single<String>>> items) {
        values = toList(items);
        notifyDataSetChanged();
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<LottieUi> itemClicked() {
        return itemClicked.asObservable();
    }

}
