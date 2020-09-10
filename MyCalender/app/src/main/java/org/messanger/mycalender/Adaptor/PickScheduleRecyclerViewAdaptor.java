package org.messanger.mycalender.Adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.messanger.mycalender.Data.ScheduleItem;
import org.messanger.mycalender.R;

import java.util.List;

public class PickScheduleRecyclerViewAdaptor extends RecyclerView.Adapter<PickScheduleRecyclerViewAdaptor.ItemHolder>{//알람설정을 위한 일정을 나열할 리사이클러뷰 어뎁터
    private List<ScheduleItem> list;
    private IPickScheduleItem listener;
    public interface IPickScheduleItem{
        void callBack(String title,String time); //스케줄 아이템 클릭시, 해당 날짜와 제목을 던져줌
    }
    public void setList(List<ScheduleItem> list){this.list=list;}
    public void setListener(IPickScheduleItem listener){this.listener=listener;}
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        //바인딩
        final String tt= list.get(position).getTitle(); //title
        final String wt=list.get(position).getTime(); //wtime
        holder.time.setText(wt);
        holder.title.setText(tt);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.callBack(tt,wt);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder{
        private TextView title,time;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.scheduleTitle);
            time=itemView.findViewById(R.id.write_time);
        }
    }
}
