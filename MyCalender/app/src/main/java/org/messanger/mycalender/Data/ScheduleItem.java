package org.messanger.mycalender.Data;

public class ScheduleItem { //스케줄의 리스트를 저장할 객체
    private String title;
    private String time;
    public String getTitle(){ return title; }
    public String getTime(){ return time; }
    public void setTitle(String title){
        this.title=title;
    }
    public void setTime(String time){
        this.time=time;
    }
}
