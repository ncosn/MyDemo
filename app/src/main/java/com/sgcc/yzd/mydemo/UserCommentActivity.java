package com.sgcc.yzd.mydemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Group;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 86181
 */
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

    /**
     * 用户选择实名时的控件组
     */
    Group group;
    EditText etName, etTel, etComment;
    TextView tvTitle, tvName, tvSex, tvTel, tvWordsNum;
    Toolbar toolbar;

    public static String TOOLBAR_TITLE = "客户意见留言";
    public static String WORDS_NUM = "/500";
    public static String TOAST1 = "请输入客户姓名";
    public static String TOAST2 = "请输入联系方式";
    public static String TOAST3 = "请输入留言";
    public static String FORMAT = "yyyy-MM-dd HH:mm";
    public static String ANONYMOUS_NAME = "匿名用户";
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

        //防止自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //绑定控件
        init();
        //绑定事件
        initEvent();
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
        group = findViewById(R.id.group);
        //初始化身份为实名
        status = STATUS_REAL;
        //初始化性别为男
        sex = SEX_MALE;
        //初始化类型为表扬
        type = TYPE_PRAISE;
    }

    public void initEvent() {
        //身份单选框改变事件
        rgStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_real:
                        status = STATUS_REAL;
                        //实名时显示控件组
                        group.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_anonymous:
                        status = STATUS_ANONYMOUS;
                        //匿名实隐藏控件组
                        group.setVisibility(View.GONE);
                        break;
                    default:
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
                    default:
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
                    default:
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
                if (status == 0 && "".equals(etName.getText().toString())) {
                    Toast.makeText(UserCommentActivity.this,TOAST1,Toast.LENGTH_SHORT).show();
                    return;
                } else if (status == 0 && "".equals(etTel.getText().toString())) {
                    Toast.makeText(UserCommentActivity.this,TOAST2,Toast.LENGTH_SHORT).show();
                    return;
                } else if ("".equals(etComment.getText().toString())) {
                    Toast.makeText(UserCommentActivity.this,TOAST3,Toast.LENGTH_SHORT).show();
                    return;
                }
                Date date = new Date();
                SimpleDateFormat dateFormat= new SimpleDateFormat(FORMAT);
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
                    d.setName(ANONYMOUS_NAME);
                }
                d.setType(type);
                d.setMsg(etComment.getText().toString());
                d.setIfReply(1);
                database.mainDao().insert(d);
                Intent intent = new Intent(UserCommentActivity.this, UserCommentSuccess.class);
                startActivity(intent);
            }
        });
        //留言编辑框滑动事件
        etComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
                if ((view.getId() == R.id.et_comment)) {
                    //垂直方向上可以滚动
                    if(etComment.canScrollVertically(-1) || etComment.canScrollVertically(0)) {
                        //请求父控件不拦截滑动事件
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                }
                return false;
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