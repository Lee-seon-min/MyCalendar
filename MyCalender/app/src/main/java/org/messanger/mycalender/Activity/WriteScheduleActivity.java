package org.messanger.mycalender.Activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.messanger.mycalender.Database.DataHandler;
import org.messanger.mycalender.Fragment.CalendarFragment;
import org.messanger.mycalender.R;

public class WriteScheduleActivity extends AppCompatActivity { //작성 액티비티
    private String thisdate;
    private EditText title_Contents,daily_Contents;
    private Button button;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_schedule);

        setID();
        setEvents();

        Intent intent=getIntent();
        thisdate=intent.getStringExtra(CalendarFragment.PICKDATE); //선택한 날짜
        //Toast.makeText(WriteScheduleActivity.this,thisdate,Toast.LENGTH_SHORT).show();
    }
    public void setID(){
        title_Contents=findViewById(R.id.title_contents);
        daily_Contents=findViewById(R.id.daily_contents);
        button=findViewById(R.id.write_savebtn);
        alertDialog=getAlertDialog();
    }
    public AlertDialog getAlertDialog(){ //저장하기 버튼 누를시, 안내문
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("일정 저장 알림");
        builder.setMessage("해당 내용을 저장하시겠습니까?");
        builder.setIcon(getResources().getDrawable(R.drawable.ic_launcher_foreground));
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return; //그냥 종료
            }
        });
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() { //데이터베이스에 저장
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContentValues values=new ContentValues();
                values.put("title",title_Contents.getText().toString());
                values.put("wtime",thisdate);
                values.put("todo",daily_Contents.getText().toString());
                Uri uri=getContentResolver().insert(DataHandler.CONTENT_URI_SCHDULE,values);

                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        return builder.create();
    }
    public void setEvents(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });
    }
}
