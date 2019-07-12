package com.nlptech.function.languagesetting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.inputmethod.latin.R;
import com.nlptech.function.languagesetting.langadded.AddedMultiChildViewHolder;
import com.nlptech.function.languagesetting.langadded.AddedMultiViewHolder;
import com.nlptech.function.languagesetting.langadded.AddedSingleViewHolder;
import com.nlptech.function.languagesetting.langchooser.ChooseSingleViewHolder;
import com.nlptech.language.IMELanguageWrapper;

import java.util.ArrayList;
import java.util.List;

public class LanguageSettingAdapter extends RecyclerView.Adapter<LangBaseViewHolder<IMELanguageWrapper>> {
    private List<IMELanguageWrapper> dataSet = new ArrayList<>();

    private LanguageListener listener;

    public LanguageSettingAdapter() {

    }

    public LanguageSettingAdapter(LanguageListener listener) {
        this.listener = listener;
    }

    public void setDataSet(List<IMELanguageWrapper> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LangBaseViewHolder<IMELanguageWrapper> onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case IMELanguageWrapper.TYPE_SINGLE:
                return onCreateAddedSingleHolder(viewGroup);
            case IMELanguageWrapper.TYPE_MULTI:
                return onCreateAddedMultiHolder(viewGroup);
            case IMELanguageWrapper.TYPE_CHOOSE_SINGLE:
                return onCreateChooseSingleHolder(viewGroup);
            case IMELanguageWrapper.TYPE_MULTI_CHILD:
                return onCreateAddedMultiChildHolder(viewGroup);
        }
        return onCreateAddedSingleHolder(viewGroup);
    }

    private LangBaseViewHolder onCreateAddedSingleHolder(ViewGroup parent) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.viewholder_added_single, parent, false);
        AddedSingleViewHolder viewHolder = new AddedSingleViewHolder(itemView);
        itemView.setTag(viewHolder);
        viewHolder.setLangListener(listener);
        return viewHolder;
    }

    private LangBaseViewHolder onCreateAddedMultiHolder(ViewGroup parent) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.viewholder_added_multi, parent, false);
        AddedMultiViewHolder viewHolder = new AddedMultiViewHolder(itemView);
        itemView.setTag(viewHolder);
        viewHolder.setLangListener(listener);
        return viewHolder;
    }

    private LangBaseViewHolder onCreateChooseSingleHolder(ViewGroup parent) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.viewholder_choose_single, parent, false);
        ChooseSingleViewHolder viewHolder = new ChooseSingleViewHolder(itemView);
        itemView.setTag(viewHolder);
        viewHolder.setLangListener(listener);
        return viewHolder;
    }

    private LangBaseViewHolder onCreateAddedMultiChildHolder(ViewGroup parent) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.viewholder_added_child, parent, false);
        AddedMultiChildViewHolder viewHolder = new AddedMultiChildViewHolder(itemView);
        itemView.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LangBaseViewHolder<IMELanguageWrapper> langBaseViewHolder, int pos) {
        langBaseViewHolder.fillView(dataSet.get(pos),getItemCount());
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    // For Drag +
    public boolean isLongPressDragEnabled() {
        return true;
    }

    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    public void onItemMove(int fromPosition, int toPosition) {
        IMELanguageWrapper data = dataSet.get(fromPosition);
        if (fromPosition < toPosition) {
            dataSet.remove(fromPosition);
            dataSet.add(toPosition, data);
        } else {
            dataSet.add(toPosition, data);
            dataSet.remove(fromPosition + 1);
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public void onReallyItemMoved(int fromPosition, int toPosition) {
        listener.onLanguagePositionChanged(fromPosition, toPosition);
    }
    // For Drag -
}
