package com.sgcc.yzd.mydemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tvLogin, tvTitile;
    Button btLeave, btTop;
    RecyclerView recyclerView;

    Toolbar toolbar;
    public static String TOOLBARTITLE = "电子意见簿";

    List<MainData> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RoomDB database;
    MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
//        dataList = database.mainDao().getAll();
//        mainAdapter = new MainAdapter(MainActivity.this,dataList);
//        //Set adapter
//        recyclerView.setAdapter(mainAdapter);
//        mainAdapter.notifyDataSetChanged();
//
//        String len = "length:"+dataList.size();
//
//        Log.i("length", len);
    }

    public void init() {
        //Assign varibale
        tvLogin = findViewById(R.id.tv_login);
        btLeave = findViewById(R.id.bt_leave);
        btTop = findViewById(R.id.bt_top);
        recyclerView = findViewById(R.id.recycler_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        tvTitile = findViewById(R.id.tv_title);
        tvTitile.setText(TOOLBARTITLE);

        //Initialize database
        database = RoomDB.getInstance(this);
        dataList = database.mainDao().getAll();

        //Initialize linear layout manager
        linearLayoutManager = new LinearLayoutManager(this);
        //Set layout manager
        recyclerView.setLayoutManager(linearLayoutManager);
        mainAdapter = new MainAdapter(MainActivity.this,dataList);
        //Set adapter
        recyclerView.setAdapter(mainAdapter);
//
//        String len = "length:"+dataList.size();
//
//        Log.i("length", len);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getLayoutManager().findViewByPosition(0)==null) {
                    btTop.setVisibility(View.VISIBLE);
                } else {
                    btTop.setVisibility(View.GONE);
                }
            }
        });

        btTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });


        btLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserCommentActivity.class);
                startActivity(intent);
            }
        });



    }

}