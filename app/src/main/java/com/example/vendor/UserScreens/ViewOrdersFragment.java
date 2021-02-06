package com.example.vendor.UserScreens;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vendor.Adapters.OrderViewHolder;
import com.example.vendor.OrderData;
import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewOrdersFragment extends Fragment {


    //view orders for user side

    public ViewOrdersFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerViewOrders;

    private FirebaseRecyclerAdapter<OrderData, OrderViewHolder> adapter;
    FirebaseRecyclerOptions<OrderData> options;
    DatabaseReference mRef;

    String userId, userType;

    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_orders, container, false);

        try {
            recyclerViewOrders = view.findViewById(R.id.recyclerOrder);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);
            recyclerViewOrders.setLayoutManager(layoutManager);

            mAuth = FirebaseAuth.getInstance();

            SharedPreferences sh = this.getActivity().getSharedPreferences("MyLogin", MODE_PRIVATE);
            userId = sh.getString("userId", "");
            userType = sh.getString("userType", "");

            if (mAuth.getUid() != null) {
                mRef = FirebaseDatabase.getInstance().getReference().child("myOrder").child(FirebaseAuth.getInstance().getUid());
            } else {
                mRef = FirebaseDatabase.getInstance().getReference().child("myOrder").child(userId);
            }


            loadAllOrders();
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "On Create: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }



        return view;
    }

    public void loadAllOrders() {
        try {


            Query query = mRef;


            options = new FirebaseRecyclerOptions.Builder<OrderData>().setQuery(query, OrderData.class).setLifecycleOwner(this).build();
            adapter = new FirebaseRecyclerAdapter<OrderData, OrderViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final OrderViewHolder holder, int position, @NonNull final OrderData model) {


                    holder.setOrderDate(model.getOrderDate(), model.getOrderTime());
                    holder.setOrderNumber(model.getOrderNumber());


                    holder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myOrderList ldf = new myOrderList();

                            FragmentTransaction transection = getFragmentManager().beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString("orderNumber", model.getOrderNumber());
                            bundle.putString("hotelName",model.getHotelName());
                            bundle.putString("memberType",model.getMemberType());
                            bundle.putString("totalAmount",model.getTotal_order_amount());

                            ldf.setArguments(bundle);
                            transection.replace(R.id.fragment_container, ldf);
                            transection.commit();
                        }
                    });


                }

                @NonNull
                @Override
                public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.order_layout, parent, false);

                    return new OrderViewHolder(view);
                }
            };

            recyclerViewOrders.setAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "View Orders: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
