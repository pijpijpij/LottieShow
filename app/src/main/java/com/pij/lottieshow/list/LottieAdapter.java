package com.pij.lottieshow.list;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pij.lottieshow.model.LottieFile;

import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

import static com.jakewharton.rxbinding.view.RxView.clicks;
import static com.jakewharton.rxbinding.view.RxView.detaches;
import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.IterableUtils.toList;
import static org.apache.commons.collections4.ListUtils.defaultIfNull;

/**
 * <p>Created on 06/04/2017.</p>
 * @author Pierrejean
 */
class LottieAdapter extends RecyclerView.Adapter<LottieViewHolder> {

    private final PublishSubject<LottieFile> itemClicked = PublishSubject.create();
    private final int itemLayout;
    @NonNull
    private List<LottieFile> values = emptyList();

    LottieAdapter(@LayoutRes int itemLayout) {
        this.itemLayout = itemLayout;
    }

    @Override
    public LottieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        LottieViewHolder holder = new LottieViewHolder(view);
        holder.subscription.add(clicks(view).takeUntil(detaches(parent))
                                            .map(click -> holder.item)
                                            .subscribe(itemClicked));
        return holder;
    }

    @Override
    public void onBindViewHolder(final LottieViewHolder holder, int position) {
        LottieFile item = values.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    @Override
    public void onViewRecycled(LottieViewHolder holder) {
        holder.subscription.clear();
        super.onViewRecycled(holder);
    }

    @SuppressWarnings("WeakerAccess")
    public void setItems(@Nullable Iterable<LottieFile> items) {
        values = defaultIfNull(toList(items), emptyList());
        notifyDataSetChanged();
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<LottieFile> itemClicked() {
        return itemClicked.asObservable();
    }

}
