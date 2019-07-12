package com.nlptech.function.languagesetting;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nlptech.Agent;
import com.nlptech.language.IMELanguage;
import com.nlptech.language.IMELanguageWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LanguageSettingViewModel extends ViewModel {

    private MutableLiveData<List<IMELanguageWrapper>> resultAddedLiveData;

    private List<IMELanguageWrapper> resultAddedList = new ArrayList<>();

    private List<IMELanguage> singleAddedList = new ArrayList<>();

    private List<IMELanguage> singleChooseList = new ArrayList<>();

    private MutableLiveData<List<IMELanguageWrapper>> resultChooseLiveData;

    private List<IMELanguageWrapper> resultChooseList = new ArrayList<>();

    private String resultChooseFilerTag;

    public LanguageSettingViewModel() {
        resultAddedLiveData = new MutableLiveData<>();
        resultChooseLiveData = new MutableLiveData<>();
    }

    public void obtainList() {
        setValue();
    }

    public void moveSubtype(int from, int to) {

    }

    public void addSubtype(IMELanguage subtypeIME) {
        Agent.getInstance().addIMELanguage(subtypeIME);
        setValue();
    }

    public void removeSubtype(IMELanguageWrapper item) {
        Agent.getInstance().removeIMELanguage(item.getIMELanguage());
        setValue();
    }

    public void removeSubtype(IMELanguage subtypeIME) {
        Agent.getInstance().removeIMELanguage(subtypeIME);
        setValue();
    }

    private void setValue() {
        //selected
        singleAddedList = Agent.getInstance().getAddedIMELanguageList();
        singleChooseList = Agent.getInstance().getAvailableIMELanguageList();

        //added
        resultAddedList.clear();
        for (IMELanguage item : singleAddedList) {
            IMELanguageWrapper sItem = new IMELanguageWrapper(IMELanguageWrapper.TYPE_SINGLE);
            sItem.setIMELanguage(item);
            resultAddedList.add(sItem);
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
        Collections.sort(resultChooseList, (subtypeIMEWrapper1, subtypeIMEWrapper2) ->
                subtypeIMEWrapper1.getIMELanguage().getDisplayName().compareToIgnoreCase(subtypeIMEWrapper2.getIMELanguage().getDisplayName()));
        resultChooseLiveData.setValue(resultChooseList);
    }

    public LiveData<List<IMELanguageWrapper>> getAddedResult() {
        return resultAddedLiveData;
    }

    public LiveData<List<IMELanguageWrapper>> getChooseResult() {
        return resultChooseLiveData;
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
