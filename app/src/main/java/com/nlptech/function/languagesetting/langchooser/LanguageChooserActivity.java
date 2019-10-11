package com.nlptech.function.languagesetting.langchooser;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.inputmethod.latin.R;
import com.android.inputmethod.latin.databinding.ActivityLanguageChooseBinding;
import com.nlptech.Agent;
import com.nlptech.function.dictionary.DictionaryDownloadManager;
import com.nlptech.function.dictionary.DictionaryListener;
import com.nlptech.function.languagesetting.LanguageListener;
import com.nlptech.function.languagesetting.LanguageSettingAdapter;
import com.nlptech.function.languagesetting.LanguageSettingViewModel;
import com.nlptech.language.IMELanguage;
import com.nlptech.language.IMELanguageWrapper;
import com.nlptech.language.VertexInputMethodManager;
import com.nlptech.ui.ToolBarActivity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LanguageChooserActivity extends ToolBarActivity implements LanguageListener, DictionaryListener {
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
        DictionaryDownloadManager.getInstance().addLocale(item.getIMELanguage().getLocale());
        DictionaryDownloadManager.getInstance().requestDictionaryConfig(VertexInputMethodManager.getInstance().obtainLocaleStringForDownload());
    }

    @Override
    public void onClickRemove(IMELanguageWrapper item) {
        viewModel.removeSubtype(item);
        if(item.getType() == IMELanguageWrapper.TYPE_MULTI){
            Set<String> localList = new HashSet<>();
            for(IMELanguage IMELanguage :item.getMultiIMELanguage().getSubtypeIMEList()){
                localList.add(IMELanguage.getLocale());
            }
            DictionaryDownloadManager.getInstance().removeLocaleAll(localList);
        }else{
            DictionaryDownloadManager.getInstance().removeLocale(item.getIMELanguage().getLocale());
        }
        DictionaryDownloadManager.getInstance().requestDictionaryConfig(VertexInputMethodManager.getInstance().obtainLocaleStringForDownload());
    }

    @Override
    public void onLanguagePositionChanged(int from, int to) {

    }

    @Override
    public void onClickChangeLayoutSet(IMELanguage imeLanguage) {

    }

    @Override
    public void onDownloadComplete() {
        if (adapter == null) {
            return;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDownloadFail(int errorCode) {
        Toast.makeText(this,"onDownloadDictionaryFail() errorCode="+errorCode,Toast.LENGTH_LONG).show();
        if (adapter == null) {
            return;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutResource() {
        return LAYOUT;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.language_choose_search, menu);

        MenuItem menuSearchItem = menu.findItem(R.id.search);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menuSearchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setOnCloseListener(() -> {
            viewModel.filterChooseResult(null);
            return false;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                viewModel.filterChooseResult(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                viewModel.filterChooseResult(s);
                return false;
            }
        });

        return true;
    }

}
