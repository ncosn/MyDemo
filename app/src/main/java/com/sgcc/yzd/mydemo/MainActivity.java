package com.sgcc.yzd.mydemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tvLogin, tvTitile;
    Button btLeave, btTop;
    RecyclerView recyclerView;

    androidx.appcompat.widget.Toolbar toolbar;
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

    public void init() {
        //Assign varibale
        tvLogin = findViewById(R.id.tv_login);
        btLeave = findViewById(R.id.bt_leave);
        btTop = findViewById(R.id.bt_top);
        recyclerView = findViewById(R.id.recycle_view);

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
        //Set adapter
        recyclerView.setAdapter(mainAdapter);

        btTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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