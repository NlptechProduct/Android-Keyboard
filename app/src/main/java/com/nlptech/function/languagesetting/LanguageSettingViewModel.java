package com.nlptech.function.languagesetting;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nlptech.language.IMELanguage;
import com.nlptech.language.IMELanguageWrapper;
import com.nlptech.language.MultiIMELanguage;
import com.nlptech.language.VertexInputMethodManager;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LanguageSettingViewModel extends ViewModel {

    private boolean isMultiMode;

    private MutableLiveData<Boolean> multiModeLiveData;

    private MutableLiveData<List<IMELanguageWrapper>> resultAddedLiveData;

    private List<IMELanguageWrapper> resultAddedList = new ArrayList<>();

    private List<IMELanguage> singleAddedList = new ArrayList<>();

    private Map<String, MultiIMELanguage> multiAddedMap = new HashMap<>();

    private List<IMELanguage> singleChooseList = new ArrayList<>();

    private MutableLiveData<List<IMELanguageWrapper>> resultChooseLiveData;

    private List<IMELanguageWrapper> resultChooseList = new ArrayList<>();

    private String resultChooseFilerTag;

    public LanguageSettingViewModel() {
        resultAddedLiveData = new MutableLiveData<>();
        resultChooseLiveData = new MutableLiveData<>();
        multiModeLiveData = new MutableLiveData<>();
    }

    public void obtainList() {
        setValue();
    }

    public void moveSubtype(int from, int to) {
        if (isMultiMode) {
            VertexInputMethodManager.getInstance().moveSubtype(resultAddedList);
        } else {
            VertexInputMethodManager.getInstance().moveSubtype(from, to);
        }
    }

    public void addSubtype(IMELanguage IMELanguage) {
        VertexInputMethodManager.getInstance().addSubtype(IMELanguage);
        setValue();
    }

    public void removeSubtype(IMELanguageWrapper item) {
        //TYPE_MULTI
        if (item.getType() == IMELanguageWrapper.TYPE_MULTI) {
            VertexInputMethodManager.getInstance().removeSubtype(item.getMultiIMELanguage().getSubtypeIMEList());
        } else {
            //TYPE_SINGLE 和 TYPE_CHOOSE_SINGLE
            VertexInputMethodManager.getInstance().removeSubtype(item.getIMELanguage());
        }
        setValue();
    }

    public void removeSubtype(IMELanguage IMELanguage) {
        VertexInputMethodManager.getInstance().removeSubtype(IMELanguage);
        setValue();
    }

    private void setValue() {
        //selected
        singleAddedList = VertexInputMethodManager.getInstance().getSingleAddedList();
        singleChooseList = VertexInputMethodManager.getInstance().getSingleChooseList();
        multiAddedMap = VertexInputMethodManager.getInstance().getMutilAddedMap();
        isMultiMode = VertexInputMethodManager.getInstance().isMultiTypeMode();

        //added
        resultAddedList.clear();
        if (isMultiMode) {
            ArrayList<String> layoutSets = new ArrayList<>();
            for (IMELanguage singleItem : singleAddedList) {
                if (layoutSets.contains(singleItem.getLayout())) {
                    continue;
                }
                for (MultiIMELanguage multiItem : multiAddedMap.values()) {
                    if (singleItem.getLayout().equals(multiItem.getLayoutSet())) {
                        layoutSets.add(singleItem.getLayout());
                        IMELanguageWrapper sItem = new IMELanguageWrapper(IMELanguageWrapper.TYPE_MULTI);
                        sItem.setMultiIMELanguage(multiItem);
                        resultAddedList.add(sItem);
                    }
                }
            }
        } else {
            for (IMELanguage item : singleAddedList) {
                IMELanguageWrapper sItem = new IMELanguageWrapper(IMELanguageWrapper.TYPE_SINGLE);
                sItem.setIMELanguage(item);
                resultAddedList.add(sItem);
            }
        }
        resultAddedLiveData.setValue(resultAddedList);

        //chooser
        resultChooseList.clear();
        for (IMELanguage item : singleChooseList) {
            if (TextUtils.isEmpty(resultChooseFilerTag) || item.getDisplayName().toLowerCase().contains(resultChooseFilerTag.toLowerCase())) {
                IMELanguageWrapper sItem = new IMELanguageWrapper(IMELanguageWrapper.TYPE_CHOOSE_SINGLE);
                sItem.setIMELanguage(item);
                resultChooseList.add(sItem);
            }
        }
        if (Locale.getDefault().getLanguage().equals(Locale.CHINESE.getLanguage())) {
            Collections.sort(resultChooseList, (subtypeIMEWrapper1, subtypeIMEWrapper2) ->
                            Collator.getInstance(Locale.CHINESE).compare(subtypeIMEWrapper1.getIMELanguage().getDisplayName(),
                                    subtypeIMEWrapper2.getIMELanguage().getDisplayName()));
        } else {
            Collections.sort(resultChooseList, (subtypeIMEWrapper1, subtypeIMEWrapper2) ->
                    subtypeIMEWrapper1.getIMELanguage().getDisplayName().compareToIgnoreCase(subtypeIMEWrapper2.getIMELanguage().getDisplayName()));
        }
        resultChooseLiveData.setValue(resultChooseList);
        //multiMode
        multiModeLiveData.setValue(isMultiMode);
    }

    public LiveData<List<IMELanguageWrapper>> getAddedResult() {
        return resultAddedLiveData;
    }

    public LiveData<List<IMELanguageWrapper>> getChooseResult() {
        return resultChooseLiveData;
    }

    public MutableLiveData<Boolean> getMultiModeLiveData() {
        return multiModeLiveData;
    }

    /**
     * 点击混输/单语言切换
     */
    public void updateMultiMode() {
        VertexInputMethodManager.updateMultiTypeMode();
        setValue();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public void filterChooseResult(String filterTag) {
        resultChooseFilerTag = filterTag;
        setValue();
    }
}
