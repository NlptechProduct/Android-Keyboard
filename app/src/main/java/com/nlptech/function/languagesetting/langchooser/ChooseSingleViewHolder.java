package com.nlptech.function.languagesetting.langchooser;

import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.android.inputmethod.latin.databinding.ViewholderChooseSingleBinding;
import com.nlptech.function.dictionary.DictionaryDownloadManager;
import com.nlptech.function.languagesetting.LangBaseViewHolder;
import com.nlptech.language.IMELanguageWrapper;
import com.nlptech.language.VertexInputMethodManager;

public class ChooseSingleViewHolder extends LangBaseViewHolder<IMELanguageWrapper> {

    private ViewholderChooseSingleBinding binding;

    public View itemView;

    public ChooseSingleViewHolder(View itemView){
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        this.itemView = itemView;
    }
    @Override
    public void fillView(IMELanguageWrapper entity, int size) {
        binding.displayName.setText(entity.getIMELanguage().getDisplayName());
        binding.layoutSetName.setText(entity.getIMELanguage().getDisplayNameWithLocale());
        itemView.setOnClickListener(v -> {
            if(languageListener == null){
                return;
            }
            boolean added = VertexInputMethodManager.getInstance().inSingleAddedList(entity.getIMELanguage());
            if (added) {
                languageListener.onClickRemove(entity);
            } else {
                languageListener.onClickAdd(entity);
            }
            updateDownloadingStatus(entity);
        });
        updateDownloadingStatus(entity);
    }

    private void updateDownloadingStatus(IMELanguageWrapper entity){
        if(!VertexInputMethodManager.getInstance().inSingleAddedList(entity.getIMELanguage())){
            binding.checkIcon.setVisibility(View.GONE);
            binding.loadingProgressBar.setVisibility(View.GONE);
            return;
        }
        if(DictionaryDownloadManager.getInstance().queryLocaleDownload(entity.getIMELanguage().getLocale())){
            //已经下载成功
            binding.loadingProgressBar.setVisibility(View.GONE);
            binding.checkIcon.setVisibility(View.VISIBLE);
        }else{
            //正在下载中
            binding.loadingProgressBar.setVisibility(View.VISIBLE);
            binding.checkIcon.setVisibility(View.GONE);
        }
    }
}
