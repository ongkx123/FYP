package com.example.myapplication;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {

    public static final String CHANNEL_ID= "channel";
    public static final String CHANNEL_Name= "Channel";

    private NotificationManager mManager;

    public NotificationHelper(Context base){
        super(base);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannel(){

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_Name, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(R.color.colorPrimary);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager().createNotificationChannel(notificationChannel);

    }

    public NotificationManager getManager(){
        if(mManager == null){
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(String title,String message,String userName){
        Intent intent = new Intent(this,ChatActivity.class);
        intent.putExtra("name",title);
        intent.putExtra("userName",userName);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_baseline_message_24)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
    }
}
