package org.messanger.mycalender.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.messanger.mycalender.Adaptor.PickScheduleRecyclerViewAdaptor;
import org.messanger.mycalender.Data.ScheduleItem;
import org.messanger.mycalender.Database.DataHandler;
import org.messanger.mycalender.R;

import java.util.ArrayList;
import java.util.List;

public class AllScheduleListActivity extends AppCompatActivity implements PickScheduleRecyclerViewAdaptor.IPickScheduleItem{ //알람설정할 일정을 불러올 액티비티
    private List<ScheduleItem> list=new ArrayList<>(); //일정을 담을 리스트
    private PickScheduleRecyclerViewAdaptor adaptor=new PickScheduleRecyclerViewAdaptor();
    private RecyclerView recyclerView;
    static public String TARGET_TITLE="targettitle";
    static public String TARGET_TIME="targettime";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_schedule_list);
        setID();

        //모든 일정 불러오는 코드 작성 (list에 저장){....}
        try{
            Cursor cursor=getContentResolver().query(DataHandler.CONTENT_URI_SCHDULE,new String[]{"title","wtime"},null,null,null);
            if(cursor!=null){
                while(cursor.moveToNext()) {
                    ScheduleItem item = new ScheduleItem();
                    item.setTitle(cursor.getString(0));
                    item.setTime(cursor.getString(1));
                    list.add(item);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        RecyclerView.LayoutManager manager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(manager);
        adaptor.setList(list);
        adaptor.setListener(this);
        recyclerView.setAdapter(adaptor);
        adaptor.notifyDataSetChanged();
    }
    public void setID(){
        recyclerView=findViewById(R.id.allchedulelist);
    }

    @Override
    public void callBack(String title, String time) { //클릭한 타이틀과 시간
        Intent intent=new Intent();
        intent.putExtra(TARGET_TITLE,title);
        intent.putExtra(TARGET_TIME,time);
        setResult(RESULT_OK,intent);
        finish();
    }
}
