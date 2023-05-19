package com.sgcc.yzd.mydemo;

import android.app.Activity;
import android.app.Dialog;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.BaseViewHolder> {
    private final List<MainData> dataList;
    private final Activity context;
    private RoomDB database;
    public static String PRAISE = "表扬";
    public static String ADVICE = "建议";
    public static String COMPLAIN = "投诉";
    public static String ADMIN_NAME = "电网—";
    public static int WORD_LIMIT_NUM= 500;
    public static String WORDS_NUM = "/500";
    public static String CONTENT = "留言内容：";
    public static String TOAST1 = "请输入称呼";
    public static String TOAST2 = "请输入留言";

    /**类型，用此来判断recyclerview该用哪个布局显示*/
    public final int TYPE_EMPTY = 0;
    public final int TYPE_NORMAL = 1;

    public AdminAdapter(Activity context, List<MainData> dataList) {
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(dataList.size()<=0){
            //空数据视图类型
            return TYPE_EMPTY;
        }
        //正常视图类型
        return TYPE_NORMAL;
    }

    @NonNull
    @NotNull
    @Override
    public AdminAdapter.BaseViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        AdminAdapter.BaseViewHolder viewHolder;
        if (viewType == TYPE_EMPTY) {
            //空数据布局
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_empty_admin,parent,false);
            viewHolder = new AdminAdapter.EmptyViewHolder(view);
            return viewHolder;
        } else {
            //正常局部
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_list_admin,parent,false);
            viewHolder = new AdminAdapter.ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdminAdapter.BaseViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case TYPE_EMPTY:
                AdminAdapter.EmptyViewHolder emptyViewHolder = (AdminAdapter.EmptyViewHolder) holder;
                break;
            case TYPE_NORMAL:
                AdminAdapter.ViewHolder viewHolder = (AdminAdapter.ViewHolder) holder;
                MainData data = dataList.get(position);
                database = RoomDB.getInstance(context);
                viewHolder.tvTime.setText(data.getTime());
                //判断是否实名
                switch (data.getStatus()) {
                    //实名
                    case 0:
                        switch (data.getSex()) {
                            //男性头像
                            case 0:
                                viewHolder.imUser.setImageResource(R.mipmap.avatar2);
                                break;
                            //女性头像
                            case 1:
                                viewHolder.imUser.setImageResource(R.mipmap.avatar1);
                                break;
                            default:
                                break;
                        }
                        viewHolder.tvName.setText(data.getName());
                        break;
                    //匿名
                    case 1:
                        //匿名头像
                        viewHolder.imUser.setImageResource(R.mipmap.avatar3);
                        viewHolder.tvName.setText(data.getName());
                        break;
                    default:
                        break;
                }
                //判断留言类型
                switch (data.getType()) {
                    //表扬
                    case 0:
                        viewHolder.tvType.setText(PRAISE);
                        viewHolder.tvType.setBackgroundResource(R.drawable.border_type_praise);
                        break;
                    //建议
                    case 1:
                        viewHolder.tvType.setText(ADVICE);
                        viewHolder.tvType.setBackgroundResource(R.drawable.border_type_advice);
                        break;
                    //投诉
                    case 2:
                        viewHolder.tvType.setText(COMPLAIN);
                        viewHolder.tvType.setBackgroundResource(R.drawable.border_type_complain);
                        break;
                    default:
                        break;
                }
                String msg = (data.getMsg()==null) ? "" : data.getMsg();
                viewHolder.tvMsg.setText(msg);
                //判断是否回复
                switch (data.getIfReply()) {
                    //已回复
                    case 0:
                        viewHolder.relativeLayout.setVisibility(View.VISIBLE);
                        String adminName = ADMIN_NAME + data.getAdminName();
                        viewHolder.tvAdminName.setText(adminName);
                        String reply = data.getReply();
                        viewHolder.tvReply.setText(reply);
                        break;
                    //未回复
                    case 1:
                        viewHolder.relativeLayout.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
                //查看详情点击事件
                viewHolder.btDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edit(data);
                    }
                });
                //重新编辑点击事件
                viewHolder.btEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edit(data);
                    }
                });
                break;
            default:
                break;
        }

    }


    @Override
    public int getItemCount() {
        if (dataList.size() <= 0) {
            return 1;
        } else {
            return dataList.size();
        }
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }

    public class EmptyViewHolder extends AdminAdapter.BaseViewHolder {
        public EmptyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }

    public class ViewHolder extends AdminAdapter.BaseViewHolder {

        ImageView imUser;
        TextView tvTime, tvName, tvType, tvMsg, tvAdminName, tvReply;
        Button btDetail, btEdit;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imUser = itemView.findViewById(R.id.im_user);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvName = itemView.findViewById(R.id.tv_name);
            tvType = itemView.findViewById(R.id.tv_type);
            tvMsg = itemView.findViewById(R.id.tv_msg);
            relativeLayout = itemView.findViewById(R.id.ll_reply);
            tvAdminName = itemView.findViewById(R.id.tv_admin_name);
            tvReply = itemView.findViewById(R.id.tv_reply);
            btDetail = itemView.findViewById(R.id.bt_detail);
            btEdit = itemView.findViewById(R.id.bt_edit);
        }
    }

    public void edit(MainData data) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.comment_detail);
        int width = context.getResources().getDisplayMetrics().widthPixels;
        //Initialize height
        int height = context.getResources().getDisplayMetrics().heightPixels;
        //Set layout
        dialog.getWindow().setLayout(width*750/960,height*509/600);
        //Show dialog
        dialog.show();

        EditText etCall, etReply;
        TextView tvName,tvType,tvTel,tvTime,tvComment,tvWordsNum;
        Button btBack, btCommit;
        btBack = dialog.findViewById(R.id.bt_back);
        btCommit = dialog.findViewById(R.id.bt_commit);
        etCall = dialog.findViewById(R.id.et_call);
        etReply = dialog.findViewById(R.id.et_reply);
        tvName = dialog.findViewById(R.id.tv_name);
        tvTel = dialog.findViewById(R.id.tv_tel);
        tvType = dialog.findViewById(R.id.tv_type);
        tvTime = dialog.findViewById(R.id.tv_time);
        tvComment = dialog.findViewById(R.id.tv_comment);
        tvWordsNum = dialog.findViewById(R.id.tv_words_num);
        String tel,time;
        //获取客户姓名
        tvName.setText(data.getName());
        //获取留言内容
        String msg = (data.getMsg()==null) ? "" : data.getMsg();
        //Spannable设置不同大小样式的文本，“留言内容”字体颜色设为黑色
        Spannable span = new SpannableString(CONTENT+msg);
        span.setSpan(new AbsoluteSizeSpan(13,true), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.black)), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvComment.setText(span);

        //获取联系方式
        tel = data.getTel();
        tvTel.setText((tel==null)?"":tel);
        //获取留言时间
        time = data.getTime();
        tvTime.setText(time);
        //获取留言类型
        switch (data.getType()) {
            case 0:
                tvType.setText("表扬");
                tvType.setBackgroundResource(R.drawable.border_type_praise);
                break;
            case 1:
                tvType.setText("建议");
                tvType.setBackgroundResource(R.drawable.border_type_advice);
                break;
            case 2:
                tvType.setText("批评");
                tvType.setBackgroundResource(R.drawable.border_type_complain);
                break;
            default:
                break;
        }

        //获取回复内容
        switch (data.getIfReply()) {
            case 0:
                String adminCall = (data.getAdminName()==null) ? "" : data.getAdminName();
                etCall.setText(adminCall);
                String reply = (data.getReply()==null) ? "" : data.getReply();
                etReply.setText(reply);
                break;
            case 1:
            default:
                break;
        }

        /* 回复对话框返回按钮点击事件 */
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        /* 回复对话框提交按钮点击事件 */
        btCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(etCall.getText().toString())) {
                    Toast.makeText(context,TOAST1,Toast.LENGTH_SHORT).show();
                    return;
                } else if ("".equals(etReply.getText().toString())) {
                    Toast.makeText(context,TOAST2,Toast.LENGTH_SHORT).show();
                    return;
                }

                String adminCall = etCall.getText().toString().trim();
                String reply = etReply.getText().toString().trim();
                database.mainDao().update(data.getID(),0,adminCall,reply);
                //Notify when data is updated
                dataList.clear();
                dataList.addAll(database.mainDao().getAll());
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        //回复编辑框滑动事件
        etReply.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
                if ((view.getId() == R.id.et_reply)) {
                    //垂直方向上可以滚动
                    if(etReply.canScrollVertically(-1) || etReply.canScrollVertically(0)) {
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

        tvWordsNum.setText(etReply.getText().length() + WORDS_NUM);

        etReply.addTextChangedListener(new TextWatcher() {
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
                selectionStart = etReply.getSelectionStart();
                selectionEnd = etReply.getSelectionEnd();
                if (enterWords.length() > WORD_LIMIT_NUM) {
                    //删除多余输入的字（不会显示出来）
                    editable.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    etReply.setText(editable);
                    //设置光标在最后
                    etReply.setSelection(tempSelection);
                }
            }
        });
    }
}
