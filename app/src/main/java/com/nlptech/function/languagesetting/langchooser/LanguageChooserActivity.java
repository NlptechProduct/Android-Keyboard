package com.nlptech.function.languagesetting.langchooser;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.inputmethod.latin.R;
import com.android.inputmethod.latin.databinding.ActivityLanguageChooseBinding;
import com.nlptech.Agent;
import com.nlptech.function.dictionary.DictionaryListener;
import com.nlptech.function.languagesetting.LanguageListener;
import com.nlptech.function.languagesetting.LanguageSettingAdapter;
import com.nlptech.function.languagesetting.LanguageSettingViewModel;
import com.nlptech.language.IMELanguageWrapper;

import java.util.List;

public class LanguageChooserActivity extends AppCompatActivity implements LanguageListener, DictionaryListener {
    public static final int LAYOUT = R.layout.activity_language_choose;

    private ActivityLanguageChooseBinding binding;

    private LanguageSettingAdapter adapter;

    private LanguageSettingViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        binding = DataBindingUtil.setContentView(this, LAYOUT);
        viewModel = ViewModelProviders.of(this).get(LanguageSettingViewModel.class);
        viewModel.getChooseResult().observe(this,this::handleResponse);
        adapter = new LanguageSettingAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.recyclerView.setAdapter(adapter);
        Agent.getInstance().registerDictionaryDownloadListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.obtainList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.filterChooseResult(null);
        Agent.getInstance().unregisterDictionaryDownloadListener();
    }

    private void handleResponse(List<IMELanguageWrapper> result) {
        adapter.setDataSet(result);
    }

    @Override
    public void onClickAdd(IMELanguageWrapper item) {
        viewModel.addSubtype(item.getIMELanguage());
        Agent.getInstance().downloadDictionary();
        if (adapter == null) {
            return;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClickRemove(IMELanguageWrapper item) {
        viewModel.removeSubtype(item);
        Agent.getInstance().downloadDictionary();
        if (adapter == null) {
            return;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLanguagePositionChanged(int from, int to) {

    }

    @Override
    public void onClickChangeLayoutSet(String charset) {

    }

    @Override
    public void onDownloadComplete() {
        if (adapter == null) {
            return;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDownloadFail() {
        Toast.makeText(this,"onDownloadDictionaryFail()",Toast.LENGTH_LONG).show();
        if (adapter == null) {
            return;
        }
        adapter.notifyDataSetChanged();
    }

}
