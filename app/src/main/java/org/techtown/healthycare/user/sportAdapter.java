package org.techtown.healthycare.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import org.techtown.healthycare.Sport;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.healthycare.R;

import java.util.ArrayList;

public class sportAdapter extends RecyclerView.Adapter<sportAdapter.sportViewHolder> {
    private ArrayList<Sport> sportList;
    private Context context;

    // interface - 클릭인터페이스 커스톰
    private onItemListener  mListener;


    // 인터페이스 객체를 초기화 시키는 메서드
    public void setOnClickListener(onItemListener  listener){
        mListener =listener;
    }


    private int mCheckedPosition = -1;




    public sportAdapter(ArrayList<Sport> list, Context context) {
        this.sportList =list;
        this.context = context;
    }

    public void addSport(Sport sport){
        sportList.add(sport);
    }

    public void setItem(int position, Sport sport){
        sportList.set(position,sport);
    }


    @NonNull
    @Override
    public sportAdapter.sportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.sport_item,parent, false);

        sportViewHolder holder = new sportViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull sportAdapter.sportViewHolder holder, int position) {
        final Sport item = sportList.get(position);

        final int color;
        if (holder.getAbsoluteAdapterPosition() == mCheckedPosition) {
            color = ContextCompat.getColor(holder.itemView.getContext(), R.color.purple_700);
        } else {
            color = ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent);

        }
        holder.itemView.setBackgroundColor(color);



        // 바인딩할  sportList안에 저장된 sport 객체 가져옴
      //  holder.onBind(sportList.get(position));

        Sport currentItem = sportList.get(position);
        holder.sportName.setText(sportList.get(position).getSportName());
        String str_cal = Integer.toString(sportList.get(position).getCalorie());
        String str_time = Integer.toString(sportList.get(position).getRunTime());
        holder.sportDate.setText(sportList.get(position).getEarningPoint());
        holder.sportContent.setText("-운동시 분당 소비 칼로리 : "+str_cal
                +"\n-현재 누적 운동시간 (초) : "+str_time);
        //holder.sportContent.setText(sportList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return sportList != null ? sportList.size() : 0;
    }

    public class sportViewHolder extends RecyclerView.ViewHolder {

        TextView sportName;
        TextView sportContent;TextView sportNum;
        TextView sportDate;



        public sportViewHolder(@NonNull View itemView) {
            super(itemView);
            this.sportName = itemView.findViewById(R.id.sportName);
            this.sportContent = itemView.findViewById(R.id.sportContent);
            this.sportDate = itemView.findViewById(R.id.sportDate);
            Button button = itemView.findViewById(R.id.btnDelte);




            if(mListener != null){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override

                    // 호출시점 : 화면에 배치된 view 을 클릭할 경우
                    public void onClick(View view) {
                        int position = getAbsoluteAdapterPosition();
                            mListener.onItemClicked(itemView, position);
                            //mListener.onItemClicked(item);

                        }

                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAbsoluteAdapterPosition();
                        mListener.onDeleteClick(v, position);


                    }
                });


            }

        }

    }

    public interface onItemListener {
        void onItemClicked(View view, int position); // 클릭
        void onDeleteClick(View v, int positon); //삭제
    }

}
