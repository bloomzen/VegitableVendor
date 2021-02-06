package com.example.vendor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendor.LogData;
import com.example.vendor.R;

import java.util.ArrayList;

public class AdapterLogData extends RecyclerView.Adapter<AdapterLogData.LogHolder> {

    Context context;
    ArrayList<LogData> logData;

    public AdapterLogData(Context context, ArrayList<LogData> logData) {
        this.context = context;
        this.logData = logData;
    }

    @NonNull
    @Override
    public LogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.log_layout,parent,false);

        return new LogHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogHolder holder, int position) {

        LogData logs = logData.get(position);
        holder.txt_date.setText(logs.getDate()+"  :  "+ "Rs " + logs.getAmount());


    }

    @Override
    public int getItemCount() {
        return logData.size();
    }

    public class LogHolder extends RecyclerView.ViewHolder {

        public  View view;
        public TextView txt_date;
        public LogHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            txt_date = view.findViewById(R.id.txt_date_payed);




        }
    }
}
