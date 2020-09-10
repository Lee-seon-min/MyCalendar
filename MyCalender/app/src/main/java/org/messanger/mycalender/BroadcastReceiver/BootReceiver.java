package org.messanger.mycalender.BroadcastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import org.messanger.mycalender.Fragment.AlarmFragment;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(AlarmFragment.ALARM_FOLDER_NAME, Context.MODE_PRIVATE);
            long milliTime = sharedPreferences.getLong(AlarmFragment.ALARM_FILE_TIME, -1);

            Intent intent1 = new Intent(context, NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent1, 0);
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if(milliTime!=-1){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, milliTime, pendingIntent);
                else{
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        manager.setExact(AlarmManager.RTC_WAKEUP, milliTime, pendingIntent);
                    } else {
                        manager.set(AlarmManager.RTC_WAKEUP, milliTime, pendingIntent);
                    }
                }
            }
        }
    }
}
