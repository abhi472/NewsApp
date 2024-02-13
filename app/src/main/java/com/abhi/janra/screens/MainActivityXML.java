package com.abhi.janra.screens;


import static androidx.lifecycle.RepeatOnLifecycleKt.repeatOnLifecycle;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.abhi.janra.common.MainViewModel;
import com.abhi.janra.util;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.abhi.janra.R;
import com.abhi.janra.databinding.ActivityMainBinding;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivityXML extends AppCompatActivity {

    MainViewModel viewModel ;

    NewsListAdapter adapter;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);;
        adapter = new NewsListAdapter(new ArrayList<>());
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setAdapter(adapter);
        String json = util.INSTANCE.getJsonFromDir(this, "news.json");
        viewModel.loadDataRx(json);

        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                viewModel.postEvent(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
       viewModel.getLiveEvent().observe(this, articles -> adapter.setNews(articles));
    }
}