package com.android.inputmethod.latin.settings;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.android.inputmethod.latin.R;

public class CustomPreference extends Preference {

    protected Context mContext;
    protected TextView mTitle;
    protected TextView mSubTitle;

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
        mTitle = view.findViewById(R.id.custom_title);
        mSubTitle = view.findViewById(R.id.custom_subtitle);
        view.setOnClickListener(v -> onItemClick());
    }

    protected void onItemClick() {

    }

}
