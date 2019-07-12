package com.nlptech.function.languagesetting.langadded;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.nlptech.function.languagesetting.LanguageSettingAdapter;

import org.jetbrains.annotations.NotNull;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final LanguageSettingAdapter mAdapter;
    private int dragFrom = -1;
    private int dragTo = -1;

    public ItemTouchHelperCallback(LanguageSettingAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return mAdapter.isLongPressDragEnabled();
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return mAdapter.isItemViewSwipeEnabled();
    }

    @Override
    public int getMovementFlags(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder
            , @NotNull RecyclerView.ViewHolder target) {
        if(dragFrom == -1) {
            dragFrom =  viewHolder.getAdapterPosition();
        }
        dragTo = target.getAdapterPosition();
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (viewHolder == null) {
            return;
        }
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setSelected(true);
        }
    }

    @Override
    public void clearView(@NotNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if(dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
            mAdapter.onReallyItemMoved(dragFrom, dragTo);
        }

        dragFrom = dragTo = -1;

        viewHolder.itemView.setSelected(false);
    }

    @Override
    public float getMoveThreshold(@NotNull RecyclerView.ViewHolder viewHolder) {
        return .75f;
    }
}
