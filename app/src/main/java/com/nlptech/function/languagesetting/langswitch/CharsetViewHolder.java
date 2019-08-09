package com.nlptech.function.languagesetting.langswitch;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.android.inputmethod.latin.databinding.ViewholderCharsetBinding;
import com.nlptech.language.IMELanguageWrapper;
import com.nlptech.language.LayoutDisplayTable;

public class CharsetViewHolder extends RecyclerView.ViewHolder {
    private static final int CLICK_ITEM_DELAY = 400;

    private ViewholderCharsetBinding binding;

    private View itemView;

    private IMELanguageWrapper wrapper;

    private SubtypeSwitchListener listener;

    public CharsetViewHolder(View itemView, IMELanguageWrapper wrapper){
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        this.itemView = itemView;
        this.wrapper = wrapper;
    }

    public void setListener(SubtypeSwitchListener listener) {
        this.listener = listener;
    }

    public void fillView(String entity, String currentSub) {
        binding.displayName.setText(LayoutDisplayTable.getInstance().obtainDisplayLayout(entity));
        //当前subtype
        if(currentSub.equalsIgnoreCase(entity)){
            binding.btn.setChecked(true);
        }else{
            binding.btn.setChecked(false);
        }

        binding.layout.setOnClickListener(v ->{
            if(listener == null){
                return;
            }
            if(currentSub.equalsIgnoreCase(entity)){
                return;
            }
            itemView.postDelayed(() -> listener.onChangeLayout(wrapper, entity), CLICK_ITEM_DELAY);
        });
    }

}
