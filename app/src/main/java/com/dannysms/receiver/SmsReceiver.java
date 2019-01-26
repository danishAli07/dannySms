package com.dannysms.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.dannysms.R;
import com.dannysms.activity.SmsDetailActivity;
import com.dannysms.utils.Utils;

public class SmsReceiver extends BroadcastReceiver{
    private static final String TAG = SmsReceiver.class.getSimpleName();
    private Context mContext;
    private String CHANNEL_ID = "id_danny";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        mContext = context;
        final Bundle bundle = intent.getExtras();
        createNotificationChannel();
        try {
            if (bundle != null)
            {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for(Object aPdusObj : pdusObj)
                {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);

                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage .getDisplayMessageBody();
                    Log.e(TAG, "Received SMS: " + message + ", Sender: " + senderAddress);
                    simpleNotification(senderAddress,message,Utils.getCurrentDateTime());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = mContext.getString(R.string.app_name);
            String description = mContext.getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void simpleNotification(String title, String message, String time) {
        Log.d(TAG,"simpleNotification called..");

        Intent intent = new Intent(mContext, SmsDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("key_origin", "receiver");
        intent.putExtra("title", title);
        intent.putExtra("msg", message);
        intent.putExtra("time", time);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingNotificationIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(010, mBuilder.build());
        //
    }

}
