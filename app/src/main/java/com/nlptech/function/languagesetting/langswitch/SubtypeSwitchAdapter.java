package com.nlptech.function.languagesetting.langswitch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.inputmethod.latin.R;
import com.nlptech.function.languagesetting.LangBaseViewHolder;
import com.nlptech.language.IMELanguageWrapper;

import java.util.ArrayList;
import java.util.List;

public class SubtypeSwitchAdapter extends RecyclerView.Adapter<LangBaseViewHolder<IMELanguageWrapper>> {
    private List<IMELanguageWrapper> dataSet = new ArrayList<>();

    private SubtypeSwitchListener listener;

    public SubtypeSwitchAdapter(SubtypeSwitchListener subtypelistener) {
        this.listener = subtypelistener;
    }

    public void setDataSet(List<IMELanguageWrapper> data) {
        this.dataSet = data;
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
        }
        return onCreateAddedSingleHolder(viewGroup);
    }

    private LangBaseViewHolder onCreateAddedSingleHolder(ViewGroup parent) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.viewholder_subtype_switch_single, parent, false);
        SubtypeSingleViewHolder viewHolder = new SubtypeSingleViewHolder(itemView);
        itemView.setTag(viewHolder);
        viewHolder.setSubtypeListener(listener);
        return viewHolder;
    }

    private LangBaseViewHolder onCreateAddedMultiHolder(ViewGroup parent) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.viewholder_subtype_switch_multi, parent, false);
        SubtypeMultiViewHolder viewHolder = new SubtypeMultiViewHolder(itemView);
        itemView.setTag(viewHolder);
        viewHolder.setSubtypeListener(listener);
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
}
