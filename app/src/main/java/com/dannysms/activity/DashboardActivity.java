package com.dannysms.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.dannysms.R;
import com.dannysms.adapter.ViewPagerAdapter;
import com.dannysms.fragment.OneDayAgoFragment;
import com.dannysms.fragment.OneHourAgoFragment;
import com.dannysms.fragment.SixHourAgoFragment;
import com.dannysms.fragment.ThreeHourAgoFragment;
import com.dannysms.fragment.TwelveHourAgoFragment;
import com.dannysms.fragment.TwoHourAgoFragment;
import com.dannysms.model.SmsModel;
import com.dannysms.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST = 1001;
    private static final String TAG = "dannySms";
    private List<SmsModel> mOneSmsList = new ArrayList<>();
    private List<SmsModel> mTwoSmsList = new ArrayList<>();
    private List<SmsModel> mThreeSmsList = new ArrayList<>();
    private List<SmsModel> mSixSmsList = new ArrayList<>();
    private List<SmsModel> mTwelveSmsList = new ArrayList<>();
    private List<SmsModel> mDaySmsList = new ArrayList<>();
    private String[] PERMISSIONS = {
            android.Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS
    };

    // @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    //@BindView(R.id.tabs)
    protected TabLayout tabLayout;
    // @BindView(R.id.viewpager)
    protected ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //ButterKnife.bind(this);
        inti();
    }

    private void inti() {
        checkSmsPermission();
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        toolbar = findViewById(R.id.toolbar);
        tabLayout.setupWithViewPager(viewPager);
        toolbar.setTitle("Message");
    }

    private void checkSmsPermission() {
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, MY_PERMISSIONS_REQUEST);
        } else {
            new GetSmsAsyncTask().execute();
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG,"onRequestPermission result all permission granted....");
                    new GetSmsAsyncTask().execute();
                } else {
                    Log.d(TAG,"onRequestPermission result denied finishing activity");
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetSmsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                mOneSmsList.clear();
                mTwoSmsList.clear();
                mThreeSmsList.clear();
                mSixSmsList.clear();
                mTwelveSmsList.clear();
                mDaySmsList.clear();
                List<SmsModel> lstSms = new ArrayList<>();
                SmsModel objSms = new SmsModel();
                Uri message = Uri.parse("content://sms/");
                ContentResolver cr = getContentResolver();

                Cursor c = cr.query(message, null, null, null, null);
                assert c != null;
                if (!c.isClosed())
                    startManagingCursor(c);
                int totalSMS = c.getCount();
                String mCurrentTime = Utils.getCurrentDateTime();
                if (c.moveToFirst()) {
                    for (int i = 0; i < totalSMS; i++) {

                        objSms = new SmsModel();
                        objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                        objSms.setAddress(c.getString(c
                                .getColumnIndexOrThrow("address")));
                        objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                        objSms.setReadState(c.getString(c.getColumnIndex("read")));
                        objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                        long mStartTime = Long.parseLong(c.getString(c.getColumnIndexOrThrow("date")));
                        long mTimeDiff = Utils.timeDiffInHr(Utils.getTimeInFormat(mStartTime),mCurrentTime);
                        Log.d(TAG,"show time currentTime :"+mCurrentTime+",, serverTime :"+Utils.getTimeInFormat(mStartTime)+" timeDiff :"+mTimeDiff);
                        if (mTimeDiff < 1){
                            Log.d(TAG,"sms within 1 hr");
                            mOneSmsList.add(objSms);
                        } else if (mTimeDiff < 2) {
                            Log.d(TAG,"sms within 2 hr");
                            mTwoSmsList.add(objSms);
                        } else if (mTimeDiff < 3) {
                            Log.d(TAG,"sms within 3 hr");
                            mThreeSmsList.add(objSms);
                        } else if (mTimeDiff >= 3 && mTimeDiff < 6) {
                            Log.d(TAG,"sms within 6 hr");
                            mSixSmsList.add(objSms);
                        }else if (mTimeDiff >= 6 && mTimeDiff  < 12) {
                            Log.d(TAG,"sms within 12 hr");
                            mTwelveSmsList.add(objSms);
                        } else if (mTimeDiff >= 12 && mTimeDiff < 24) {
                            Log.d(TAG,"sms within 1 day");
                            mDaySmsList.add(objSms);
                        } else if (mTimeDiff >= 24) {
                            Log.d(TAG,"ignore sms after 1 day");
                            break;
                        }
                        if (!c.isClosed())
                            c.moveToNext();
                    }
                }
                //c.close();

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setupViewPager(viewPager);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(OneHourAgoFragment.newInstance(mOneSmsList), "1 hr ago");
        adapter.addFragment(TwoHourAgoFragment.newInstance(mTwoSmsList), "2 hr ago");
        adapter.addFragment(ThreeHourAgoFragment.newInstance(mThreeSmsList), "3 hr ago");
        adapter.addFragment(SixHourAgoFragment.newInstance(mSixSmsList), "6 hr ago");
        adapter.addFragment(TwelveHourAgoFragment.newInstance(mTwelveSmsList), "12 hr ago");
        adapter.addFragment(OneDayAgoFragment.newInstance(mDaySmsList), "1 day ago");
        viewPager.setAdapter(adapter);
    }
}
