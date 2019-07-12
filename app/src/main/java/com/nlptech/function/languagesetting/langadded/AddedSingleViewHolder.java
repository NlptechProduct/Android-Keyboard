package com.nlptech.function.languagesetting.langadded;

import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.android.inputmethod.latin.databinding.ViewholderAddedSingleBinding;
import com.nlptech.function.languagesetting.LangBaseViewHolder;
import com.nlptech.language.IMELanguageWrapper;

public class AddedSingleViewHolder extends LangBaseViewHolder<IMELanguageWrapper> {

    private ViewholderAddedSingleBinding binding;

    public View itemView;

    public AddedSingleViewHolder(View itemView){
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        this.itemView = itemView;
    }
    @Override
    public void fillView(IMELanguageWrapper entity, int size) {
        binding.displayName.setText(entity.getIMELanguage().getDisplayName());
        binding.layoutSetName.setText(entity.getIMELanguage().getLayoutName());
        if(size == 1){
            binding.removeBtn.setVisibility(View.GONE);
        }else{
            binding.removeBtn.setVisibility(View.VISIBLE);
        }
        binding.removeBtn.setOnClickListener(v -> {
            if(languageListener == null){
                return;
            }
            languageListener.onClickRemove(entity);
        });
        binding.layoutSetName.setOnClickListener(v -> {
            languageListener.onClickChangeLayoutSet(entity.getIMELanguage().getCharset());
        });
    }
}
