package com.nlptech.function.languagesetting.langadded;

import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.android.inputmethod.latin.databinding.ViewholderAddedChildBinding;
import com.nlptech.function.languagesetting.LangBaseViewHolder;
import com.nlptech.language.IMELanguageWrapper;

public class AddedMultiChildViewHolder extends LangBaseViewHolder<IMELanguageWrapper> {

    private ViewholderAddedChildBinding binding;

    public View itemView;

    public AddedMultiChildViewHolder(View itemView){
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        this.itemView = itemView;
    }
    @Override
    public void fillView(IMELanguageWrapper entity, int size) {
        binding.displayName.setText(entity.getIMELanguage().getDisplayName());
    }
}
