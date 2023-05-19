package com.sgcc.yzd.mydemo;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.BaseViewHolder> {

    private List<MainData> dataList;
    private Activity context;
    private RoomDB database;
    public static String ANONYMOUS_NAME = "匿名用户";
    public static String PRAISE = "表扬";
    public static String ADVICE = "建议";
    public static String COMPLAIN = "投诉";
    public static String ADMIN_NAME = "电网—";

    /**
     *类型，用此来判断recyclerview该用哪个布局显示
     **/
    public final int TYPE_EMPTY = 0;
    public final int TYPE_NORMAL = 1;

    public MainAdapter(Activity context,List<MainData> dataList) {
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
        return  TYPE_NORMAL;
    }

    @NonNull
    @NotNull
    @Override
    public MainAdapter.BaseViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        BaseViewHolder viewHolder;
        if (viewType == TYPE_EMPTY) {
            //空数据布局
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_empty,parent,false);
            viewHolder = new EmptyViewHolder(view);
            return viewHolder;
        } else {
            //正常布局
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_list_user,parent,false);
            viewHolder = new ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MainAdapter.BaseViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case TYPE_EMPTY:
                EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
                break;
            case TYPE_NORMAL:
                ViewHolder viewHolder = (ViewHolder) holder;
                MainData data = dataList.get(position);
                database = RoomDB.getInstance(context);
                viewHolder.tvTime.setText(data.getTime());
                String name = data.getName();
                //判断是否实名
                switch (data.getStatus()) {
                    //实名
                    case 0:
                        switch (data.getSex()) {
                            //0代表男,1代表女
                            case 0:
                                viewHolder.imUser.setImageResource(R.mipmap.avatar2);
                                break;
                            case 1:
                                viewHolder.imUser.setImageResource(R.mipmap.avatar1);
                                break;
                            default:
                                break;
                        }
                        viewHolder.tvName.setText(name);
                        break;
                    //匿名
                    case 1:
                        viewHolder.imUser.setImageResource(R.mipmap.avatar3);
                        viewHolder.tvName.setText(ANONYMOUS_NAME);
                        break;
                    default:
                        break;
                }
                //判断类型
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
                String msg = data.getMsg();
                viewHolder.tvMsg.setText((msg==null) ? "" : msg);
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

//                Log.d("test", "onBindViewHolder: 验证是否重用:");
//
//                Log.d("test", "onBindViewHolder: 重用了"+viewHolder.tvName.getTag());
//
//                Log.d("test", "onBindViewHolder: 放入"+data.getID());
//
//                viewHolder.tvName.setTag(data.getID());
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        if (dataList.size() <= 0) {
            //无数据
            return 1;
        } else {
            return dataList.size();
        }
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder{

        public BaseViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }

    public class EmptyViewHolder extends BaseViewHolder{
        public EmptyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }

    public class ViewHolder extends BaseViewHolder{

        ImageView imUser;
        TextView tvTime, tvName, tvType, tvMsg, tvAdminName, tvReply;
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
        }
    }
}
