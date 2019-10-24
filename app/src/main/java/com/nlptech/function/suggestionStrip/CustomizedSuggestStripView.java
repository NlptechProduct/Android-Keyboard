package com.nlptech.function.suggestionStrip;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.inputmethod.latin.R;
import com.nlptech.inputmethod.latin.SuggestedWords;
import com.nlptech.keyboardview.suggestions.SuggestionStripView;

public class CustomizedSuggestStripView extends SuggestionStripView implements View.OnClickListener {

    private TextView mLeft;
    private TextView mRight;

    private ViewPager mViewPager;
    private CustomizedPagerAdapter mCustomizedPagerAdapter;

    private SuggestionStripViewListener mSuggestionStripViewListener;

    private int mPageCount;

    public CustomizedSuggestStripView(Context context) {
        super(context);
    }

    public CustomizedSuggestStripView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomizedSuggestStripView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mLeft = findViewById(R.id.left);
        mLeft.setOnClickListener(this::onClick);
        mRight = findViewById(R.id.right);
        mRight.setOnClickListener(this::onClick);

        mViewPager = findViewById(R.id.view_pager);
        mCustomizedPagerAdapter = new CustomizedPagerAdapter();
        mViewPager.setAdapter(mCustomizedPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateLeftAndRightBtn();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void setSuggestionStripViewListener(SuggestionStripViewListener suggestionStripViewListener, View view) {
        mSuggestionStripViewListener = suggestionStripViewListener;
    }

    @Override
    public void setSuggestions(SuggestedWords suggestedWords, boolean b) {
        int listSize = suggestedWords.size();
        int divide = listSize / 3;
        int mod = listSize % 3;
        mPageCount = (mod == 0) ? divide : divide + 1;
        mCustomizedPagerAdapter.setData(suggestedWords, mPageCount);
        mViewPager.setCurrentItem(0, false);
        updateLeftAndRightBtn();
    }

    @Override
    public void dismissMoreSuggestionsPanel() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left :
                if (mViewPager.getCurrentItem() > 0) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                    updateLeftAndRightBtn();
                }
                break;
            case R.id.right :
                if (mViewPager.getCurrentItem() < mPageCount -1) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    updateLeftAndRightBtn();
                }
                break;
        }
    }

    private void updateLeftAndRightBtn() {
        int currentPosition = mViewPager.getCurrentItem();
        mLeft.setTextColor((currentPosition == 0) ? Color.TRANSPARENT : Color.BLACK);
        mRight.setTextColor((currentPosition == mPageCount - 1 || mPageCount == 0) ? Color.TRANSPARENT : Color.BLACK);
    }

    private class CustomizedPagerAdapter extends PagerAdapter {

        private SuggestedWords suggestedWords;
        private int pageCount;

        public void setData(SuggestedWords suggestedWords, int pageCount) {
            this.suggestedWords = suggestedWords;
            this.pageCount = pageCount;
            notifyDataSetChanged();
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            LinearLayout viewItem = (LinearLayout) LayoutInflater.from(container.getContext()).inflate(
                    R.layout.customized_chinesed_suggest_view_item, container, false /* attachToRoot */);

            for (int i = 0 ; i < 3 ; i ++) {
                int index = position * 3 + i;
                String s = (index >= suggestedWords.size()) ? "" : suggestedWords.getWord(index);
                TextView item = (i == 0) ? viewItem.findViewById(R.id.item1) :
                        (i == 1) ? viewItem.findViewById(R.id.item2) :  viewItem.findViewById(R.id.item3);
                item.setText(s);
                item.setOnClickListener(v -> {
                    if (!s.equals("")) {
                        mSuggestionStripViewListener.pickSuggestionManually(suggestedWords.getInfo(index));
                    }
                });
            }

            container.addView(viewItem);
            return viewItem;
        }

        @Override
        public int getCount() {
            return pageCount;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
