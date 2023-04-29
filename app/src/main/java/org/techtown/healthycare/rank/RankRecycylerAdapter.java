package org.techtown.healthycare.rank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.techtown.healthycare.R;
import org.techtown.healthycare.UserAccount;

import java.util.ArrayList;
import java.util.List;

public class RankRecycylerAdapter extends RecyclerView.Adapter<RankRecycylerAdapter.ItemViewHolder> {
    int i = 0;
    List<UserAccount> data = new ArrayList<>();

    public RankRecycylerAdapter(ArrayList<UserAccount> userList, Context context)
    {
        this.data = userList;

    }


    public void addItem(UserAccount item){
        data.add(item);
    }
    public void setItem(ArrayList<UserAccount> data){
     this.data = data;
    }

    public UserAccount getItem(int position){
      return data.get(position);
    }
    public void setItem(int position, UserAccount chicken){
    data.set(position,chicken);
    }

    @Override
    //각각의 item들을 위한 Layout을 inflation ->Itemview를 viewHolder에 넣음
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemview = inflater.inflate(R.layout.rank_item, parent, false);

        return new ItemViewHolder(itemview);
        }

        @Override

    // 이미 만들어진 view 에 데이터값(텍스트, 이미지, 인덱스)만 재 셋팅되도록 특정 시점에 호출됨.
    // position번째 data 값 이미 만들어진 view에재 셋팅
        public void onBindViewHolder(RankRecycylerAdapter.ItemViewHolder holder, int position) {

        holder.onBind(data.get(position));
        }

        @Override
        public int getItemCount() {
        return data.size();
        }


        class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView rankText;
        private TextView idText;
        private TextView calorieText;
        private androidx.cardview.widget.CardView cardView;


        //하나의 item을 위한 ViewHolder 생성
        public ItemViewHolder(View itemView) {
            super(itemView);

            rankText = itemView.findViewById(R.id.rankText);
            idText = itemView.findViewById(R.id.idText);
            calorieText = itemView.findViewById(R.id.calorieText);
            cardView =itemView.findViewById(R.id.overallRanking);
        }

        //ViewHold 방식
        void onBind(UserAccount item){
            rankText.setText(String.valueOf(i+1));
            idText.setText(item.getUserEmail());
            calorieText.setText(String.valueOf(item.getCalorie()));
            i++;

            if(item.getCalorie()==0){
                cardView.setVisibility(View.GONE);
            }
        }

    }
}