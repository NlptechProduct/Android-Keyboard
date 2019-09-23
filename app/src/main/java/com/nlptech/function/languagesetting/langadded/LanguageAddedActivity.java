package com.nlptech.function.languagesetting.langadded;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.inputmethod.latin.R;
import com.android.inputmethod.latin.databinding.ActivityLanguageAddedBinding;
import com.nlptech.Agent;
import com.nlptech.common.utils.DensityUtil;
import com.nlptech.function.dictionary.DictionaryDownloadManager;
import com.nlptech.function.languagesetting.LanguageListener;
import com.nlptech.function.languagesetting.LanguageSettingAdapter;
import com.nlptech.function.languagesetting.LanguageSettingViewModel;
import com.nlptech.function.languagesetting.langchooser.LanguageChooserActivity;
import com.nlptech.language.IMELanguage;
import com.nlptech.language.IMELanguageWrapper;
import com.nlptech.language.VertexInputMethodManager;
import com.nlptech.ui.ToolBarActivity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LanguageAddedActivity extends AppCompatActivity implements LanguageListener,
        SelectLayoutSetDialogFragment.SelectLayoutSetListener, MultiLocaleRemoveDialogFragment.MultiLocaleRemoveListener {
    public static final int LAYOUT = R.layout.activity_language_added;

    private ActivityLanguageAddedBinding binding;

    private LanguageSettingAdapter adapter;

    private LanguageSettingViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, LAYOUT);
        viewModel = ViewModelProviders.of(this).get(LanguageSettingViewModel.class);
        viewModel.getAddedResult().observe(this, this::handleResponse);
        viewModel.getMultiModeLiveData().observe(this, this::handleMultiMode);
        adapter = new LanguageSettingAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.recyclerView.setAdapter(adapter);

        ItemTouchHelperCallback itemTouchHelperCallback = new ItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        touchHelper.attachToRecyclerView(binding.recyclerView);

        binding.multiBtn.setOnClickListener(v -> {
            viewModel.updateMultiMode();
            DictionaryDownloadManager.getInstance().requestDictionaryConfig(VertexInputMethodManager.getInstance().obtainLocaleStringForDownload());
        });
        binding.addLanguage.setOnClickListener(v -> {
            Intent intent = new Intent(this, LanguageChooserActivity.class);
            startActivity(intent);
        });
        binding.enableMultilingualTypingIcon.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog alertDialog=builder.create();
            View altView = View.inflate(this, R.layout.language_multitype_dialog, null);
            altView.findViewById(R.id.multi_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            alertDialog.setView(altView, DensityUtil.dp2px(this,40), 0, DensityUtil.dp2px(this,40), 0);
            alertDialog.show();
            Window window = alertDialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 有白色背景，加这句代码


//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage(R.string.multityping_help_info)
//                    .setTitle(R.string.multityping_help_info_title)
//                    .setCancelable(true)
//                    .setView(R.layout.activity_language_choose)
//                    .setPositiveButton(getString(R.string.keyboard_language_ok), (dialog, id) -> {
//                        dialog.dismiss();
//                    });
//            AlertDialog alert = builder.create();
//            alert.show();
//            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xffff6f61);
        });

//        setupSupportActionBar(binding.toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.obtainList();
    }

    private void handleResponse(List<IMELanguageWrapper> result) {
        adapter.setDataSet(result);
    }

    private void handleMultiMode(Boolean bool) {
        binding.multiBtn.setChecked(bool);
    }

    @Override
    public void onClickAdd(IMELanguageWrapper item) {
        viewModel.addSubtype(item.getIMELanguage());
        DictionaryDownloadManager.getInstance().addLocale(item.getIMELanguage().getLocale());
        DictionaryDownloadManager.getInstance().requestDictionaryConfig(VertexInputMethodManager.getInstance().obtainLocaleStringForDownload());
    }

    @Override
    public void onClickRemove(IMELanguageWrapper item) {
        if (item.getType() == IMELanguageWrapper.TYPE_MULTI) {
            List<IMELanguage> IMELanguages = item.getMultiIMELanguage().getSubtypeIMEList();
            if (IMELanguages.size() > 1) {
                showMultiLocaleRemoveDialogFragment(IMELanguages);
            } else {
                Set<String> localList = new HashSet<>();
                for (com.nlptech.language.IMELanguage IMELanguage : IMELanguages) {
                    localList.add(IMELanguage.getLocale());
                }
                DictionaryDownloadManager.getInstance().removeLocaleAll(localList);
                viewModel.removeSubtype(item);
            }
        } else {
            viewModel.removeSubtype(item);
            DictionaryDownloadManager.getInstance().removeLocale(item.getIMELanguage().getLocale());
        }

        DictionaryDownloadManager.getInstance().requestDictionaryConfig(VertexInputMethodManager.getInstance().obtainLocaleStringForDownload());
    }

    @Override
    public void onLanguagePositionChanged(int from, int to) {
        viewModel.moveSubtype(from, to);
    }

    @Override
    public void onClickChangeLayoutSet(IMELanguage imeLanguage) {
        if(imeLanguage == null){
            return;
        }
        List<String> layoutSets = Agent.getInstance().obtainLayoutList(imeLanguage);
        if (layoutSets == null || layoutSets.size() <= 0) {
            return;
        }

        String currentLayoutSet = imeLanguage.getLayout();

        showSelectLayoutSetDialogFragment(imeLanguage,imeLanguage.getCharset(), currentLayoutSet, layoutSets);
    }

    private void showSelectLayoutSetDialogFragment(IMELanguage imeLanguage, String charset, String currentLayoutSet, List<String> layoutSets) {
        SelectLayoutSetDialogFragment f = SelectLayoutSetDialogFragment.newInstance();
        f.setCharset(charset);
        f.setCurrentLayoutSet(currentLayoutSet);
        f.setLayoutSets(layoutSets);
        f.setSelectLayoutSetListener(this);
        f.setIMELanguage(imeLanguage);
        f.show(getSupportFragmentManager(), SelectLayoutSetDialogFragment.DIALOG_FRAGMENT);
    }

    private void showMultiLocaleRemoveDialogFragment(List<IMELanguage> IMELanguages) {
        MultiLocaleRemoveDialogFragment f = MultiLocaleRemoveDialogFragment.newInstance();
        f.setSubtypeIMES(IMELanguages);
        f.setMultiLocaleRemoveListener(this);
        f.show(getSupportFragmentManager(), MultiLocaleRemoveDialogFragment.DIALOG_FRAGMENT);
    }

    @Override
    public void onLayoutSetChanged(IMELanguage imeLanguage, String newLayout) {
        Agent.getInstance().onLayoutChanged(imeLanguage,newLayout);
        viewModel.obtainList();
    }

    @Override
    public void onLocaleRemoved(IMELanguage IMELanguage) {
        viewModel.removeSubtype(IMELanguage);
        DictionaryDownloadManager.getInstance().removeLocale(IMELanguage.getLocale());
        DictionaryDownloadManager.getInstance().requestDictionaryConfig(VertexInputMethodManager.getInstance().obtainLocaleStringForDownload());
    }

//    @Override
//    protected int getLayoutResource() {
//        return LAYOUT;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
        }
        return super.onOptionsItemSelected(item);
    }
}
