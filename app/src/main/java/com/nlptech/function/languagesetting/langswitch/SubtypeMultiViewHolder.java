package com.nlptech.function.languagesetting.langswitch;

import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.android.inputmethod.latin.R;
import com.android.inputmethod.latin.databinding.ViewholderSubtypeSwitchMultiBinding;
import com.nlptech.function.languagesetting.LangBaseViewHolder;
import com.nlptech.language.IMELanguage;
import com.nlptech.language.IMELanguageWrapper;

import java.util.List;

public class SubtypeMultiViewHolder extends LangBaseViewHolder<IMELanguageWrapper> {
    private static final int CLICK_ITEM_DELAY = 400;

    private ViewholderSubtypeSwitchMultiBinding binding;

    public View itemView;

    public SubtypeMultiViewHolder(View itemView){
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        this.itemView = itemView;
    }
    @Override
    public void fillView(IMELanguageWrapper entity, int size) {
        List<IMELanguage> list = entity.getMultiIMELanguage().getSubtypeIMEList();
        //如果没有多语言
        if(list.size() <= 1){
            binding.displayName.setText(list.get(0).getDisplayName());
            binding.localeOrLayoutName.setText(list.get(0).getDisplayNameWithLocale());
        }else{
            binding.displayName.setText(itemView.getContext().getString(R.string.subtype_switch_multi_language));
            binding.localeOrLayoutName.setText(entity.getMultiIMELanguage().getLayoutSet());
        }

        binding.layout.setOnClickListener(v -> {
            if(subtypeSwitchListener == null){
                return;
            }
            itemView.postDelayed(() -> subtypeSwitchListener.onChangeSubtype(entity), CLICK_ITEM_DELAY);
        });
    }
}
