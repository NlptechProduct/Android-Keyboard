package com.android.inputmethod.latin.settings;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.android.inputmethod.latin.R;
import com.nlptech.common.utils.SystemUtils;

public class CustomPreference extends Preference {

    private Context mContext;

    public CustomPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public CustomPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomPreference(Context context) {
        this(context, null);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        TextView textView = view.findViewById(R.id.custom_item_name);
        textView.setText("guid : " + SystemUtils.getGuid(mContext.getApplicationContext()));
    }

}
