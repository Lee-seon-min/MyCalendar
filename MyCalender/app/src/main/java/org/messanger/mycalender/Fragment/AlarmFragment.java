package org.messanger.mycalender.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.messanger.mycalender.Activity.AllScheduleListActivity;
import org.messanger.mycalender.BroadcastReceiver.BootReceiver;
import org.messanger.mycalender.BroadcastReceiver.NotificationReceiver;
import org.messanger.mycalender.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class AlarmFragment extends Fragment {
    private TimePicker picker;
    private Button save,call;
    private EditText target_title;
    private int REQUEST_CODE=102;
    private String title=null,time=null;
    private boolean Flag=false;
    public static String ALARM_FOLDER_NAME="alarmfoldername";
    public static String ALARM_FILE_TITLE="alarmfiletitle";
    public static String ALARM_FILE_TIME="alarmfiletime";
    public static AlarmManager alarmManager=null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.alarm_fragment,container,false);
        setID(v);
        setEvents();

        SharedPreferences sharedPreferences=getContext().getSharedPreferences(ALARM_FOLDER_NAME,Context.MODE_PRIVATE);
        title=sharedPreferences.getString(ALARM_FILE_TITLE,null);
        long milliTime=sharedPreferences.getLong(ALARM_FILE_TIME,-1);
        if(title!=null && milliTime!=-1) { //알람이 지정되있으므로,
            target_title.setText(title);
            Calendar alarmTime=new GregorianCalendar();
            alarmTime.setTimeInMillis(milliTime); //가져온 날짜를 milli단위로 저장

            Date date=alarmTime.getTime(); //특정 포맷으로 지정하기 위해 선언
            SimpleDateFormat HourFormat = new SimpleDateFormat("kk", Locale.getDefault());
            SimpleDateFormat MinuteFormat = new SimpleDateFormat("mm", Locale.getDefault());

            picker.setHour(Integer.parseInt(HourFormat.format(date)));
            picker.setMinute(Integer.parseInt(MinuteFormat.format(date)));
        }
        else{
            Toast.makeText(getContext(), "아직 지정된 알람이 없습니다.", Toast.LENGTH_SHORT).show();
        }


        return v;
    }
    public void setID(View v){
        picker=v.findViewById(R.id.time_picker);
        save=v.findViewById(R.id.saveAlarm);
        call=v.findViewById(R.id.call_schedule);
        target_title=v.findViewById(R.id.target_title);
    }
    public void setEvents(){
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AllScheduleListActivity.class); //일정을 불러온다.
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
        save.setOnClickListener(new View.OnClickListener() { //저장하기
            @Override
            public void onClick(View v) {
                if(!Flag){ //아직 일정을 가져오지 못했다면,
                    Toast.makeText(getContext(), "새로운 일정을 불러오지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                else{ //시간과 타이틀을 Shared에 저장하고,알람을 설정한다.
                    Calendar calendar=designateTimeAndScheduleAlarm();
                    if(calendar==null){
                        return;
                    }
                    settingNotification(calendar);
                    //알람 지정 코드 작성 요망

                    Toast.makeText(getContext(), "알람이 지정되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { //시간과 타이틀을 가져올것
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK){
            title=data.getStringExtra(AllScheduleListActivity.TARGET_TITLE); //선택한 일정의 제목
            time=data.getStringExtra(AllScheduleListActivity.TARGET_TIME); //선택한 일정의 날짜
            target_title.setText(title);
            Flag=true;
        }
    }
    public Calendar designateTimeAndScheduleAlarm(){
        String[] alarm_time=time.split("-"); //year,month,date
        int hour,minute;

        hour=picker.getHour(); //timePicker의 시
        minute=picker.getMinute(); //분

        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis()); //우선, 현재시간을 지정

        //알람 울릴 시간을 지정한다.
        calendar.set(Calendar.YEAR,Integer.parseInt(alarm_time[0]));
        calendar.set(Calendar.MONTH,Integer.parseInt(alarm_time[1])-1);//1을 빼줘야한다. (9월은 8로 표현하기 때문)
        calendar.set(Calendar.DATE,Integer.parseInt(alarm_time[2]));
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);

        Log.d("QQQQQQQQQQQQQQQQQQQ",alarm_time[0]+"-"+alarm_time[1]+"-"+alarm_time[2]);

        if(calendar.before(Calendar.getInstance())){ //현재시간보다 전시간의 일정이라면,
            Toast.makeText(getContext(), "이미 지난 일정입니다.", Toast.LENGTH_SHORT).show();
            return null;
        }

        SharedPreferences.Editor editor= getContext().getSharedPreferences(ALARM_FOLDER_NAME,Context.MODE_PRIVATE).edit();
        editor.putLong(ALARM_FILE_TIME,calendar.getTimeInMillis()); //시간 저장
        editor.putString(ALARM_FILE_TITLE,title); //타이틀 저장
        editor.apply();

        return calendar;
    }
    public void settingNotification(Calendar calendar){
        ComponentName receiver = new ComponentName(getContext(), BootReceiver.class); //부팅될때의 브로드캐스트 리시버

        Intent intent=new Intent(getContext(), NotificationReceiver.class);//해당 브로드캐스트 리시버로의 인텐트 (알람울리는 리시버)
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getContext(),0,intent,0);

        alarmManager=(AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE); //특정 리시버를 특정 시간에 작동하게 하는 알람매니저

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }
}
