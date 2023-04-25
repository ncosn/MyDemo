package com.sgcc.yzd.mydemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

public class UserCommentActivity extends AppCompatActivity {

    TextView tvTitile;
    Toolbar toolbar;
    public static String TOOLBARTITLE = "客户意见留言";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_comment);

        init();
    }

    public void init() {
        toolbar = findViewById(R.id.toolbar_main);
        tvTitile = findViewById(R.id.tv_title);
        tvTitile.setText(TOOLBARTITLE);
    }
}