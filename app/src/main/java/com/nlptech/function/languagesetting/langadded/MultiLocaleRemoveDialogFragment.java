package com.nlptech.function.languagesetting.langadded;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.inputmethod.latin.R;
import com.nlptech.language.IMELanguage;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MultiLocaleRemoveDialogFragment extends DialogFragment {
    public static final String DIALOG_FRAGMENT = "multi_locale_remove_dialog_fragment";

    protected Activity mActivity;
    protected AlertDialog mDialog;

    private List<IMELanguage> mSubtypeIMES;

    private Adapter mAdapter;

    public interface MultiLocaleRemoveListener {
        void onLocaleRemoved(IMELanguage subtypeIME);
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

    public void setSubtypeIMES(List<IMELanguage> subtypeIMES) {
        mSubtypeIMES = subtypeIMES;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialogfg_multi_locale_remove, null, false);
        TextView title = view.findViewById(R.id.title);
        if (mSubtypeIMES != null && mSubtypeIMES.get(0) != null) {
            title.setText(mSubtypeIMES.get(0).getLayout());
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        mAdapter = new Adapter(mSubtypeIMES, subtypeIME -> {
            mSubtypeIMES.remove(subtypeIME);
            mAdapter.setData(mSubtypeIMES);
            if (mMultiLocaleRemoveListener != null) mMultiLocaleRemoveListener.onLocaleRemoved(subtypeIME);
            if (mSubtypeIMES.size() == 0) {
                MultiLocaleRemoveDialogFragment.this.dismiss();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                .setView(view)
                .setPositiveButton("OK", (dialog, which) -> {
                    MultiLocaleRemoveDialogFragment.this.dismiss();
                });
        mDialog = builder.create();
        return mDialog;
    }

    private class Adapter extends RecyclerView.Adapter<RemoveViewHolder> {

        RemoveViewHolderListener removeViewHolderListener;
        List<IMELanguage> subtypeIMES;

        private Adapter(List<IMELanguage> subtypeIMES, RemoveViewHolderListener listener) {
            this.subtypeIMES = subtypeIMES;
            this.removeViewHolderListener = listener;
        }

        public void setData(List<IMELanguage> subtypeIMES) {
            this.subtypeIMES = subtypeIMES;
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
            viewHolder.bind(subtypeIMES.get(i));
        }

        @Override
        public int getItemCount() {
            return subtypeIMES == null ? 0 : subtypeIMES.size();
        }
    }

    private interface RemoveViewHolderListener {
        void onClick(IMELanguage subtypeIME);
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

        void bind(final IMELanguage subtypeIME) {
            displayName.setText(subtypeIME.getDisplayName());
            removeIcon.setOnClickListener(v -> removeViewHolderListener.onClick(subtypeIME));
        }
    }
}
