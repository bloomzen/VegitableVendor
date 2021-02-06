package com.example.vendor.AdminScreen;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.Adapters.HolderHotelName;
import com.example.vendor.HotelNameContsnats;
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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RemoveHotelFragment extends Fragment {


    public RemoveHotelFragment() {
        // Required empty public constructor
    }

    RecyclerView rlHotelList;

    String category;
    TextView txt_hotelCat;

    DatabaseReference dbRef, removeHotel;

    ArrayList<HotelNameContsnats> hotelLists;

    FirebaseRecyclerAdapter<HotelNameContsnats, HolderHotelName> adapters;
    FirebaseRecyclerOptions<HotelNameContsnats> options;

    AlertDialog.Builder builder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_remove_hotel, container, false);

        try {
            final Bundle bundle = getArguments();
            final String cate = bundle.getString("categoriesName");
            Toast.makeText(Vendor.getAppContext(), "catw", Toast.LENGTH_SHORT).show();
            rlHotelList = view.findViewById(R.id.hotelNameRecyclerRemove);
            rlHotelList.setLayoutManager(new LinearLayoutManager(getContext()));

            txt_hotelCat = view.findViewById(R.id.txt_AdminHotel_remove);

            txt_hotelCat.setText("" + cate + " Hotel List");

            dbRef = FirebaseDatabase.getInstance().getReference().child("HotelName").child(cate);
            removeHotel = FirebaseDatabase.getInstance().getReference().child("HotelName").child(cate);
            Query query = dbRef;
            options = new FirebaseRecyclerOptions.Builder<HotelNameContsnats>().setQuery(query, HotelNameContsnats.class).setLifecycleOwner(this).build();


            adapters = new FirebaseRecyclerAdapter<HotelNameContsnats, HolderHotelName>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final HolderHotelName holder, int position, @NonNull final HotelNameContsnats model) {
                    // Toast.makeText(getContext(), model.getHotelName(), Toast.LENGTH_SHORT).show();
                    holder.txt_HotelName.setText(""+model.getHotelName());
                    holder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Delete")
                                    .setMessage("Are you sure you want to delete the " + model.getHotelName())
                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            removeHotel.orderByChild("hotelName").equalTo(model.getHotelName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                                                        appleSnapshot.getRef().removeValue();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                            builder.show();
                        }
                    });

                }

                @NonNull
                @Override
                public HolderHotelName onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view1= LayoutInflater.from(getContext()).inflate(R.layout.hotel_list, parent, false);

                    return new HolderHotelName(view1);
                }
            };


            rlHotelList.setAdapter(adapters);
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "Remove: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapters.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapters.startListening();
    }

}
