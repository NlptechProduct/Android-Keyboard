package com.android.inputmethod.latin.settings;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.android.inputmethod.latin.R;
import com.nlptech.common.utils.SystemUtils;

public class ZengineIDPreference extends CustomPreference {

    public ZengineIDPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ZengineIDPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZengineIDPreference(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mTitle.setText("Zengine ID");
    }

    protected void onItemClick() {
        mSubTitle.setText("guid : " + SystemUtils.getGuid(mContext.getApplicationContext()));
    }

}
