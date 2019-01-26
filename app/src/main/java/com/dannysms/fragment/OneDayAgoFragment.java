package com.dannysms.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dannysms.R;
import com.dannysms.adapter.SmsListItemAdapter;
import com.dannysms.model.SmsModel;

import java.util.List;

public class OneDayAgoFragment extends Fragment {

    private List<SmsModel> mList;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private SmsListItemAdapter mAdapter;

    public static OneDayAgoFragment newInstance(List<SmsModel> mList) {
        OneDayAgoFragment fragment = new OneDayAgoFragment();
        fragment.mList = mList;
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sms_list_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        callRecyclerView();
    }

    private void callRecyclerView() {
        if (mList.size() > 0) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
            mRecyclerView.setLayoutManager(layoutManager);
            mAdapter = new SmsListItemAdapter(mContext, mList);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            Log.d("danny","NO SMS");
        }
    }

}