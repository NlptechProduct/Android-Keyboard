package com.nlptech.function.languagesetting.langswitch;

import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.android.inputmethod.latin.R;
import com.android.inputmethod.latin.databinding.ViewholderSubtypeSwitchSingleBinding;
import com.nlptech.function.languagesetting.LangBaseViewHolder;
import com.nlptech.language.IMELanguageWrapper;

public class SubtypeSingleViewHolder extends LangBaseViewHolder<IMELanguageWrapper> {
    private static final int CLICK_ITEM_DELAY = 400;

    private ViewholderSubtypeSwitchSingleBinding binding;

    public View itemView;

    public SubtypeSingleViewHolder(View itemView){
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        this.itemView = itemView;
    }
    @Override
    public void fillView(IMELanguageWrapper entity, int size) {
        binding.displayName.setText(entity.getIMELanguage().getDisplayName());
        binding.localeOrLayoutName.setText(entity.getIMELanguage().getDisplayNameWithLocale());
        itemView.setOnClickListener(v -> {
            if(subtypeSwitchListener == null){
                return;
            }

            binding.btn.setImageResource(R.drawable.ic_theme_icon_in_use);
            // for fully see the checked effect
            itemView.postDelayed(() -> subtypeSwitchListener.onChangeSubtype(entity), CLICK_ITEM_DELAY);
        });
    }
}
