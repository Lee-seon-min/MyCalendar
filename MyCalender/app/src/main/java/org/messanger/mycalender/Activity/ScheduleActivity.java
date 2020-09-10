package org.messanger.mycalender.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.messanger.mycalender.Adaptor.ScheduleRecyclerViewAdaptor;
import org.messanger.mycalender.Data.ScheduleItem;
import org.messanger.mycalender.Database.DataHandler;
import org.messanger.mycalender.Fragment.CalendarFragment;
import org.messanger.mycalender.R;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity implements ScheduleRecyclerViewAdaptor.IShowContents {
    private Button writebtn;
    private RecyclerView recyclerView;
    private List<ScheduleItem> list=new ArrayList<>(); //데이터베이스에서 가져온 데이터로 리스트화
    private ScheduleRecyclerViewAdaptor adaptor=new ScheduleRecyclerViewAdaptor();
    private String thisdate;
    private int REQUEST_WRITE=101;
    public static String TITLE="title";
    public static String TIME="time";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        setID();
        setEvents();

        Intent intent=getIntent();
        thisdate=intent.getStringExtra(CalendarFragment.PICKDATE); //캘린더에서 고른 날짜 데이터

        //데이터베이스에서 데이터 가져와서 리스트에 담기
        Cursor cursor=getContentResolver().query(DataHandler.CONTENT_URI_SCHDULE,new String[]{"title","wtime"},"wtime = ?",new String[]{thisdate},null); //가져온 날짜 데이터를 활용하여, 해당 URI와 매칭하며 해당하는 테이블에서 조작한다.
        if(cursor!=null)
            while(cursor.moveToNext()){ //한 행씩 받아옴
                String title=cursor.getString(0); //0번째 컬럼
                String time=cursor.getString(1); //1번째 컬럼
                ScheduleItem item=new ScheduleItem(); //객체생성
                item.setTime(time);
                item.setTitle(title);
                list.add(item); //리스트에 담기
            }
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this); //리사이클러뷰의 스타일은 Linear로
        recyclerView.setLayoutManager(layoutManager); //레이아웃세팅
        adaptor.setList(list); //리스트 세팅
        adaptor.setListener(this); //인터페이스객체로 설정
        recyclerView.setAdapter(adaptor); //어뎁터 세팅
        adaptor.notifyDataSetChanged();
    }
    public void setID() {
        writebtn = findViewById(R.id.addSchedule);
        recyclerView = findViewById(R.id.scheduleList);
    }
    public void setEvents() {
        writebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //일정추가버튼
                Intent intent=new Intent(ScheduleActivity.this,WriteScheduleActivity.class);
                intent.putExtra(CalendarFragment.PICKDATE,thisdate);
                startActivityForResult(intent,REQUEST_WRITE); //작성을 요청
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_WRITE && resultCode==RESULT_OK){ //작성이 되었으므로, 다시 리스트를 갱신해야한다.

            list.clear();

            Cursor cursor=getContentResolver().query(DataHandler.CONTENT_URI_SCHDULE,new String[]{"title","wtime"},"wtime = ?",new String[]{thisdate},null); //해당 URI와 매칭하며, 해당하는 테이블에서 조작한다.
            while(cursor.moveToNext()){ //한 행씩 받아옴
                String title=cursor.getString(0); //0번째 컬럼
                String time=cursor.getString(1); //1번째 컬럼
                ScheduleItem item=new ScheduleItem(); //객체생성
                item.setTime(time);
                item.setTitle(title);
                list.add(item); //리스트에 담기
            }
            adaptor.setList(list);
            adaptor.notifyDataSetChanged(); //갱신
        }
    }

    @Override
    public void intentPage(String title, String wtime) { //일정 불러오기(리사이클러뷰 어뎁터의 메소드 오버라이딩)
        Intent intent=new Intent(ScheduleActivity.this,ScheduleContentsViewActivity.class);
        intent.putExtra(TITLE,title);
        intent.putExtra(TIME,wtime);
        startActivity(intent);
    }
}
