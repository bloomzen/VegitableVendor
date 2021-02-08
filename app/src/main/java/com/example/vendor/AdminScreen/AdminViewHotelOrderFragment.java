package com.example.vendor.AdminScreen;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vendor.Adapters.HotelOrderHolder;
import com.example.vendor.Adapters.adminOrderPagerHolder;
import com.example.vendor.AdminHotelOrder;
import com.example.vendor.Constants;
import com.example.vendor.R;
import com.example.vendor.UserScreens.HomeFragment;
import com.example.vendor.Vendor;
import com.example.vendor.orderItemData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminViewHotelOrderFragment extends Fragment {


    public AdminViewHotelOrderFragment() {
        // Required empty public constructor
    }

    String hotelName, hotelID, hotelDateTime, member, status;

    TextView txt_hotelName, txt_orderNumber, txt_orderTime, txt_member, txt_total_Price;
    RecyclerView rl_item;

    FirebaseRecyclerAdapter<orderItemData, adminOrderPagerHolder> adapter;
    FirebaseRecyclerOptions<orderItemData> options;
    DatabaseReference mOrderContent, mOrder, mUserID, mShopUid;
    Button btn_completed_change;
    String key, shopUid, total;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_view_hotel_order, container, false);


        try {
            Bundle bundle = getArguments();
            hotelName = bundle.getString("hotelName", "");
            hotelID = bundle.getString("orderID", "");
            hotelDateTime = bundle.getString("orderON", "");
            member = bundle.getString("member", "");
            status = bundle.getString("status", "");
            total = bundle.getString("total","");

            txt_hotelName = view.findViewById(R.id.txt_hotelName);
            txt_orderNumber = view.findViewById(R.id.txt_orderIDNumber);
            txt_orderTime = view.findViewById(R.id.txt_orderONNumber);
            txt_total_Price = view.findViewById(R.id.txt_total_Price);
            txt_member = view.findViewById(R.id.txt_memberType);
            btn_completed_change = view.findViewById(R.id.btn_pending_complete);
            rl_item = view.findViewById(R.id.recycler_pending_items);
            rl_item.setLayoutManager(new LinearLayoutManager(getContext()));

            if(status.equalsIgnoreCase("Pending")){
                btn_completed_change.setText("Approve");
            }else if(status.equalsIgnoreCase("Approve")){
                btn_completed_change.setText("Complete");
            }else if(status.equalsIgnoreCase("Complete")){
                btn_completed_change.setVisibility(View.GONE);
            }




            subscribeToTopic();

            SharedPreferences admin =this.getActivity().getSharedPreferences("MyAdminLogin",MODE_PRIVATE);
            shopUid = admin.getString("adminId","");

            txt_hotelName.setText("" + hotelName);
            txt_orderNumber.setText("" + hotelID);
            txt_orderTime.setText("" + hotelDateTime);
            txt_member.setText("" + member);
            txt_total_Price.setText("Total: "+total);
            mOrderContent = FirebaseDatabase.getInstance().getReference().child("OrderContent").child(hotelName).child(hotelID);
            mOrder = FirebaseDatabase.getInstance().getReference().child("Order").child(hotelName).child(hotelID);
            mUserID = FirebaseDatabase.getInstance().getReference().child("User");
            mUserID.orderByChild("UserHotelName").equalTo(hotelName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            key = ds.getKey();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




            getAllOrders();

        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "On Create: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try {
            btn_completed_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (status.equals("Pending")) {
                        mOrder.child("orderStatus").setValue("Approve").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                                String message = "Order is now Approved";
                                Toast.makeText(Vendor.getAppContext(), "" + message, Toast.LENGTH_SHORT).show();

                                AdminOrdersFragment hotelOrderFragment = new AdminOrdersFragment();

                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();


                                fragmentTransaction.replace(R.id.fragment_Admin_container, hotelOrderFragment).commit();
                                prepareNotificationMessage(hotelID, message);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    } else if (status.equals("Approve")) {
                        mOrder.child("orderStatus").setValue("Complete").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                String message = "Order is now Completed";
                                Toast.makeText(Vendor.getAppContext(), "" + message, Toast.LENGTH_SHORT).show();

                                try {
                                    AdminOrdersFragment hotelOrderFragment = new AdminOrdersFragment();

                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();


                                    fragmentTransaction.replace(R.id.fragment_Admin_container, hotelOrderFragment).commit();
                                    prepareNotificationMessage(hotelID, message);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }
            });

        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "ViewHotel" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    private void subscribeToTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.FCM_TOPIC).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Vendor.getAppContext(), "Done", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Vendor.getAppContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void prepareNotificationMessage(String orderId, String message) {
        //when user place order, send notification to vendor

        String NOTIFICATION_TOPIC = "/topics/" + Constants.FCM_TOPIC;
        String NOTIFICATION_TITLE = "Your Order" + orderId;
        String NOTIFICATION_MEASSAGE = "" + message;
        String NOTIFICATION_TYPE = "OrderStatusChange";


        //prepare json
        JSONObject notificationJO = new JSONObject();
        JSONObject notificationBodyJO = new JSONObject();

        try {
            notificationBodyJO.put("notificationType", NOTIFICATION_TYPE);
            notificationBodyJO.put("buyerUid", key);
            notificationBodyJO.put("sellerUid", shopUid);
            notificationBodyJO.put("orderId", orderId);
            notificationBodyJO.put("NotificationTitle", NOTIFICATION_TITLE);
            notificationBodyJO.put("notificationMessage", NOTIFICATION_MEASSAGE);


            notificationJO.put("to", NOTIFICATION_TOPIC);
            notificationJO.put("data", notificationBodyJO);

        } catch (Exception e) {

            Log.v("exe", "" + e.getMessage());
        }

        sendFCMNotification(notificationJO);


    }

    private void sendFCMNotification(JSONObject notificationJO) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationJO, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //after sending fcm start order details activity


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "key=" + Constants.FCM_KEY);


                return headers;
            }
        };

        //enqui volly
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }

    private void getAllOrders() {


        try {
            Query query = mOrderContent;
            options = new FirebaseRecyclerOptions.Builder<orderItemData>().setQuery(query, orderItemData.class).setLifecycleOwner(this).build();
            adapter = new FirebaseRecyclerAdapter<orderItemData, adminOrderPagerHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull adminOrderPagerHolder holder, int position, @NonNull final orderItemData model) {
                    try {
                        if(model.getItemGms().equalsIgnoreCase("0")){
                            holder.txt_itemName.setText(model.getItemName()+" x "+model.getItemQuantity()+" "+ model.getItemUnit());
                        }else {
                            holder.txt_itemName.setText(model.getItemName()+" x "+model.getItemQuantity()+" "+ model.getItemUnit() + " , " + model.getItemGms() + " gms");
                        }if(model.getItemQuantity().equalsIgnoreCase("0")){
                            holder.txt_itemName.setText(model.getItemName()+" x "+model.getItemGms()+" "+ " gms");
                        }

                        holder.txt_itemPrice.setText("Rs. "+model.getItemFinalPrice());
                    }catch (Exception e){
                        Log.v("excep",""+e.getMessage());
                    }


                }

                @NonNull
                @Override
                public adminOrderPagerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.admin_order_pager_layout, parent, false);

                    return new adminOrderPagerHolder(view);
                }
            };

            rl_item.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "All Orders" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
