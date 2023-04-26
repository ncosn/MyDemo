package com.sgcc.yzd.mydemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserCommentActivity extends AppCompatActivity {
    RoomDB database;
    Integer status, sex, type;

    RadioGroup rgStatus,rgSex,rgType;
    RadioButton rbReal;
    RadioButton rbAnonymous;

    RadioButton rbMale;
    RadioButton rbFemale;

    RadioButton rbPraise;
    RadioButton rbAdvice;
    RadioButton rbComplain;

    LinearLayout linearLayout;
    Button btBack, btCommit;

    EditText etName, etTel, etComment;
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
        //Initialize database
        database = RoomDB.getInstance(this);

        rgStatus = findViewById(R.id.rg_status);
        rbReal = findViewById(R.id.rb_real);
        rbAnonymous = findViewById(R.id.rb_anonymous);

        rgSex = findViewById(R.id.rg_sex);
        rbMale = findViewById(R.id.rb_male);
        rbFemale = findViewById(R.id.rb_female);

        rgType = findViewById(R.id.rg_type);
        rbPraise = findViewById(R.id.rb_praise);
        rbAdvice = findViewById(R.id.rb_advice);
        rbComplain = findViewById(R.id.rb_complain);

        linearLayout = findViewById(R.id.ll_real);
        btBack = findViewById(R.id.bt_back);
        btCommit = findViewById(R.id.bt_commit);

        toolbar = findViewById(R.id.toolbar_main);
        tvTitile = findViewById(R.id.tv_title);
        tvTitile.setText(TOOLBARTITLE);

        etName = (EditText) findViewById(R.id.et_name);
        etTel = (EditText) findViewById(R.id.et_tel);
        etComment = (EditText) findViewById(R.id.et_comment);

        status = 0;
        sex = 0;
        type = 0;

        rgStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_real:
                        status = 0;
                        linearLayout.setVisibility(View.VISIBLE);
                    case R.id.rb_anonymous:
                        status = 1;
                        linearLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_male:
                        sex = 0;
                        break;
                    case R.id.rb_female:
                        sex = 1;
                        break;
                }
            }
        });

        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_praise:
                        type = 0;
                        break;
                    case R.id.rb_advice:
                        type = 1;
                        break;
                    case R.id.rb_complain:
                        type = 2;
                        break;
                }
            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserCommentActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String time = dateFormat.format(date);

                MainData d = new MainData();
                d.setTime(time);
                if (status == 0) {
                    d.setStatus(0);
                    d.setName(etName.getText().toString().trim());
                    d.setSex(sex);
                    d.setTel(etTel.getText().toString().trim());
                } else {
                    d.setStatus(1);
                }
                d.setType(type);
                d.setMsg(etComment.getText().toString());
                database.mainDao().insert(d);
                Intent intent = new Intent(UserCommentActivity.this, UserCommentSucces.class);
                startActivity(intent);
            }
        });
    }


}