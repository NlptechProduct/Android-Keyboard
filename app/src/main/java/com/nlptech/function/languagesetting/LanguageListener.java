package com.nlptech.function.languagesetting;

import com.nlptech.language.IMELanguageWrapper;

public interface LanguageListener {
    void onClickAdd(IMELanguageWrapper item);

    void onClickRemove(IMELanguageWrapper item);

    void onLanguagePositionChanged(int from, int to);

    void onClickChangeLayoutSet(String charset);
}
