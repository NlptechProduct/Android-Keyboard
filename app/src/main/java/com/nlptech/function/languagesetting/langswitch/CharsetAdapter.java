package com.nlptech.function.languagesetting.langswitch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.inputmethod.latin.R;
import com.nlptech.language.IMELanguageWrapper;

import java.util.ArrayList;
import java.util.List;

public class CharsetAdapter extends RecyclerView.Adapter<CharsetViewHolder> {
    private List<String> dataSet = new ArrayList<>();

    private String currentSubtype;

    private IMELanguageWrapper wrapper;

    private SubtypeSwitchListener listener;

    public CharsetAdapter(SubtypeSwitchListener listener) {
        this.listener = listener;
    }

    public void setDataSet(IMELanguageWrapper wrapper, List<String> dataSet, String currentSubtype) {
        this.dataSet = dataSet;
        this.currentSubtype = currentSubtype;
        this.wrapper = wrapper;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CharsetViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.viewholder_charset, viewGroup, false);
        CharsetViewHolder viewHolder = new CharsetViewHolder(itemView,wrapper);
        itemView.setTag(viewHolder);
        viewHolder.setListener(listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CharsetViewHolder langBaseViewHolder, int pos) {
        langBaseViewHolder.fillView(dataSet.get(pos), currentSubtype);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
