package org.messanger.mycalender.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.messanger.mycalender.Database.DataHandler;
import org.messanger.mycalender.R;
public class ScheduleContentsViewActivity extends AppCompatActivity { //저장된 일정을 불러오는 곳
    public class NoDataSQLException extends Exception{
        public NoDataSQLException(String message){
            super(message);
        }
    }
    private EditText title, contents;
    private Button fixbtn;
    private String thisTime,thisTitle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_contents_view);
        setID();
        setEvents();

        Intent intent=getIntent();
        thisTitle=intent.getStringExtra(ScheduleActivity.TITLE); //시간
        thisTime=intent.getStringExtra(ScheduleActivity.TIME); //제목
        try{ //데이터 불러오기
            Cursor cursor=getContentResolver().query(DataHandler.CONTENT_URI_SCHDULE,new String[]{"title, todo"},"title = ? and wtime = ?",new String[]{thisTitle,thisTime},null); //제목과 시간으로 해당 콘텐츠를 불러옴
            if(cursor==null){ //가져올 데이터가 없다면,
                throw new NoDataSQLException("데이터 무결성 오류");
            }
            while(cursor.moveToNext()){
                title.setText(cursor.getString(0));
                contents.setText(cursor.getString(1));
            }
        }
        catch(NoDataSQLException e){
            Log.d("NoDataSQLException......",e.getMessage());
        }

    }
    public void setID(){
        title=findViewById(R.id.fix_title);
        contents=findViewById(R.id.fix_daily_contents);
        fixbtn=findViewById(R.id.fix_contentsbtn);
    }
    public void setEvents(){
        fixbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //수정버튼
                ContentValues values=new ContentValues();
                values.put("title",title.getText().toString());
                values.put("todo",contents.getText().toString());
                int cnt = getContentResolver().update(DataHandler.CONTENT_URI_SCHDULE,values,"title = ? and wtime = ?",new String[]{thisTitle,thisTime});

                if(cnt!=0)
                    Toast.makeText(ScheduleContentsViewActivity.this,"수정이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ScheduleContentsViewActivity.this,"일시적인 오류가 발생하였습니다.",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
