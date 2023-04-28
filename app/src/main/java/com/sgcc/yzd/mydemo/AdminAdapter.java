package com.sgcc.yzd.mydemo;

import android.app.Activity;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.BaseViewHolder> {
    private List<MainData> dataList;
    private Activity context;
    private RoomDB database;
    public static String ANONYMOUS_NAME = "匿名用户";
    public static String PRAISE = "表扬";
    public static String ADVICE = "建议";
    public static String COMPLAIN = "投诉";
    public static String ADMIN_NAME = "电网—";
    public static int WORD_LIMIT_NUM= 500;
    public static String WORDS_NUM = "/500";

    //类型，用此来判断recyclerview该用哪个布局显示
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
            return TYPE_EMPTY;
        }
        return TYPE_NORMAL;
    }

    @NonNull
    @NotNull
    @Override
    public AdminAdapter.BaseViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        AdminAdapter.BaseViewHolder viewHolder;
        if (viewType == TYPE_EMPTY) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_empty_admin,parent,false);
            viewHolder = new AdminAdapter.EmptyViewHolder(view);
            return viewHolder;
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_list_admin,parent,false);
            viewHolder = new AdminAdapter.ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdminAdapter.BaseViewHolder holder, int position) {
        int type = getItemViewType(position);
        String name = "";
        switch (type) {
            case TYPE_EMPTY:
                AdminAdapter.EmptyViewHolder emptyViewHolder = (AdminAdapter.EmptyViewHolder) holder;
                break;
            case TYPE_NORMAL:
                AdminAdapter.ViewHolder viewHolder = (AdminAdapter.ViewHolder) holder;
                MainData data = dataList.get(position);
                database = RoomDB.getInstance(context);
                viewHolder.tvTime.setText(data.getTime());
                switch (data.getStatus()) {
                    //0代表实名,1代表匿名
                    case 0:
                        switch (data.getSex()) {
                            //0代表男,1代表女
                            case 0:
                                viewHolder.imUser.setImageDrawable(context.getResources()
                                        .getDrawable(R.drawable.user_male));
                                break;
                            case 1:
                                viewHolder.imUser.setImageDrawable(context.getResources().
                                        getDrawable(R.drawable.user_female));
                                break;
                        }
                        name = data.getName();
                        viewHolder.tvName.setText(name);
                        break;
                    case 1:
                        viewHolder.imUser.setImageDrawable(context.getResources()
                                .getDrawable(R.drawable.user_anonymous));
                        name = ANONYMOUS_NAME;
                        viewHolder.tvName.setText(name);
                        break;
                }
                switch (data.getType()) {
                    case 0:
                        viewHolder.tvType.setText(PRAISE);
                        viewHolder.tvType.setBackground(context.getResources()
                                .getDrawable(R.drawable.border_type_praise));
                        break;
                    case 1:
                        viewHolder.tvType.setText(ADVICE);
                        viewHolder.tvType.setBackground(context.getResources()
                                .getDrawable(R.drawable.border_type_advice));
                        break;
                    case 2:
                        viewHolder.tvType.setText(COMPLAIN);
                        viewHolder.tvType.setBackground(context.getResources()
                                .getDrawable(R.drawable.border_type_complain));
                        break;
                }
                String msg = (data.getMsg()==null) ? "" : data.getMsg();
                viewHolder.tvMsg.setText(msg);
                switch (data.getIfReply()) {
                    case 0:
                        viewHolder.relativeLayout.setVisibility(View.VISIBLE);
                        String adminName = ADMIN_NAME + data.getAdminName();
                        viewHolder.tvAdminName.setText(adminName);
                        String reply = data.getReply();
                        viewHolder.tvReply.setText(reply);
                        break;
                    case 1:
                        viewHolder.relativeLayout.setVisibility(View.GONE);
                        break;
                }
                String username = name;
                viewHolder.btDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                        tvName.setText(username);
                        tvComment.setText(msg);
                        tel = data.getTel();
                        tvTel.setText((tel==null)?"":tel);
                        time = data.getTime();
                        tvTime.setText(time);

                        switch (data.getType()) {
                            case 0:
                                tvType.setText("表扬");
                                tvType.setBackground(context.getResources()
                                        .getDrawable(R.drawable.border_type_praise));
                                break;
                            case 1:
                                tvType.setText("建议");
                                tvType.setBackground(context.getResources()
                                        .getDrawable(R.drawable.border_type_advice));
                                break;
                            case 2:
                                tvType.setText("批评");
                                tvType.setBackground(context.getResources()
                                        .getDrawable(R.drawable.border_type_complain));
                                break;
                        }

                        switch (data.getIfReply()) {
                            case 0:
                                String adminCall = (data.getAdminName()==null) ? "" : data.getAdminName();
                                etCall.setText(adminCall);
                                String reply = (data.getReply()==null) ? "" : data.getReply();
                                etReply.setText(reply);
                                break;
                            case 1:
                                break;
                        }

                        btBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        btCommit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String adminCall = etCall.getText().toString().trim();
                                String reply = etReply.getText().toString().trim();
//                                data.setIfReply(0);
//                                data.setAdminName(adminCall);
//                                data.setReply(reply);
                                database.mainDao().update(data.getID(),0,adminCall,reply);
                                //Notify when data is updated
                                dataList.clear();
                                dataList.addAll(database.mainDao().getAll());
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });

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
                });

                viewHolder.btEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                        tvName.setText(username);
                        tvComment.setText(msg);
                        tel = data.getTel();
                        tvTel.setText((tel==null)?"":tel);
                        time = data.getTime();
                        tvTime.setText(time);

                        switch (data.getType()) {
                            case 0:
                                tvType.setText("表扬");
                                tvType.setBackground(context.getResources()
                                        .getDrawable(R.drawable.border_type_praise));
                                break;
                            case 1:
                                tvType.setText("建议");
                                tvType.setBackground(context.getResources()
                                        .getDrawable(R.drawable.border_type_advice));
                                break;
                            case 2:
                                tvType.setText("批评");
                                tvType.setBackground(context.getResources()
                                        .getDrawable(R.drawable.border_type_complain));
                                break;
                        }

                        switch (data.getIfReply()) {
                            case 0:
                                String adminCall = (data.getAdminName()==null) ? "" : data.getAdminName();
                                etCall.setText(adminCall);
                                String reply = (data.getReply()==null) ? "" : data.getReply();
                                etReply.setText(reply);
                                break;
                            case 1:
                                break;
                        }

                        btBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        btCommit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String adminCall = etCall.getText().toString().trim();
                                String reply = etReply.getText().toString().trim();
//                                data.setIfReply(0);
//                                data.setAdminName(adminCall);
//                                data.setReply(reply);
                                database.mainDao().update(data.getID(),0,adminCall,reply);
                                //Notify when data is updated
                                dataList.clear();
                                dataList.addAll(database.mainDao().getAll());
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });

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
                });



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
}
