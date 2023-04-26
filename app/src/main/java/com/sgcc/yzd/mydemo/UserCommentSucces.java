package com.sgcc.yzd.mydemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserCommentSucces extends AppCompatActivity {

    Toolbar toolbar;
    Button btBack;
    TextView tvTitile;
    public static String TOOLBARTITLE = "客户意见留言成功";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_comment_succes);

        init();
    }

    public void init() {
        toolbar = findViewById(R.id.toolbar_main);
        btBack = findViewById(R.id.bt_back);
        tvTitile = findViewById(R.id.tv_title);

        tvTitile.setText(TOOLBARTITLE);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserCommentSucces.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}