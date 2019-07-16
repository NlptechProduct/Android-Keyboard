package com.android.inputmethod.latin.settings;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.nlptech.common.utils.SystemUtils;

public class PrivacyPolicyPreference extends CustomPreference {

    public PrivacyPolicyPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PrivacyPolicyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PrivacyPolicyPreference(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mTitle.setText("Privacy Policy");
    }

    protected void onItemClick() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://zengine.nlptech.com/privacy.txt"));
        mContext.startActivity(browserIntent);
    }

}
