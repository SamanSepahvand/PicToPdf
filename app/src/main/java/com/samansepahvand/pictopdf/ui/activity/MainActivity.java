package com.samansepahvand.pictopdf.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.samansepahvand.pictopdf.R;
import com.samansepahvand.pictopdf.ui.adapter.RecentlyAdapter;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recentlyRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    private void initView(){
        recentlyRecyclerView=findViewById(R.id.recently_recycler_view);
        initRecyclerView();
    }

    private void initRecyclerView(){
        recentlyRecyclerView.setHasFixedSize(true);
        recentlyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recentlyRecyclerView.setAdapter(new RecentlyAdapter(MainActivity.this));
    }
}