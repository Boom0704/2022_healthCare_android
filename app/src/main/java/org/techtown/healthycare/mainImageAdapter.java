package org.techtown.healthycare;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class mainImageAdapter extends RecyclerView.Adapter<mainImageAdapter.MyViewHolder> {

    List<mainImageModel> Data = new ArrayList<>() ;

    public mainImageAdapter() { }


    public void addSkin(mainImageModel skinModel){
        Data.add(skinModel);
    }


    // 주소값 받음
    public mainImageAdapter(List<mainImageModel> list) {
        Data = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_image, parent, false));
    }
    //mainImageModel

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // 새로 값을 셋팅, 표시될 값(View 에 새롭게 셋팅될 position 의미)
        mainImageModel currentItem = Data.get(position);
        // 홀더가 저장한 View에 값 셋팅
        holder.slideImage.setImageResource(currentItem.getResId());
        holder.slideContent.setText(currentItem.getSkinName());
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView slideImage;
        TextView slideContent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            slideContent =  itemView.findViewById(R.id.slide_content);
            slideImage = itemView.findViewById(R.id.slide_image);
        }
    }
}