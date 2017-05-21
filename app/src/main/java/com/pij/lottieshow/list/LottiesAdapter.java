package com.pij.lottieshow.list;

import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pij.lottieshow.model.LottieContent;
import com.pij.lottieshow.model.LottieUi;

import org.apache.commons.lang3.tuple.Pair;

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
    private final int itemLayout;
    @NonNull
    private List<Pair<LottieUi, LottieContent>> values = emptyList();

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
        Pair<LottieUi, LottieContent> item = values.get(position);
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
    public void setItems(@NonNull List<Pair<LottieUi, LottieContent>> items) {
        List<Pair<LottieUi, LottieContent>> oldValues = values;
        values = toList(items);
        DiffUtil.DiffResult diff = DiffUtil.calculateDiff(new MyCallback(oldValues, items));
        diff.dispatchUpdatesTo(this);
    }

    @SuppressWarnings("WeakerAccess")
    public Observable<LottieUi> itemClicked() {
        return itemClicked.asObservable();
    }

    private static class MyCallback extends DiffUtil.Callback {

        private final List<Pair<LottieUi, LottieContent>> oldValues;
        private final List<Pair<LottieUi, LottieContent>> newValues;

        MyCallback(List<Pair<LottieUi, LottieContent>> oldValues, List<Pair<LottieUi, LottieContent>> newValues) {
            this.oldValues = oldValues;
            this.newValues = newValues;
        }

        @Override
        public int getOldListSize() {
            return oldValues.size();
        }

        @Override
        public int getNewListSize() {
            return newValues.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            Uri oldItemId = getItemId(oldItemPosition, oldValues);
            Uri newItemId = getItemId(newItemPosition, newValues);
            return oldItemId.equals(newItemId);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Pair<LottieUi, LottieContent> oldItem = oldValues.get(oldItemPosition);
            Pair<LottieUi, LottieContent> newItem = newValues.get(newItemPosition);
            return oldItem.getLeft().label().equals(newItem.getLeft().label()) &&
                   oldItem.getRight() == newItem.getRight();
        }

        private Uri getItemId(int position, List<Pair<LottieUi, LottieContent>> values) {
            return values.get(position).getLeft().id();
        }
    }
}
