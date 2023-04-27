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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class MainActivity extends AppCompatActivity {

    TextView tvLogin, tvTitle;
    Button btLeave, btTop;
    RecyclerView recyclerView;

    EditText etPassword;

    Toolbar toolbar;
    public static String TOOLBAR_TITLE = "电子意见簿";
    public static String PASSWORD = "123456";

    List<MainData> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RoomDB database;
    MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteStudioService.instance().start(this);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    public void init() {

        //Assign varibale
        tvLogin = findViewById(R.id.tv_login);
        btLeave = findViewById(R.id.bt_leave);
        btTop = findViewById(R.id.bt_top);
        etPassword = findViewById(R.id.et_password);
        recyclerView = findViewById(R.id.recycler_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(TOOLBAR_TITLE);

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

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create dialog
                Dialog dialog = new Dialog(MainActivity.this);
                //Set content view
                dialog.setContentView(R.layout.admin_login);
                //Show dialog
                dialog.show();

                EditText etPassword = dialog.findViewById(R.id.et_password);
                Button btLogin = dialog.findViewById(R.id.bt_login);
                btLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String password = etPassword.getText().toString().trim();
                        if (password.equals(PASSWORD)) {
                            Intent intent = new Intent(MainActivity.this,AdminActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                        }
//                        dialog.dismiss();
                    }
                });

                ImageView imCancel = dialog.findViewById(R.id.im_cancel);
                imCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
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

    @Override
    protected void onDestroy() {
        SQLiteStudioService.instance().stop();
        super.onDestroy();
    }
}