package com.sgcc.yzd.mydemo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<MainData> dataList;
    private Activity context;
    private RoomDB database;
    public static String ANONYMOUSNAME = "匿名用户";
    public static String PRAISE = "表扬";
    public static String ADVICE = "建议";
    public static String COMPLAIN = "投诉";

    public MainAdapter(Activity context,List<MainData> dataList) {
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_list_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MainAdapter.ViewHolder holder, int position) {
        MainData data = dataList.get(position);
        database = RoomDB.getInstance(context);
        holder.tvTime.setText(data.getTime());
        String name = data.getName();
        switch (data.getStatus()) {
            //0代表实名,1代表匿名
            case 0:
                switch (data.getSex()) {
                    //0代表男,1代表女
                    case 0:
                        holder.imUser.setImageDrawable(context.getResources()
                                .getDrawable(R.drawable.user_male));
                        break;
                    case 1:
                        holder.imUser.setImageDrawable(context.getResources().
                                getDrawable(R.drawable.user_female));
                        break;
                }
                holder.tvName.setText(name);
                break;
            case 1:
                holder.imUser.setImageDrawable(context.getResources()
                        .getDrawable(R.drawable.user_anonymous));
                holder.tvName.setText(ANONYMOUSNAME);
                break;
        }
        switch (data.getType()) {
            case 0:
                holder.tvType.setText(PRAISE);
                holder.tvType.setBackground(context.getResources()
                        .getDrawable(R.drawable.border_type_praise));
                break;
            case 1:
                holder.tvType.setText(ADVICE);
                holder.tvType.setBackground(context.getResources()
                        .getDrawable(R.drawable.border_type_advice));
                break;
            case 2:
                holder.tvType.setText(COMPLAIN);
                holder.tvType.setBackground(context.getResources()
                        .getDrawable(R.drawable.border_type_complain));
                break;
        }
        String msg = data.getMsg();
        holder.tvMsg.setText((msg==null) ? "" : msg);


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imUser;
        TextView tvTime, tvName, tvType, tvMsg;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imUser = itemView.findViewById(R.id.im_user);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvName = itemView.findViewById(R.id.tv_name);
            tvType = itemView.findViewById(R.id.tv_type);
            tvMsg = itemView.findViewById(R.id.tv_msg);
        }
    }
}
