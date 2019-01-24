package com.dannysms;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dannysms.model.SmsModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 1001;
    private static final String TAG = "danny";
    private List<SmsModel> mSmsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        inti();
    }

    private void inti() {
    checkSmsPermission();
    }

    private void checkSmsPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    MY_PERMISSIONS_REQUEST_READ_SMS);
        } else {
           new GetSmsAsyncTask().execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new GetSmsAsyncTask().execute();
                } else {
                    Log.d(TAG,"onRequestPermission result denied finishing activity");
                }
                return;
            }
        }
    }

    private class GetSmsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            List<SmsModel> lstSms = new ArrayList<>();
            SmsModel objSms = new SmsModel();
            Uri message = Uri.parse("content://sms/inbox");
            ContentResolver cr = getContentResolver();

            Cursor c = cr.query(message, null, null, null, null);
            startManagingCursor(c);
            int totalSMS = c.getCount();

            if (c.moveToFirst()) {
                for (int i = 0; i < totalSMS; i++) {

                    objSms = new SmsModel();
                    objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                    objSms.setAddress(c.getString(c
                            .getColumnIndexOrThrow("address")));
                    objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                    objSms.setReadState(c.getString(c.getColumnIndex("read")));
                    objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));

                    lstSms.add(objSms);
                    c.moveToNext();
                }
            } else {
                Log.d(TAG,"no sms avaiable in inbox");
            }
            c.close();
            mSmsList = lstSms;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG,"sms list :"+mSmsList.size());
        }
    }
}
