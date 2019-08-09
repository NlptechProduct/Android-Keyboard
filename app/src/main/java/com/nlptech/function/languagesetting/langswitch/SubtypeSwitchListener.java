package com.nlptech.function.languagesetting.langswitch;

import com.nlptech.language.IMELanguageWrapper;

public interface SubtypeSwitchListener {
    /**
     * 点击切换语言
     * @param item
     */
    void onChangeSubtype(IMELanguageWrapper item);

    /**
     * 点击切换layout
     * @param item
     * @param newLayout
     */
    void onChangeLayout(IMELanguageWrapper item, String newLayout);

}
