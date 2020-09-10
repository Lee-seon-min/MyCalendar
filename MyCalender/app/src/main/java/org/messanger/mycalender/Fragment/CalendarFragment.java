package org.messanger.mycalender.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.messanger.mycalender.Activity.ScheduleActivity;
import org.messanger.mycalender.R;

import java.util.Calendar;

public class CalendarFragment extends Fragment { //일정
    private CalendarView calendarView;
    private Button button;
    private String thisdate;
    public static String PICKDATE="pickdate";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.calendar_fragment,container,false);
        setID(v);
        setEvents();

        Calendar curDate=Calendar.getInstance();
        curDate.setTimeInMillis(calendarView.getDate()); //캘린더 뷰에 픽되어있는 날짜를 가져온다.
        thisdate=curDate.get(Calendar.YEAR)+"-"+(curDate.get(Calendar.MONTH)+1)+"-"+curDate.get(Calendar.DATE); //가져온 날짜를 알맞은 포맷으로 변경한다.(2020-9-2)

        return v;
    }
    public void setID(View v){
        calendarView=v.findViewById(R.id.mycalendar);
        button=v.findViewById(R.id.editbutton);
    }
    public void setEvents() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                thisdate = year + "-" + (month+1) + "-" + dayOfMonth; //선택한 날짜로 날짜 저장
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ScheduleActivity.class);
                intent.putExtra(PICKDATE,thisdate);
                startActivity(intent);
            }
        });
    }
}
