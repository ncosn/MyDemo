package com.sgcc.yzd.mydemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sgcc.yzd.mydemo.view.SuperEasyRefreshLayout;

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
    static final int PAGE_LIMIT = 5;
    int pageIndex = 0,total = 0;
    boolean more = false;

    List<MainData> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RoomDB database;
    MainAdapter mainAdapter;

    SuperEasyRefreshLayout swipeRefreshLayout;

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
        //找到刷新对象
        swipeRefreshLayout = (SuperEasyRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(TOOLBAR_TITLE);

        //Initialize database
        database = RoomDB.getInstance(this);
//        dataList = database.mainDao().getAll();
        total = database.mainDao().getCount();
        Log.e("MainActivity", "init: total:"+total);
        pageIndex = 0;
        if (dataList != null && dataList.size() > 0) {
            dataList.clear();
        }
        more = total > PAGE_LIMIT;
        if (more) {
            dataList = database.mainDao().queryPage(PAGE_LIMIT,0);
        } else {
            dataList = database.mainDao().getAll();
        }

//        //初始化为第一页
//        pageIndex = 0;
//        //总数，小于一页的查询数，全部查询
//        if (PAGE_LIMIT > total) {
//            if (dataList != null && dataList.size() > 0) {
//                dataList.clear();
//            }
//            dataList = database.mainDao().getAll();
//        } else if (pageIndex == 0) {
//            //第一页
//            if (dataList != null && dataList.size() > 0) {
//                dataList.clear();
//            }
//            dataList = database.mainDao().queryPage(PAGE_LIMIT, 0);
//        }

        //Initialize linear layout manager
        linearLayoutManager = new LinearLayoutManager(this);
        //Set layout manager
        recyclerView.setLayoutManager(linearLayoutManager);
        mainAdapter = new MainAdapter(MainActivity.this,dataList);
        //Set adapter
        recyclerView.setAdapter(mainAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SuperEasyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        pageIndex = 0;
                        more = total > PAGE_LIMIT;
                        Log.e("MainActivity", "run: more:"+more);
                        dataList.clear();
                        Log.e("MainActivity", "run: size1:"+dataList.size());
                        if (more) {
                            dataList.addAll(database.mainDao().queryPage(PAGE_LIMIT,0));
                        } else {
                            dataList.addAll(database.mainDao().getAll());
                        }
                        Log.e("MainActivity", "run: size2:"+dataList.size());
                        mainAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
                    }
                },2000);
            }
        });

        swipeRefreshLayout.setOnLoadMoreListener(new SuperEasyRefreshLayout.OnLoadMoreListener() {
            @Override
            public void onLoad() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (more) {
                            pageIndex++;
                            if (PAGE_LIMIT > (total - pageIndex * PAGE_LIMIT)) {
                                more = false;
                                dataList.addAll(database.mainDao().queryPage(total - pageIndex * PAGE_LIMIT, pageIndex * PAGE_LIMIT));
                            } else {
                                dataList.addAll(database.mainDao().queryPage(PAGE_LIMIT, pageIndex * PAGE_LIMIT));
                            }
//                            mainAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.finishLoadMore();
                            Toast.makeText(MainActivity.this,"加载更多成功",Toast.LENGTH_SHORT).show();
                        } else {
                            swipeRefreshLayout.finishLoadMore();
                            Toast.makeText(MainActivity.this,"已全部加载",Toast.LENGTH_SHORT).show();
                        }
                    }
                },2000);
            }
        });

        //动态显示返回顶部按钮
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

//        //添加recyclerview的滑动监听
//        recyclerView.addOnScrollListener(new EndLessOnScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int currentPage, int totalItemCount) {
//                if (pageIndex == currentPage) {
//                    return;
//                }
//                pageIndex++;
//                if (totalItemCount < total) {
//                    dataList.addAll(database.mainDao().queryPage(PAGE_LIMIT, totalItemCount));
//                }else{//如果重新数据已经超出总数
//                    dataList.addAll(database.mainDao().queryPage(total-totalItemCount, totalItemCount));
//                }
//                mainAdapter.notifyDataSetChanged();
//                Log.e("MainActivity", "onLoadMore: 加载第"+pageIndex+"页，共"+dataList.size()+"条留言");
//            }
//        });


        //管理员登录
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //防止自动弹出软键盘
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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


        //返回顶部点击事件
        btTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        //留言点击事件
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