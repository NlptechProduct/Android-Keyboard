package com.nlptech.function.languagesetting.langswitch;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.inputmethod.latin.R;
import com.nlptech.common.utils.DisplayUtil;
import com.nlptech.function.languagesetting.langadded.LanguageAddedActivity;
import com.nlptech.language.CharsetTable;
import com.nlptech.language.IMELanguage;
import com.nlptech.language.IMELanguageWrapper;
import com.nlptech.language.LayoutDisplayTable;
import com.nlptech.language.MultiIMELanguage;
import com.nlptech.language.VertexInputMethodManager;

import java.util.ArrayList;
import java.util.List;

import static com.nlptech.common.utils.DensityUtil.dp2px;

public class SubtypeSwitchDialogView extends RelativeLayout implements SubtypeSwitchListener {

    public interface SubtypeSwitchDialogViewListener {
        void dismissSubtypeSwitchDialog();
    }

    private RelativeLayout subtypeLayout;
    private RelativeLayout charsetLayout;
    private TextView displayName, localeOrLayoutName;
    private ImageView enterCharsetBtn;
    private View divider;

    private SubtypeSwitchAdapter subtypeAdapter;
    private CharsetAdapter charsetAdapter;
    private List<IMELanguageWrapper> dataSet = new ArrayList<>();
    private IMELanguageWrapper currentSubtype;
    //标识当前是subtype还是charset界面
    private boolean isCharsetShow;

    private SubtypeSwitchDialogViewListener mListener;

    public SubtypeSwitchDialogView(Context context) {
        this(context, null);
    }

    public SubtypeSwitchDialogView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubtypeSwitchDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListener(SubtypeSwitchDialogViewListener listener) {
        mListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        RelativeLayout layout = findViewById(R.id.subtype_switch_layout);
        subtypeLayout = findViewById(R.id.subtype_layout);
        charsetLayout = findViewById(R.id.layoutset_layout);
        MaxHeightRecyclerView charsetRecycler = findViewById(R.id.charset_recycler);
        MaxHeightRecyclerView subtypeRecycler = findViewById(R.id.recyclerView);
        displayName = findViewById(R.id.displayName);
        TextView openLanguageSettings = findViewById(R.id.open_language_settings);
        openLanguageSettings.setOnClickListener(v -> {
            Intent intentLanguageAddedActivity = new Intent(getContext(), LanguageAddedActivity.class);
            intentLanguageAddedActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intentLanguageAddedActivity);
            if (mListener != null) {
                mListener.dismissSubtypeSwitchDialog();
            }
        });
        localeOrLayoutName = findViewById(R.id.localeOrLayoutName);
        enterCharsetBtn = findViewById(R.id.change_layout);
        View topLayout = findViewById(R.id.top_layout);
        topLayout.setOnClickListener(v -> enterCharset(currentSubtype));
        layout.setOnClickListener(v -> {
            if (isCharsetShow) {
                showSubtype();
            } else {
                if (mListener != null) {
                    mListener.dismissSubtypeSwitchDialog();
                }
            }
        });
        divider = findViewById(R.id.divider);

        boolean isOrientationPortrait = DisplayUtil.isOrientationPortrait(getContext());
        if (!isOrientationPortrait) {
            ((RelativeLayout.LayoutParams) localeOrLayoutName.getLayoutParams()).topMargin = 0;
            ((RelativeLayout.LayoutParams) topLayout.getLayoutParams()).topMargin = 0;
            ((RelativeLayout.LayoutParams) subtypeRecycler.getLayoutParams()).topMargin = 0;
            ((RelativeLayout.LayoutParams) openLanguageSettings.getLayoutParams()).bottomMargin = dp2px(getContext(), 5);
            float subtypeRecyclerM = isOrientationPortrait ? 0.2f : 0.15f;
            subtypeRecycler.setMaxHeight((int) (DisplayUtil.getScreenHeight(getContext()) * subtypeRecyclerM));
        }

        subtypeAdapter = new SubtypeSwitchAdapter(this);
        subtypeRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        subtypeRecycler.setAdapter(subtypeAdapter);

        charsetAdapter = new CharsetAdapter(this);
        charsetRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        charsetRecycler.setAdapter(charsetAdapter);

        showSubtype();
        initThemeItems();

