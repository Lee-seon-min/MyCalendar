package org.messanger.mycalender.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.messanger.mycalender.Fragment.AlarmFragment;
import org.messanger.mycalender.Fragment.CalendarFragment;
import org.messanger.mycalender.Fragment.WeatherFragment;
import org.messanger.mycalender.R;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    static private CalendarFragment calendarFragment=new CalendarFragment();
    static private WeatherFragment weatherFragment=new WeatherFragment();
    static private AlarmFragment alarmFragment=new AlarmFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setID(); //view들의 아이디를 세팅합니다.
        setEvents();//하단네비게이션의 이벤트를 설정합니다.

        fragmentManager.beginTransaction().replace(R.id.myframe,calendarFragment).commit(); //첫 페이지는 캘린더입니다.

    }
    private void setID(){
        fragmentManager=getSupportFragmentManager();
        bottomNavigationView=findViewById(R.id.myBottomnavi);
}
    private void setEvents(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){ //터치했을때,
                    case R.id.calendar: //달력이라면
                        fragmentManager.beginTransaction().replace(R.id.myframe,calendarFragment).commit();
                        break;
                    case R.id.weather: //날씨라면,
                        fragmentManager.beginTransaction().replace(R.id.myframe,weatherFragment).commit();
                        break;
                    case R.id.alarm: //알람이라면,
                        fragmentManager.beginTransaction().replace(R.id.myframe,alarmFragment).commit();
                        break;
                }
                return true;
            }
        });
    }
}
