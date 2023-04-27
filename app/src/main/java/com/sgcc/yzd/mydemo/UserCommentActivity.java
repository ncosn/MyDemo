package com.sgcc.yzd.mydemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    TextView tvTitle, tvName, tvSex, tvTel, tvWordsNum;
    Toolbar toolbar;

    public static String TOOLBAR_TITLE = "客户意见留言";
    public static String WORDS_NUM = "/500";
    public static int TYPE_PRAISE = 0;
    public static int TYPE_ADVICE = 1;
    public static int TYPE_COMPLAIN = 2;
    public static int SEX_MALE = 0;
    public static int SEX_FEMALE = 1;
    public static int STATUS_REAL = 0;
    public static int STATUS_ANONYMOUS = 1;

    public static int WORD_LIMIT_NUM= 500;

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
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(TOOLBAR_TITLE);

        etName = (EditText) findViewById(R.id.et_name);
        etTel = (EditText) findViewById(R.id.et_tel);
        etComment = (EditText) findViewById(R.id.et_comment);

        tvWordsNum = findViewById(R.id.tv_words_num);
        tvName = findViewById(R.id.tv_name);
        tvSex = findViewById(R.id.tv_sex);
        tvTel = findViewById(R.id.tv_tel);

        status = STATUS_REAL;
        sex = SEX_MALE;
        type = TYPE_PRAISE;

        rgStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_real:
                        status = STATUS_REAL;
                        linearLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_anonymous:
                        status = STATUS_ANONYMOUS;
                        linearLayout.setVisibility(View.GONE);
                        break;
                }
            }
        });

        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_male:
                        sex = SEX_MALE;
                        break;
                    case R.id.rb_female:
                        sex = SEX_FEMALE;
                        break;
                }
            }
        });

        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_praise:
                        type = TYPE_PRAISE;
                        break;
                    case R.id.rb_advice:
                        type = TYPE_ADVICE;
                        break;
                    case R.id.rb_complain:
                        type = TYPE_COMPLAIN;
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

        etComment.addTextChangedListener(new TextWatcher() {
            //记录输入的字数
            private CharSequence enterWords;
            private int selectionStart;
            private int selectionEnd;
            private int enteredWords;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //实时记录输入的字数
                enterWords= charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //TextView显示字数
                tvWordsNum.setText(editable.length() + WORDS_NUM);
                selectionStart = etComment.getSelectionStart();
                selectionEnd = etComment.getSelectionEnd();
                if (enterWords.length() > WORD_LIMIT_NUM) {
                    //删除多余输入的字（不会显示出来）
                    editable.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    etComment.setText(editable);
                    //设置光标在最后
                    etComment.setSelection(tempSelection);
                }
            }
        });
    }

}