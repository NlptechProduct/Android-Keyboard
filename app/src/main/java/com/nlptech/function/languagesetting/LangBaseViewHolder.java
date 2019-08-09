package com.nlptech.function.languagesetting;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.nlptech.function.languagesetting.langswitch.SubtypeSwitchListener;

public abstract class LangBaseViewHolder<E> extends RecyclerView.ViewHolder {

    protected LanguageListener languageListener;

    protected SubtypeSwitchListener subtypeSwitchListener;

    public LangBaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void fillView(E entity,int size);

    public void setLangListener(LanguageListener listener){
        this.languageListener = listener;
    }

    public void setSubtypeListener(SubtypeSwitchListener listener){
        this.subtypeSwitchListener = listener;
    }
}
