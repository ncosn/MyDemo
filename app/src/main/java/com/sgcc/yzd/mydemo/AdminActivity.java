package com.sgcc.yzd.mydemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author 86181
 */
public class AdminActivity extends AppCompatActivity {

    /**
     * 是否回复标志，0表示已回复，1表示未回复，2表示全部
     */
    private int ifReply=2;
    /**
     * 类型标志，0表示表扬，1表示建议，2表示投诉，3表示全部
     */
    private int type = 3;
    /**
     * 时间标志，0表示全部，1表示一周内，2表示一个月内，3表示三个月内，4表示一年内
     */
    private int time = 0;
    /**
     * 年龄标志，0表示全部，1表示25岁以下，2表示25-35岁，3表示35岁以上
     */
    private int age = 0;

    TextView tvExit,tvTitle;
    Button btClear, btFilter, btTop;
    RadioGroup rgIfReply, rgType;
    RadioButton rbAll, rbReply, rbNoReply, rbPraise, rbAdvice, rbComplain;
    Toolbar toolbar;
    Spinner spTime;
    Spinner spAge;

    RoomDB database;
    RecyclerView recyclerView;
    List<MainData> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    AdminAdapter adminAdapter;

    public static String TOOLBAR_TITLE = "电子意见簿 | 管理员";
    public static String FORMAT = "yyyy-MM-dd HH:mm";

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
        rbAll = findViewById(R.id.rb_all);
        rbReply = findViewById(R.id.rb_reply);
        rbNoReply = findViewById(R.id.rb_no_reply);

        rgType = findViewById(R.id.rg_type);
        rbPraise = findViewById(R.id.rb_praise);
        rbAdvice = findViewById(R.id.rb_advice);
        rbComplain = findViewById(R.id.rb_complain);

        spTime = findViewById(R.id.sp_time);
        spAge = findViewById(R.id.sp_age);

        recyclerView = findViewById(R.id.recycler_view);

        //初始化数据库
        database = RoomDB.getInstance(this);
        //获取意见表所有数据
        dataList = database.mainDao().getAll();
        //创建并设置布局管理器
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //初始化适配器
        adminAdapter = new AdminAdapter(AdminActivity.this,dataList);
        //设置适配器
        recyclerView.setAdapter(adminAdapter);

        //筛选留言——是否回复
        rgIfReply.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    //全部
                    case R.id.rb_all:
                        ifReply = 2;
                        break;
                    //已回复
                    case R.id.rb_reply:
                        ifReply = 0;
                        break;
                    //未回复
                    case R.id.rb_no_reply:
                        ifReply = 1;
                        break;
                    default:
                        break;
                }
            }
        });

        //筛选留言——类型
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    //表扬
                    case R.id.rb_praise:
                        type = 0;
                        break;
                    //建议
                    case R.id.rb_advice:
                        type = 1;
                        break;
                    //投诉
                    case R.id.rb_complain:
                        type = 2;
                        break;
                    default:
                        break;
                }
            }
        });

        //动态显示回到顶部按钮
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //第一个item不在屏幕内
                if (recyclerView.getLayoutManager().findViewByPosition(0)==null) {
                    btTop.setVisibility(View.VISIBLE);
                } else {
                    btTop.setVisibility(View.GONE);
                }
            }
        });

        //管理员退出
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //回到顶部点击事件
        btTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        /* 时间下拉框点击事件 */
        spTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                time = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /* 年龄下拉框点击事件 */
        spAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                age = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /* 清除筛选留言 */
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rgType.clearCheck();
                //留言类型设为全部
                type = 3;
                //是否回复设为全部
                rgIfReply.check(R.id.rb_all);
                //时间设为全部
                spTime.setSelection(0);
                //年龄设为全部
                spAge.setSelection(0);
                //清空list表
                dataList.clear();
                //数据库getAll()查询全部留言
                dataList.addAll(database.mainDao().getAll());
                //更新列表
                adminAdapter.notifyDataSetChanged();
            }
        });

        /* 筛选留言 */
        btFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataList.clear();
                String startDate = "", endDate;
                //获取Calendar类型的时间
                Calendar cal = Calendar.getInstance();
                //定义日期格式
                SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT);
                //获取Date类型的时间
                Date now = cal.getTime();
                //转换为字符串类型自定义格式时间
                endDate = dateFormat.format(now);
                /*
                根据时间time进行筛选：
                0显示全部，startDate为当前时间减20年；
                1显示一周内，startDate为当前时间减7天；
                2显示一个月内，startDate为当前时间减1个月；
                3显示一年内，startDate为当前时间减1年。
                 */
                switch (time) {
                    case 0:
                        cal.add(Calendar.YEAR, -20);
                        startDate = dateFormat.format(cal.getTime());
                        break;
                    case 1:
                        cal.add(Calendar.DATE, -7);
                        startDate = dateFormat.format(cal.getTime());
                        break;
                    case 2:
                        cal.add(Calendar.MONTH, -1);
                        startDate = dateFormat.format(cal.getTime());
                        break;
                    case 3:
                        cal.add(Calendar.MONTH, -3);
                        startDate = dateFormat.format(cal.getTime());
                        break;
                    case 4:
                        cal.add(Calendar.YEAR, -1);
                        startDate = dateFormat.format(cal.getTime());
                        break;
                    default:
                        break;
                }
                //数据库getFilter()函数查询
                dataList.addAll(database.mainDao().getFilter(ifReply,type,startDate,endDate));
                //更新列表
                adminAdapter.notifyDataSetChanged();
            }
        });

    }
}