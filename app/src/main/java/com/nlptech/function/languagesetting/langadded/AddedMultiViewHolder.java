package com.nlptech.function.languagesetting.langadded;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.inputmethod.latin.databinding.ViewholderAddedMultiBinding;
import com.nlptech.function.languagesetting.LangBaseViewHolder;
import com.nlptech.function.languagesetting.LanguageSettingAdapter;
import com.nlptech.language.IMELanguage;
import com.nlptech.language.IMELanguageWrapper;

import java.util.List;

public class AddedMultiViewHolder extends LangBaseViewHolder<IMELanguageWrapper> {

    private ViewholderAddedMultiBinding binding;

    private LanguageSettingAdapter adapter;

    public View itemView;

    public AddedMultiViewHolder(View itemView){
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        this.itemView = itemView;
    }
    @Override
    public void fillView(IMELanguageWrapper entity, int size) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), RecyclerView.VERTICAL,false);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.layoutSetName.setText(entity.getMultiIMELanguage().getLayoutName());
        if(size <= 1){
            binding.removeBtn.setVisibility(View.GONE);
        }else{
            binding.removeBtn.setVisibility(View.VISIBLE);
        }
        adapter = new LanguageSettingAdapter();
        binding.recyclerView.setAdapter(adapter);
        adapter.setDataSet(entity.getMultiIMELanguage().getIMELanguageWrappers());

        binding.removeBtn.setOnClickListener(v -> {
            if(languageListener == null){
                return;
            }
            languageListener.onClickRemove(entity);
        });

        binding.recyclerView.setOnTouchListener((v, event) -> itemView.onTouchEvent(event));

        itemView.setOnClickListener(v -> {
            if(languageListener == null){
                return;
            }
            List<IMELanguage> imeList = entity.getMultiIMELanguage().getSubtypeIMEList();
            if (imeList.size() > 0) {
                languageListener.onClickChangeLayoutSet(imeList.get(0));
            }
        });
    }
}
