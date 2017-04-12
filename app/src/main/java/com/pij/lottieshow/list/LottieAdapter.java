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

import static com.jakewharton.rxbinding.view.RxView.clicks;
import static com.jakewharton.rxbinding.view.RxView.detaches;
import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.IterableUtils.toList;

/**
 * <p>Created on 06/04/2017.</p>
 * @author Pierrejean
 */
class LottieAdapter extends RecyclerView.Adapter<LottieViewHolder> {

    private final PublishSubject<LottieUi> itemClicked = PublishSubject.create();
    private final int itemLayout;
    @NonNull
    private List<LottieUi> values = emptyList();

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
        LottieUi item = values.get(position);
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
    public void setItems(Iterable<LottieUi> items) {
        values = toList(items);
        notifyDataSetChanged();
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<LottieUi> itemClicked() {
        return itemClicked.asObservable();
    }

}
