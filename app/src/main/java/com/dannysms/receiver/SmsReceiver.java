package com.dannysms.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.dannysms.R;
import com.dannysms.activity.DashboardActivity;
import com.dannysms.activity.SmsDetailActivity;
import com.dannysms.utils.Utils;

public class SmsReceiver extends BroadcastReceiver{
    private static final String TAG = SmsReceiver.class.getSimpleName();
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        mContext = context;
        final Bundle bundle = intent.getExtras();
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
                    Toast.makeText(context,"new Msg receive by :"+senderAddress,Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void simpleNotification(String title, String message, String time) {
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                mContext.getSystemService(Context.NOTIFICATION_SERVICE);


        Intent intent = new Intent(mContext, SmsDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("key_origin", "receiver");
        intent.putExtra("title", title);
        intent.putExtra("msg", message);
        intent.putExtra("time", time);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
            final int color = mContext.getResources().getColor(R.color.colorPrimaryDark);
            notification = new NotificationCompat.Builder(mContext).
                    setContentTitle(title)
                    .setContentIntent(pendingNotificationIntent)
                    .setSmallIcon(R.mipmap.ic_launcher_round).setColor(color)
                    .setWhen(when).setSubText(message)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .build();

        } else {
            notification = new NotificationCompat.Builder(mContext)
                    .setContentTitle(title)
                    .setContentIntent(pendingNotificationIntent)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setWhen(when).setSubText(message)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .build();
        }
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
        //
    }

}
