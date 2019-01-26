package com.dannysms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.dannysms.R;
import com.dannysms.utils.Utils;

public class SmsDetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mTvMsg;
    private TextView mTvTime;
    private String mTitle = null;
    private String mMsg = null;
    private String mTime = null;
    private String fromOrigin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_detail_activity);
        init();
    }

    private void init() {
        mToolbar = findViewById(R.id.toolbar);
        mTvMsg = findViewById(R.id.tv_msg);
        mTvTime = findViewById(R.id.tv_time);
        if (getIntent() != null && getIntent().getExtras() != null) {
            fromOrigin = getIntent().getExtras().getString("key_origin","dashboard");
            mTitle = getIntent().getExtras().getString("title", "Message");
            mMsg =getIntent().getExtras().getString("msg", "");
            mTime = getIntent().getExtras().getString("time", "");
        }
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(mTitle);
        mTvMsg.setText(mMsg);
        if (fromOrigin.equals("dashboard")) {
            mTvTime.setText(Utils.getTimeInFormat(Long.parseLong(mTime)));
        } else {
            mTvTime.setText(mTime);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (fromOrigin.equals("dashboard")) {
            onBackPressed();
        } else {
            Intent intent = new Intent(this,DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (fromOrigin.equals("dashboard")) {
            super.onBackPressed();
        } else {
            Intent intent = new Intent(this,DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
