package org.messanger.mycalender.Adaptor;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.messanger.mycalender.Data.ScheduleItem;
import org.messanger.mycalender.R;

import java.util.List;

public class ScheduleRecyclerViewAdaptor extends RecyclerView.Adapter<ScheduleRecyclerViewAdaptor.ItemHolder>{ //스케줄 리스트
    private List<ScheduleItem> list;
    private IShowContents iShowContents;
    public interface IShowContents{
        public void intentPage(String title,String wtime);
    }
    public void setList(List<ScheduleItem> list){this.list=list;}
    public void setListener(IShowContents iShowContents){
        this.iShowContents=iShowContents;
    }
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        final int pos=position;
        holder.scheduleTitle.setText(list.get(position).getTitle());
        holder.writeTime.setText((list.get(position).getTime()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //해당 뷰를 누르면, 액티비티가 대신 받아서 처리
                iShowContents.intentPage(list.get(pos).getTitle(),list.get(pos).getTime());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        protected TextView scheduleTitle;
        protected TextView writeTime;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            scheduleTitle=itemView.findViewById(R.id.scheduleTitle);
            writeTime=itemView.findViewById(R.id.write_time);
        }
    }
}
