package org.messanger.mycalender.Database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DataHandler extends ContentProvider { //일정 DB로 사용
    private static final int SCHEDULE=1;
    private static final String PATH1="schedule";
    private static final String DATABASE="APPDB"; //데이터베이스 이름
    public static final Uri CONTENT_URI_SCHDULE=Uri.parse("content://org.messanger.mycalender/schedule"); //매칭될 URI
    private SQLiteDatabase database;
    private static final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    static{
        uriMatcher.addURI("org.messanger.mycalender",PATH1,SCHEDULE); //uri매칭 성공시, 1반환
    }

    @Override
    public boolean onCreate() {
        database=getContext().openOrCreateDatabase(DATABASE, Context.MODE_PRIVATE,null); //데이터베이스 오픈 및 생성

        //테이블 작성하기
        database.execSQL("create table if not exists "+PATH1+" ("+
                    "title text,"+
                    "wtime text,"+
                    "todo text)"
                );
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //select
        Cursor cursor=null; //조회결과를 담음
        switch(uriMatcher.match(uri)){
            case SCHEDULE:
                cursor=database.query("schedule",projection,selection,selectionArgs,null,null,sortOrder);
                break;
        }
        if(cursor!=null)
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor; //커서반환
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id=-1; //-1시 오류
        switch(uriMatcher.match(uri)){
            case SCHEDULE:
                id=database.insert("schedule",null,values);
                break;
        }
        if(id>0){
            Uri uri1= ContentUris.withAppendedId(CONTENT_URI_SCHDULE,id);
            getContext().getContentResolver().notifyChange(uri,null);
            return uri1;
        }
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count=0;
        switch(uriMatcher.match(uri)){
            case SCHEDULE:
                count=database.update("schedule",values,selection,selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
