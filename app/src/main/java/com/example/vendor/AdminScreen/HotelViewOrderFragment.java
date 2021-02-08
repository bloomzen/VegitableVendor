package com.example.vendor.AdminScreen;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.Adapters.CartViewHolder;
import com.example.vendor.Adapters.HotelOrderHolder;
import com.example.vendor.AdminHotelOrder;
import com.example.vendor.CartData;
import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotelViewOrderFragment extends Fragment {


    //to get hotel in admin screen
    public HotelViewOrderFragment() {
        // Required empty public constructor
    }

    SearchView searchView;
    TextView txt_hotelName, txt_member;
    ImageButton img_back_btn, img_sort_btn;

    RecyclerView recyclerHotelOrder;
    ImageView imagShop;

    FirebaseRecyclerAdapter<AdminHotelOrder, HotelOrderHolder> adapter1;
    FirebaseRecyclerOptions<AdminHotelOrder> options;
    DatabaseReference mOrder;


    String hotelName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hotel_view_order, container, false);


        try {
            SharedPreferences sh =this.getActivity().getSharedPreferences("MyHotelDetails",MODE_PRIVATE);
            hotelName = sh.getString("HotelName","");
            String member = sh.getString("memberShip","");

            mOrder = FirebaseDatabase.getInstance().getReference().child("Order");


//            txt_member = view.findViewById(R.id.txt_OrderHotelName);
//            txt_member.setText("" + member);

            imagShop = view.findViewById(R.id.img_shop);

            txt_hotelName = view.findViewById(R.id.txt_shopName);
            txt_hotelName.setText("" + hotelName);


            recyclerHotelOrder = view.findViewById(R.id.hotelRecyclerOrder);
            recyclerHotelOrder.setLayoutManager(new LinearLayoutManager(getContext()));


//            img_sort_btn = view.findViewById(R.id.btn_viewOrderHotel_sort);
//            img_sort_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//
//            img_back_btn = view.findViewById(R.id.btn_viewOrderHotel_back);
//            img_back_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    HotelListFragment hotelListFragment = new HotelListFragment();
//                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                    transaction.replace(R.id.fragment_container, hotelListFragment);
//                    transaction.commit();
//                }
//            });
//
//            searchView = (SearchView) view.findViewById(R.id.viewHotelOrder_Search);
//            ImageView icon = searchView.findViewById(androidx.appcompat.R.id.search_button);
//            icon.setColorFilter(getResources().getColor(R.color.categoriesColor));
//
//            ImageView searchClose = searchView.findViewById(R.id.search_close_btn);
//            searchClose.setColorFilter(getResources().getColor(R.color.categoriesColor));


            getAllOrders();

        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "On Create: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    private void getAllOrders() {


        try {
            Query query = mOrder.child(hotelName);
            options = new FirebaseRecyclerOptions.Builder<AdminHotelOrder>().setQuery(query, AdminHotelOrder.class).setLifecycleOwner(this).build();
            adapter1 = new FirebaseRecyclerAdapter<AdminHotelOrder, HotelOrderHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull HotelOrderHolder holder, int position, @NonNull final AdminHotelOrder model) {
                    holder.txt_hotelNumber.setText("Order ID:" + "" + model.getOrderNumber());
                    holder.txt_hotelTime.setText("Order ON:" + "" + model.getOrderDate() + "\n" + model.getOrderTime());
                   // holder.btn_status.setText("" + model.getOrderStatus());

                    holder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AdminOrderDetailsFragment hotelOrderFragment = new AdminOrderDetailsFragment();

                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                            Bundle bundle=new Bundle();
                            bundle.putString("hotelName",hotelName);
                            bundle.putString("orderID",model.getOrderNumber());
                            bundle.putString("orderOn",model.getOrderDate()  );
                            bundle.putString("member",model.getMemberType());
                            bundle.putString("orderTime",model.getOrderTime());
                            bundle.putString("totalPrice",model.getTotal_order_amount());

                            hotelOrderFragment.setArguments(bundle);

                            fragmentTransaction.replace(R.id.fragment_Admin_container, hotelOrderFragment).addToBackStack(null).commit();
                        }
                    });


                }

                @NonNull
                @Override
                public HotelOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.order_list, parent, false);

                    return new HotelOrderHolder(view);
                }
            };

            recyclerHotelOrder.setAdapter(adapter1);

        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "load Data" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.v("text",""+e.getMessage());
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            adapter1.startListening();
        }catch (Exception e){
            Log.v("daapter",""+e.getMessage());
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            adapter1.stopListening();
        }catch (Exception e){
            Log.v("StopException",""+e.getMessage());
        }

    }
}
