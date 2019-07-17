package com.nlptech.function.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.inputmethod.latin.R;
import com.android.inputmethod.latin.settings.SettingsActivity;
import com.nlptech.Agent;
import com.nlptech.function.languagesetting.langadded.LanguageAddedActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int LAYOUT = R.layout.activity_main;

    private RecyclerView mRecyclerView;

    private ArrayList<String> ITEM_LIST = new ArrayList<>(
            Arrays.asList("Language", "Settings")
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        Adapter adapter = new Adapter(index -> {
            switch (index) {
                case 0:
                    invokeLanguageActivity();
                    break;
                case 1:
                    invokeSettingsOfThisIme();
                    break;
                    default:
                        break;
            }
        });
        mRecyclerView.setAdapter(adapter);
        adapter.setDataSet(ITEM_LIST);

        Agent.getInstance().downloadDictionary();
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private ViewHolderClickListener listener;
        private List<String> dataSet = new ArrayList<>();

        public Adapter(ViewHolderClickListener listener) {
            this.listener = listener;
        }

        public void setDataSet(List<String> dataSet) {
            this.dataSet = dataSet;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View itemView = LayoutInflater.from(context).inflate(R.layout.viewholder_main_activity, parent, false);
            ViewHolder viewHolder = new ViewHolder(itemView, listener);
            itemView.setTag(viewHolder);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(position, dataSet.get(position));
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }

    private interface ViewHolderClickListener {
        void onClick(int index);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ViewHolderClickListener listener;

        private View itemView;
        private TextView textview;

        ViewHolder(@NonNull View itemView, ViewHolderClickListener listener) {
            super(itemView);
            this.listener = listener;

            this.itemView = itemView;
            this.textview = itemView.findViewById(R.id.textview);
        }

        public void bind(int index, String s) {
            textview.setText(s);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClick(index);
                }
            });
        }
    }

    private void invokeSettingsOfThisIme() {
        final Intent intent = new Intent();
        intent.setClass(this, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(SettingsActivity.EXTRA_ENTRY_KEY,
                SettingsActivity.EXTRA_ENTRY_VALUE_APP_ICON);
        startActivity(intent);
    }

    private  void invokeLanguageActivity(){
        Intent intent = new Intent(this, LanguageAddedActivity.class);
        startActivity(intent);
    }
}
