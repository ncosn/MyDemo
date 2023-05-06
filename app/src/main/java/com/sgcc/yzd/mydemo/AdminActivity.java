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

public class AdminActivity extends AppCompatActivity {
    Integer ifReply=0, type=3, time = 0;

    TextView tvExit,tvTitle;
    Button btClear, btFilter, btTop;
    RadioGroup rgIfReply, rgType;
    RadioButton rbAll, rbReply, rbNoReply, rbPraise, rbAdvice, rbComplain;
    Toolbar toolbar;
    Spinner spTime;

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
        rbAll = findViewById(R.id.rb_all);
        rbReply = findViewById(R.id.rb_reply);
        rbNoReply = findViewById(R.id.rb_no_reply);

        rgType = findViewById(R.id.rg_type);
        rbPraise = findViewById(R.id.rb_praise);
        rbAdvice = findViewById(R.id.rb_advice);
        rbComplain = findViewById(R.id.rb_complain);

        spTime = findViewById(R.id.sp_time);

        recyclerView = findViewById(R.id.recycler_view);

        database = RoomDB.getInstance(this);
        dataList = database.mainDao().getAll();
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adminAdapter = new AdminAdapter(AdminActivity.this,dataList);
        recyclerView.setAdapter(adminAdapter);

        rgIfReply.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_all:
                        ifReply = 0;
                        break;
                    case R.id.rb_reply:
                        ifReply = 1;
                        break;
                    case R.id.rb_no_reply:
                        ifReply = 2;
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

        spTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                time = i;
//                Log.e("test",""+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rgType.clearCheck();
                type = 3;
                rgIfReply.check(R.id.rb_all);
                spTime.setSelection(0);
            }
        });

        btFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataList.clear();
                String startDate = "", endDate;
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date now = cal.getTime();
                endDate = dateFormat.format(now);
                switch (time) {
                    case 0:
                        cal.add(Calendar.DATE, -7);
                        startDate = dateFormat.format(cal.getTime());
                        break;
                    case 1:
                        cal.add(Calendar.MONTH, -1);
                        startDate = dateFormat.format(cal.getTime());
                        break;
                    case 2:
                        cal.add(Calendar.MONTH, -3);
                        startDate = dateFormat.format(cal.getTime());
                        break;
                    case 3:
                        cal.add(Calendar.YEAR, -1);
                        startDate = dateFormat.format(cal.getTime());
                        break;
                }
                switch (ifReply) {
                    case 0:
                        //Notify when data is updated
                        if (type == 3) {
                            dataList.addAll(database.mainDao().getAllTime(startDate,endDate));
                        } else {
                            dataList.addAll(database.mainDao().getAllTypeTime(type,startDate,endDate));
                        }
                        break;
                    case 1:
                        if (type == 3) {
                            dataList.addAll(database.mainDao().getReplyAllTime(startDate,endDate));
                        } else {
                            dataList.addAll(database.mainDao().getFilterTime(0,type,startDate,endDate));
                        }
                        break;
                    case 2:
                        if (type == 3) {
                            dataList.addAll(database.mainDao().getNoReplyAllTime(startDate,endDate));
                        } else {
                            dataList.addAll(database.mainDao().getFilterTime(1,type,startDate,endDate));
                        }
                        break;
                }
                adminAdapter.notifyDataSetChanged();
            }
        });

    }
}