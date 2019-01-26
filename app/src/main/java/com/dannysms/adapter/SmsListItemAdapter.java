package com.dannysms.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dannysms.R;
import com.dannysms.activity.SmsDetailActivity;
import com.dannysms.model.SmsModel;
import com.dannysms.utils.Utils;

import java.util.List;

public class SmsListItemAdapter extends RecyclerView.Adapter<SmsListItemAdapter.DateViewHolder>  {

    private final Context mContext;
    private List<SmsModel> mList;
    private FragmentManager mFragmentManager;

    public SmsListItemAdapter(Context context, List<SmsModel> list) {
        this.mList = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sms_list_item, viewGroup, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, final int i) {
        String mCurrentTime = Utils.getCurrentDateTime();
       holder.tvTitle.setText(mList.get(i).getAddress());
       holder.tvMsg.setText(mList.get(i).getMsg());
       holder.tvTime.setText(Utils.timeDiff(Utils.getTimeInFormat(Long.parseLong(mList.get(i).getTime())),mCurrentTime));
       holder.llMain.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(mContext,SmsDetailActivity.class);
               intent.putExtra("key_origin","dashboard");
               intent.putExtra("title",mList.get(i).getAddress());
               intent.putExtra("msg",mList.get(i).getMsg());
               intent.putExtra("time",mList.get(i).getTime());
               mContext.startActivity(intent);
           }
       });
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class DateViewHolder extends RecyclerView.ViewHolder{

        protected RelativeLayout llMain;
        protected TextView tvTitle;
        protected TextView tvMsg;
        protected TextView tvTime;
        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            llMain = itemView.findViewById(R.id.ll_main);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvMsg = itemView.findViewById(R.id.tv_msg);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
