package com.nlptech.function.languagesetting.langadded;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.inputmethod.latin.R;
import com.nlptech.common.utils.DensityUtil;
import com.nlptech.language.IMELanguage;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MultiLocaleRemoveDialogFragment extends DialogFragment {
    public static final String DIALOG_FRAGMENT = "multi_locale_remove_dialog_fragment";

    protected Activity mActivity;
    protected AlertDialog mDialog;

    private List<IMELanguage> mIMELanguages;

    private Adapter mAdapter;

    public interface MultiLocaleRemoveListener {
        void onLocaleRemoved(IMELanguage IMELanguage);
    }
    private MultiLocaleRemoveListener mMultiLocaleRemoveListener;

    public static MultiLocaleRemoveDialogFragment newInstance() {
        return new MultiLocaleRemoveDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    public void setMultiLocaleRemoveListener(MultiLocaleRemoveListener localeRemoveListener) {
        mMultiLocaleRemoveListener = localeRemoveListener;
    }

    public void setSubtypeIMES(List<IMELanguage> IMELanguages) {
        mIMELanguages = IMELanguages;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialogfg_multi_locale_remove, null, false);
        TextView title = view.findViewById(R.id.title);
        if (mIMELanguages != null && mIMELanguages.get(0) != null) {
            title.setText(mIMELanguages.get(0).getLayoutName());
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        mAdapter = new Adapter(mIMELanguages, subtypeIME -> {
            mIMELanguages.remove(subtypeIME);
            mAdapter.setData(mIMELanguages);
            if (mMultiLocaleRemoveListener != null) mMultiLocaleRemoveListener.onLocaleRemoved(subtypeIME);
            if (mIMELanguages.size() == 0) {
                MultiLocaleRemoveDialogFragment.this.dismiss();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                .setView(view);


        mDialog = builder.create();
        return mDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);


        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，windo
        ViewGroup.LayoutParams params = win.getAttributes();
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = DensityUtil.dp2px(getActivity(),290);
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private class Adapter extends RecyclerView.Adapter<RemoveViewHolder> {

        RemoveViewHolderListener removeViewHolderListener;
        List<IMELanguage> IMELanguages;

        private Adapter(List<IMELanguage> IMELanguages, RemoveViewHolderListener listener) {
            this.IMELanguages = IMELanguages;
            this.removeViewHolderListener = listener;
        }

        public void setData(List<IMELanguage> IMELanguages) {
            this.IMELanguages = IMELanguages;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RemoveViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(mActivity).inflate(R.layout.viewholder_multi_locale_remove_dialogfg_item, viewGroup, false);
            return new RemoveViewHolder(itemView, removeViewHolderListener);
        }

        @Override
        public void onBindViewHolder(@NonNull RemoveViewHolder viewHolder, int i) {
            viewHolder.bind(IMELanguages.get(i));
        }

        @Override
        public int getItemCount() {
            return IMELanguages == null ? 0 : IMELanguages.size();
        }
    }

    private interface RemoveViewHolderListener {
        void onClick(IMELanguage IMELanguage);
    }

    private class RemoveViewHolder extends RecyclerView.ViewHolder {

        RemoveViewHolderListener removeViewHolderListener;
        TextView displayName;
        ImageView removeIcon;

        RemoveViewHolder(@NonNull View itemView, RemoveViewHolderListener listener) {
            super(itemView);
            displayName = itemView.findViewById(R.id.displayName);
            removeIcon = itemView.findViewById(R.id.remove_icon);
            removeViewHolderListener = listener;
        }

        void bind(final IMELanguage IMELanguage) {
            displayName.setText(IMELanguage.getDisplayName());
            removeIcon.setOnClickListener(v -> removeViewHolderListener.onClick(IMELanguage));
        }
    }
}
