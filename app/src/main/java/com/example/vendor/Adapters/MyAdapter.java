package com.example.vendor.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.vendor.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class MyAdapter extends SliderViewAdapter<MyAdapter.MyViewHolder> {


    List<Integer> list;

    public MyAdapter(List<Integer> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
viewHolder.imageView.setImageResource(list.get(position));

    }

    @Override
    public int getCount() {
        return list.size();
    }

    class MyViewHolder extends SliderViewAdapter.ViewHolder{

        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }




}
