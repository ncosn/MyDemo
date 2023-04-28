package com.sgcc.yzd.mydemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    TextView tvExit,tvTitle;
    Button btClear, btFilter, btTop;
    RadioGroup rgIfReply, rgType;
    RadioButton rbReply, rbNoReply, rbPraise, rbAdvice, rbComplain;
    Toolbar toolbar;

    RoomDB database;
    RecyclerView recyclerView;
    List<MainData> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    AdminAdapter adminAdapter;

    public static String TOOLBAR_TITLE = "电子意见簿 | 管理员";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        init();
    }

    public void init() {
        toolbar = findViewById(R.id.toolbar_main);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(TOOLBAR_TITLE);
        tvExit = findViewById(R.id.tv_exit);

        btClear = findViewById(R.id.bt_clear);
        btFilter = findViewById(R.id.bt_filter);
        btTop = findViewById(R.id.bt_top);

        rgIfReply = findViewById(R.id.rg_reply);
        rbReply = findViewById(R.id.rb_reply);
        rbNoReply = findViewById(R.id.rb_no_reply);

        rgType = findViewById(R.id.rg_type);
        rbPraise = findViewById(R.id.rb_praise);
        rbAdvice = findViewById(R.id.rb_advice);
        rbComplain = findViewById(R.id.rb_complain);

        recyclerView = findViewById(R.id.recycler_view);

        database = RoomDB.getInstance(this);
        dataList = database.mainDao().getAll();
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adminAdapter = new AdminAdapter(AdminActivity.this,dataList);
        recyclerView.setAdapter(adminAdapter);

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


        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        btTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

    }
}