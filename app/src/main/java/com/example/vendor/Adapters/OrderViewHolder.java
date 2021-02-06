package com.example.vendor.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendor.R;

import org.w3c.dom.Text;

public class OrderViewHolder extends RecyclerView.ViewHolder {


    //to display orders
  public   View view;
   private TextView orderNumber;
   private TextView orderDate;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        view = itemView;

        orderNumber = view.findViewById(R.id.txt_orderNumber);
        orderDate = view.findViewById(R.id.txt_orderDate);


    }



    public void setOrderNumber(String orderNumber1){
        orderNumber.setText("Order ID: " + orderNumber1);
    }

    public void setOrderDate(String orderDate1,  String orderTime1){
        orderDate.setText("Order ON:  "+ orderDate1 +"\n"+orderTime1);
    }
}
