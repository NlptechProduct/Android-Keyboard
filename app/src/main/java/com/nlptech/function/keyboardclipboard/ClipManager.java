package com.nlptech.function.keyboardclipboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.android.inputmethod.TestApplication;
import com.nlptech.common.utils.FileUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 保存20个剪贴板历史，只要纯文本
 */
public class ClipManager implements ClipboardManager.OnPrimaryClipChangedListener {
    private final static String TAG = ClipManager.class.getSimpleName();

    private final static int MAX_COUNT = 20;
    public final static String sDefData = "Use Vertex Clipboard to make paste easy";
    private final static String sClipDataFile = "sClipDataFile";

    private static ClipManager sInst;
    private ClipboardManager mCbMan;
    private List<String> mClipData;
    public long mLastChangedTime = 0;

    public boolean hasClipData() {
        return mCbMan != null && mCbMan.getPrimaryClip() != null && mCbMan.getPrimaryClip().getItemCount() > 0;
    }

    private ClipManager() {
        Context ctx = TestApplication.getInstance().getApplicationContext();
        try {
            mCbMan = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        } catch (Exception e) {
            Log.e(TAG,"Error : " + e);
            e.printStackTrace();
        }
        if (mCbMan == null) {
            return;
        }
        try {
            mCbMan.addPrimaryClipChangedListener(this);
        } catch (Exception e) {
            return;
        }
        List<String> list = null;
        try {
            list = (List<String>) FileUtils.getObject(ctx, sClipDataFile, List.class);
        } catch (Exception e) {
            Log.e(TAG,"Error : " + e);
            e.printStackTrace();
        }
        Set<String> added = new HashSet<>();
        if (list == null) {
            list = new LinkedList<>();
        }
        mClipData = new LinkedList<>();
        for (CharSequence cs : list) {
            if (!TextUtils.isEmpty(cs) && !added.contains(cs.toString()) && !sDefData.equals(cs)) {
                mClipData.add(cs.toString());
                added.add(cs.toString());
            } else {
                Log.d(TAG,"Remove dup clip: " + cs);
            }
        }
        if (mClipData.size() >= MAX_COUNT) {
            return;
        }
        boolean isAdded = false;
        try{
            ClipData cd = mCbMan.getPrimaryClip();
            if (cd != null) {
                for (int i = 0; i < cd.getItemCount(); i++) {
                    ClipData.Item item = cd.getItemAt(i);
                    if (item != null) {
                        CharSequence s = item.getText();
                        if (!TextUtils.isEmpty(s) && !added.contains(s.toString()) && !sDefData.equals(s)) {
                            isAdded = true;
                            mClipData.add(s.toString());
                            if (mClipData.size() >= MAX_COUNT) {
                                break;
                            }
                            added.add(s.toString());
                        } else {
                            Log.d("","Remove dup clip: " + s);
                        }
                    }
                }
            }
        }catch (Exception e){
            Log.e(TAG,"Error : " + e);
            e.printStackTrace();
        }
        if (mClipData.size() == 0) {
            mClipData.add(sDefData);
        } else if (isAdded) {
            save();
        }
    }

    public List<String> getClipData() {
        if (mClipData == null) {
            return new LinkedList<>();
        }
        return mClipData;
    }

    private void save() {
        try {
            FileUtils.saveObject(TestApplication.getInstance().getApplicationContext(), sClipDataFile, getClipData());
        } catch (Exception e) {
            Log.e(TAG,"Error : " + e);
            e.printStackTrace();
        }
    }

    private void addClipString(CharSequence s) {
        if (TextUtils.isEmpty(s)) {
            return;
        }
        if (mClipData == null) {
            mClipData = new LinkedList<>();
        }
        String data = s.toString();
        int idx = mClipData.indexOf(data);
        if (idx != 0) {
            if (idx > 0 && idx < mClipData.size()) {
                mClipData.remove(idx);
            }
            mClipData.add(0, data);
            while (mClipData.size() > MAX_COUNT) {
                mClipData.remove(mClipData.size() - 1);
            }
            save();
            //当剪切板第一条内容变化时，记录时间
            mLastChangedTime = SystemClock.elapsedRealtime();
            if (mOnClipTimeChangedListener != null) {
                mOnClipTimeChangedListener.onClipTimeChanged(s);
            }
        }
    }

    public void removeClipStringAt(int index) {

        if (mClipData == null) {
            mClipData = new LinkedList<>();
        }
        if (index >= mClipData.size())
            return;
        mClipData.remove(index);
        save();
    }


    public static ClipManager getInstance() {
        if (sInst == null) {
            sInst = new ClipManager();
        }
        return sInst;
    }

    @Override
    public void onPrimaryClipChanged() {
        if (mCbMan == null) {
            return;
        }
        ClipData cd = mCbMan.getPrimaryClip();
        if (cd != null && cd.getItemCount() > 0) {
            ClipData.Item item = cd.getItemAt(0);
            if (item != null) {
                CharSequence s = item.getText();
                addClipString(s);
            }
        }
    }

    public boolean canShowPasteTip() {
        return mLastChangedTime > 0 && !TextUtils.isEmpty(getPasteTipText()) && ifCopyPasteFeatureOpen();
    }

    public boolean ifCopyPasteFeatureOpen() {
        return true;
    }

    public void reset(boolean resetByInput) {
        mLastChangedTime = 0;
        if (mOnClipResetListener != null) {
            mOnClipResetListener.onClipReset(resetByInput);
        }
    }

    public String getPasteTipText() {
        return (mClipData != null && mClipData.size() > 0) ? mClipData.get(0) : "";
    }

    private OnClipTimeChangedListener mOnClipTimeChangedListener;
    private OnClipResetListener mOnClipResetListener;

    public void setOnClipTimeChangedListener(OnClipTimeChangedListener onClipTimeChangedListener) {
        mOnClipTimeChangedListener = onClipTimeChangedListener;
    }

    public void setOnClipResetListener(OnClipResetListener onClipResetListener) {
        mOnClipResetListener = onClipResetListener;
    }

    public void removeOnClipResetListener() {
        mOnClipResetListener = null;
    }

    public void removeOnClipTimeChangedListener() {
        mOnClipTimeChangedListener = null;
    }

    public boolean isPasteTipOutOfTime() {
        return (SystemClock.elapsedRealtime() - mLastChangedTime) / 1000 > 60 * 5;
    }

    public interface OnClipTimeChangedListener {
        void onClipTimeChanged(CharSequence s);
    }

    public interface OnClipResetListener {
        void onClipReset(boolean resetByInput);
    }
}
