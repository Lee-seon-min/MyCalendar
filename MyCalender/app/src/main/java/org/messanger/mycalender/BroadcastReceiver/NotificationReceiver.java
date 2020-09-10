package org.messanger.mycalender.BroadcastReceiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

import org.messanger.mycalender.Fragment.AlarmFragment;
import org.messanger.mycalender.R;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences=context.getSharedPreferences(AlarmFragment.ALARM_FOLDER_NAME,Context.MODE_PRIVATE);
        String title=sharedPreferences.getString(AlarmFragment.ALARM_FILE_TITLE,null);
       //long milliTime=sharedPreferences.getLong(AlarmFragment.ALARM_FILE_TIME,-1);

        NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE); //알람서비스를 제공하는 알람매니저 설정

        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"default");
        manager.createNotificationChannel(new NotificationChannel("default","기본",NotificationManager.IMPORTANCE_DEFAULT)); //채널생성(특정 SDK이상 부터는 채널 요함)

        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setColor(Color.GREEN);
        builder.setContentTitle("일정 알림");
        builder.setContentText(title);

        manager.notify(1,builder.build()); //알람
    }
}