        update();
    }

    private void initThemeItems() {
//        KeyboardThemeManager.getInstance().setUiModuleBackground(subtypeLayout);
//        KeyboardThemeManager.getInstance().setUiModuleBackground(charsetLayout);
//        KeyboardThemeManager.getInstance().colorUiModuleIcon(enterCharsetBtn);
        // TODO use other theme's settings : waiting for themeManager definition
    }

    private void showSubtype(){
        isCharsetShow = false;
        subtypeLayout.setVisibility(View.VISIBLE);
        charsetLayout.setVisibility(View.INVISIBLE);
    }

    private void showCharset(IMELanguageWrapper wrapper){
        isCharsetShow = true;
        subtypeLayout.setVisibility(View.INVISIBLE);
        charsetLayout.setVisibility(View.VISIBLE);

        IMELanguage item;
        if(wrapper.getType() == IMELanguageWrapper.TYPE_MULTI){
            List<IMELanguage>  list = wrapper.getMultiIMELanguage().getSubtypeIMEList();
            item = list.get(0);
        }else{
            item = wrapper.getIMELanguage();
        }
        List<String> layouts = obtainLayoutList(item);
        charsetAdapter.setDataSet(wrapper, layouts, item.getLayout());
    }

    private void update(){
        dataSet.clear();
        //创建subtype列表的显示list
        if (VertexInputMethodManager.getInstance().isMultiTypeMode()) {
            updateMultiMode();
        } else {
            updateSingleMode();
        }
        subtypeAdapter.setDataSet(dataSet);
        if(dataSet.size() == 0){
            divider.setVisibility(View.INVISIBLE);
        }else{
            divider.setVisibility(View.VISIBLE);
        }

        if (VertexInputMethodManager.getInstance().isMultiTypeMode()) {
            List<IMELanguage> list = currentSubtype.getMultiIMELanguage().getSubtypeIMEList();
            if (list.size() <= 1) {
                displayName.setText(list.get(0).getDisplayName());
                localeOrLayoutName.setText(LayoutDisplayTable.getInstance().obtainDisplayLayout(list.get(0).getLayout()));
            } else {
                displayName.setText(getContext().getString(R.string.subtype_switch_multi_language));
                localeOrLayoutName.setText(LayoutDisplayTable.getInstance().obtainDisplayLayout(currentSubtype.getMultiIMELanguage().getLayoutSet()));
            }
            showCharsetEnterBtnIfNeeded(list.get(0));
        } else {
            IMELanguage IMELanguage = currentSubtype.getIMELanguage();
            displayName.setText(IMELanguage.getDisplayName());
            localeOrLayoutName.setText(LayoutDisplayTable.getInstance().obtainDisplayLayout(IMELanguage.getLayout()));
            showCharsetEnterBtnIfNeeded(IMELanguage);
        }
        showSubtype();
    }

    /**
     * 进入charset界面的箭头是否显示
     * @param item
     */
    private void showCharsetEnterBtnIfNeeded(IMELanguage item){
        List<String> layouts = obtainLayoutList(item);
        if (layouts.size() > 1) {
            enterCharsetBtn.setVisibility(View.VISIBLE);
        } else {
            enterCharsetBtn.setVisibility(View.GONE);
        }
    }

    private List<String> obtainLayoutList(IMELanguage item){
        List<String> layouts;
        if(VertexInputMethodManager.getInstance().isMultiTypeMode()){
            //混输模式下 只获取当前charset对应的layout
            layouts = CharsetTable.getInstance().obtainLayoutListFromCharset(item.getCharset());
        }else{
            //单语言模式下，获取改语言所有charsets下的layout
            layouts = CharsetTable.getInstance().obtainLayoutListFromCharset(item.getCharsets());
        }
        return layouts;
    }

    /**
     * 创建混输的数据结构
     */
    private void updateMultiMode() {
        for (MultiIMELanguage item : VertexInputMethodManager.getInstance().getMutilAddedMap().values()) {
            IMELanguageWrapper sItem = new IMELanguageWrapper(IMELanguageWrapper.TYPE_MULTI);
            sItem.setMultiIMELanguage(item);
            if (item.getSubtypeIMEList().get(0).equals(VertexInputMethodManager.getInstance().getCurrentSubtype())) {
                currentSubtype = sItem;
            } else {
                dataSet.add(sItem);
            }
        }
    }

    /**
     * 创建单语言输入的数据结构
     */
    private void updateSingleMode() {
        for (IMELanguage item : VertexInputMethodManager.getInstance().getSingleAddedList()) {
            IMELanguageWrapper sItem = new IMELanguageWrapper(IMELanguageWrapper.TYPE_SINGLE);
            sItem.setIMELanguage(item);
            if (item.equals(VertexInputMethodManager.getInstance().getCurrentSubtype())) {
                currentSubtype = sItem;
            } else {
                dataSet.add(sItem);
            }
        }
    }

    /**
     * 进入切换layout界面
     *
     * @param item
     */
    private void enterCharset(IMELanguageWrapper item) {
        if (item == null) {
            return;
        }
        showCharset(item);
    }

    /**
     * 切换语言
     * @param item
     */
    @Override
    public void onChangeSubtype(IMELanguageWrapper item) {
        VertexInputMethodManager.getInstance().onSubtypeChanged(item);
        if (mListener != null) {
            mListener.dismissSubtypeSwitchDialog();
        }
    }

    /**
     * 点击更换layoutset
     */
    @Override
    public void onChangeLayout(IMELanguageWrapper item, String newLayout) {
        VertexInputMethodManager.getInstance().onLayoutChanged(item,newLayout);
        update();
    }
}
