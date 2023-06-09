package com.sgcc.yzd.mydemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserCommentSuccess extends AppCompatActivity {

    Toolbar toolbar;
    Button btBack;
    TextView tvTitle;
    public static String TOOLBAR_TITLE = "客户意见留言成功";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_comment_succes);

        init();
    }

    public void init() {
        toolbar = findViewById(R.id.toolbar_main);
        btBack = findViewById(R.id.bt_back);
        tvTitle = findViewById(R.id.tv_title);

        tvTitle.setText(TOOLBAR_TITLE);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserCommentSuccess.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}