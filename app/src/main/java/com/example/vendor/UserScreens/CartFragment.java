package com.example.vendor.UserScreens;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vendor.Adapters.AdapterCartData;
import com.example.vendor.Adapters.AdapterCartDataGuest;
import com.example.vendor.Adapters.AdapterCartSummary;

import com.example.vendor.Adapters.CartViewHolder;
import com.example.vendor.CartData;
import com.example.vendor.Constants;
import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {


    public CartFragment() {
        // Required empty public constructor
    }


    RecyclerView recyclerCart, recyclerSummary;

    FirebaseRecyclerAdapter<CartData, CartViewHolder> adapter;
    FirebaseRecyclerOptions<CartData> options;


//    FirebaseRecyclerAdapter<CartData, CartViewGuestHolder> adapterGuest;
//    FirebaseRecyclerAdapter<CartData, > adapterSummary;


    FirebaseAuth mAuth;
    TextView btn_checkout;

    ProgressDialog mProg;

    String name, phone, HotelName, memberType;


    int currentOrderNumber;

    ArrayList<CartData> cartDataArrayList;
    AdapterCartData adapterCartData;

    ArrayList<CartData> cartDataArrayListGuest;
    AdapterCartDataGuest adapterCartDataGuest;


    ArrayList<CartData> cartSummary;
    AdapterCartSummary adapterCartSummary;

    String currentDate;
    String currentTime;
    int count = 0;

    TextView txt_total;
    String key, today;

    DatabaseReference mO;

    Double finalItemQuantity;


    DatabaseReference mRef, mUser, mOrder, mMyOrder, orderContet, TotalProduct, mPaymentHotelList;
    String userId, userType;


    Context context;
    Activity activity;

    AlertDialog.Builder builder;


    HashMap<String, Double> totalSummary;
    HashMap<String, Object> mData;

    ChildEventListener childEventListener;
    ValueEventListener valueEventListener, valueEventListener1, valueEventListener2, valueEventListener3, valueEventListener4;


    cartListener cartListener1;
    DatabaseReference mHotelRef;

    double TotalPrice = 0;

    String totalStringQuanity;

    String currentPayment;
    DatabaseReference mHotelList;


    boolean isProcess = false;

    String TotalOrderNumber = "";
    int TotalOrderNumberInNumber = 0;

    DatabaseReference mShopUid;
    String shopUid;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context != null && context instanceof cartListener) {
            cartListener1 = (cartListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_cart, container, false);

        try {
            SharedPreferences sh = this.getActivity().getSharedPreferences("MyLogin", MODE_PRIVATE);
            userId = sh.getString("userId", "");
            userType = sh.getString("userType", "");

            Toast.makeText(Vendor.getAppContext(), "" + userId, Toast.LENGTH_SHORT).show();

            //linking recycler view

            txt_total = view.findViewById(R.id.guestTotalPrice);
            recyclerCart = view.findViewById(R.id.recyclerCart);
            recyclerCart.setLayoutManager(new LinearLayoutManager(getContext()));

            recyclerSummary = view.findViewById(R.id.recyclerCartItemSummary);
            recyclerSummary.setLayoutManager(new LinearLayoutManager(getContext()));


            //Authentication instance
            mAuth = FirebaseAuth.getInstance();

            totalSummary = new HashMap<>();
            mData = new HashMap<>();


            mShopUid = FirebaseDatabase.getInstance().getReference().child("Admin");
            mShopUid.orderByChild("UserType").equalTo("Admin").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            shopUid = ds.getKey();
                            Log.v("shop", shopUid);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            //Database Reference to the cartListener node
            mRef = FirebaseDatabase.getInstance().getReference().child("cartListener").child(userId);

            //Database Reference to the User Details node node
            mUser = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

            //Database Reference to the myOrder node
            mMyOrder = FirebaseDatabase.getInstance().getReference().child("myOrder").child(userId);


            mHotelRef = FirebaseDatabase.getInstance().getReference().child("HotelName").child("Guest");

            //Database Reference to the order details node
            orderContet = FirebaseDatabase.getInstance().getReference().child("OrderContent");

            //Database Reference to the order node used by admin
            mOrder = FirebaseDatabase.getInstance().getReference().child("Order");


            mO = FirebaseDatabase.getInstance().getReference().child("TotalProducts");

            mHotelList = FirebaseDatabase.getInstance().getReference().child("HotelWiseList");


            mPaymentHotelList = FirebaseDatabase.getInstance().getReference().child("Payment");

            btn_checkout = view.findViewById(R.id.btn_checkout);

            mProg = new ProgressDialog(getContext());
            mProg.setCanceledOnTouchOutside(false);
            mProg.setTitle("please wait");


            loadAllProducts();
            loadItemSummary();


            subscribeToTopic();


            //get the user details from the user node

            mUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    try {
                        //get data from of user
                        name = snapshot.child("UserName").getValue().toString();

                        phone = snapshot.child("UserPhone").getValue().toString();
                        HotelName = snapshot.child("UserHotelName").getValue().toString();

                        mOrder.child(HotelName).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                //get the total number of order, and increment to 1.
                                count = (int) snapshot.getChildrenCount();
                                Log.v("count", Integer.toString(count));

                                currentOrderNumber = count + 1;
                                Log.v("currentOrder", Integer.toString(currentOrderNumber));


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        Toast.makeText(Vendor.getAppContext(), "" + name, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(Vendor.getAppContext(), "Cart: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                    try {
                        memberType = snapshot.child("memberType").getValue().toString();
                    } catch (Exception e) {
                        Log.v("exception", e.getMessage());
                    }


//                    valueEventListener4 = new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if(snapshot.hasChildren()){
//                                currentPayment = snapshot.child("payment").getValue().toString();
//                                Log.v("payment",""+currentPayment);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    };
//
//                    mPaymentHotelList.child(HotelName).addValueEventListener(valueEventListener4);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMM.dd");
            String todayDate = sdf.format(new Date());
            String today = todayDate.replace(".", "");


            btn_checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkOut();

                }
            });
        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "On Create View " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    private void checkOut() {
//        mProg.setMessage("Placing Order.....");
//        mProg.show();


        //get the product from the cart node

        try {
            if (userType.equalsIgnoreCase("Gold") || userType.equalsIgnoreCase("Silver")
                    || userType.equalsIgnoreCase("Bronze") || userType.equalsIgnoreCase("Fix")) {
                if (cartDataArrayList.size() == 0) {
                    Toast.makeText(Vendor.getAppContext(), "no data", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (cartListener1 != null) {
                    cartListener1.navListener("cart");
                }


                //getting the current date and time
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMM.dd");
                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aa");
                currentDate = sdf.format(new Date());
                currentTime = sdf1.format(new Date());
                today = currentDate.replace(".", "");

                String itemGm = "";

                HashMap<String, Object> hashMap = new HashMap<>();

                for (int i = 0; i < cartDataArrayList.size(); i++) {
                    final String itemName = cartDataArrayList.get(i).getItem_Name();
                    final String itemQuantity = cartDataArrayList.get(i).getItem_Quantity();
                    String itemPrice = cartDataArrayList.get(i).getItem_Price();
                    String itemFinalCost = cartDataArrayList.get(i).getItem_Final_Cost();
                    final String itemUnit = cartDataArrayList.get(i).getItem_Unit();
                    String imageItem = cartDataArrayList.get(i).getItem_Image();

                    itemGm = cartDataArrayList.get(i).getItem_Gms();


                    hashMap.put("itemName", itemName);
                    hashMap.put("itemQuantity", itemQuantity);
                    hashMap.put("itemUnit", itemUnit);
                    hashMap.put("itemFinalPrice", itemFinalCost);
                    hashMap.put("itemGms", itemGm);
                    hashMap.put("itemImage", imageItem);


                    orderContet.child(HotelName).child(Integer.toString(currentOrderNumber)).child(itemName).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });


                }

                EasyDB easyDB = EasyDB.init(getContext(), "ITEM_DB")
                        .setTableName("ITEMS_TABLE")
                        .addColumn(new Column("Item_Name", new String[]{"text", "unique"}))
                        .addColumn(new Column("Item_Price", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Final_Cost", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Quantity", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Unit", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Image", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Gms", new String[]{"text", "not null"}))
                        .doneTableColumn();

                Toast.makeText(Vendor.getAppContext(), "Order Placed", Toast.LENGTH_SHORT).show();
                easyDB.deleteAllDataFromTable();
                cartDataArrayList.clear();
                cartSummary.clear();

                adapterCartData.notifyDataSetChanged();
                adapterCartSummary.notifyDataSetChanged();


                // storing the currnet date and time and order number for myOrder node
                Map myOrder = new HashMap<>();
                myOrder.put("orderDate", currentDate);
                myOrder.put("orderTime", currentTime);
                myOrder.put("orderNumber", Integer.toString(currentOrderNumber));
                myOrder.put("orderStatus", "Pending");
                myOrder.put("total_order_amount", String.valueOf(TotalPrice));
                myOrder.put("memberType", userType);
                myOrder.put("hotelName", HotelName);


                //updating myOrder

                mMyOrder.child(Integer.toString(currentOrderNumber)).updateChildren(myOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        HashMap<String, String> userDetails = new HashMap<>();
                        userDetails.put("Name", name);
                        userDetails.put("phone", phone);
                        userDetails.put("HotelName", HotelName);
                        userDetails.put("orderNumber", Integer.toString(currentOrderNumber));
                        userDetails.put("orderDate", currentDate);
                        userDetails.put("orderTime", currentTime);
                        userDetails.put("orderStatus", "Pending");
                        userDetails.put("total_order_amount", String.valueOf(TotalPrice));
                        userDetails.put("memberType", userType);


                        mOrder.child(HotelName).child(Integer.toString(currentOrderNumber)).setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                valueEventListener3 = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists() && !isProcess) {

                                            try {
                                                isProcess = true;
                                                TotalOrderNumber = snapshot.child(HotelName).child("OrderNumber").getValue().toString();
                                                currentPayment = snapshot.child(HotelName).child("payment").getValue().toString();
                                                TotalOrderNumberInNumber = Integer.parseInt(TotalOrderNumber) + 1;
                                                Double totalPrice = Double.parseDouble(currentPayment) + TotalPrice;
                                                mPaymentHotelList.child(HotelName).child("payment").setValue(String.valueOf(totalPrice));
                                                mPaymentHotelList.child(HotelName).child("OrderNumber").setValue(String.valueOf(TotalOrderNumberInNumber));
                                                Toast.makeText(Vendor.getAppContext(), "" + key, Toast.LENGTH_SHORT).show();

                                                Log.v("totalPrice", "" + totalPrice.toString());

                                                FragmentManager fragmentManager = getFragmentManager();
                                                FragmentTransaction transaction = null;
                                                if (fragmentManager != null) {
                                                    transaction = fragmentManager.beginTransaction();
                                                    transaction.setReorderingAllowed(true);

                                                    // Replace whatever is in the fragment_container view with this fragment
                                                    transaction.replace(R.id.fragment_container, new HomeFragment(), null);

                                                    // Commit the transaction
                                                    transaction.commit();

                                                }

                                                prepareNotificationMessage(Integer.toString(currentOrderNumber - 1));


                                            } catch (Exception e) {
                                                Log.v("Exception", "" + e.getMessage());
                                            }


                                        } else if (!snapshot.exists()) {

                                            mPaymentHotelList.child(HotelName).child("hotelName").setValue(HotelName).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    mPaymentHotelList.child(HotelName).child("OrderNumber").setValue("1").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            mPaymentHotelList.child(HotelName).child("payment").setValue(String.valueOf(TotalPrice)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.v("totalPrice", "" + TotalPrice);
                                                                    Toast.makeText(Vendor.getAppContext(), "sucessful", Toast.LENGTH_SHORT).show();
                                                                    FragmentManager fragmentManager = getFragmentManager();
                                                                    FragmentTransaction transaction = null;
                                                                    if (fragmentManager != null) {
                                                                        transaction = fragmentManager.beginTransaction();
                                                                        transaction.setReorderingAllowed(true);

                                                                        // Replace whatever is in the fragment_container view with this fragment
                                                                        transaction.replace(R.id.fragment_container, new HomeFragment(), null);

                                                                        // Commit the transaction
                                                                        transaction.commit();

                                                                    }
                                                                    prepareNotificationMessage(Integer.toString(currentOrderNumber - 1));
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(Vendor.getAppContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }
                                                    });

                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                };

                                mPaymentHotelList.orderByChild("hotelName").equalTo(HotelName).addValueEventListener(valueEventListener3);
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Vendor.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (userType.equalsIgnoreCase("guest")) {
                if (cartDataArrayListGuest.size() == 0) {
                    Toast.makeText(Vendor.getAppContext(), "no data", Toast.LENGTH_SHORT).show();
                    return;
                }


                //getting the current date and time
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMM.dd");
                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aa");
                currentDate = sdf.format(new Date());
                currentTime = sdf1.format(new Date());
                today = currentDate.replace(".", "");

                for (int i = 0; i < cartDataArrayListGuest.size(); i++) {
                    final String itemName = cartDataArrayListGuest.get(i).getItem_Name();
                    String itemQuantity = cartDataArrayListGuest.get(i).getItem_Quantity();
                    String itemPrice = cartDataArrayListGuest.get(i).getItem_Price();
                    String itemFinalCost = cartDataArrayListGuest.get(i).getItem_Final_Cost();
                    String itemUnit = cartDataArrayListGuest.get(i).getItem_Unit();
                    String itemGm = cartDataArrayListGuest.get(i).getItem_Gms();
                    String imageItem = cartDataArrayListGuest.get(i).getItem_Image();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("itemName", itemName);
                    hashMap.put("itemQuantity", itemQuantity);
                    hashMap.put("itemUnit", itemUnit);
                    hashMap.put("itemFinalPrice", itemFinalCost);
                    hashMap.put("itemGms", itemGm);
                    hashMap.put("itemImage", imageItem);


                    orderContet.child(HotelName).child(Integer.toString(currentOrderNumber)).child(itemName).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                        }
                    });


                }

                EasyDB easyDB = EasyDB.init(getContext(), "ITEM_GUEST_DB")
                        .setTableName("ITEMS_GUEST_TABLE")
                        .addColumn(new Column("Item_Name", new String[]{"text", "unique"}))
                        .addColumn(new Column("Item_Price", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Final_Cost", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Quantity", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Unit", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Image", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Gms", new String[]{"text", "not null"}))
                        .doneTableColumn();

                Toast.makeText(Vendor.getAppContext(), "Order Placed", Toast.LENGTH_SHORT).show();
                easyDB.deleteAllDataFromTable();
                cartDataArrayListGuest.clear();
                cartSummary.clear();


                if (cartListener1 != null) {
                    cartListener1.navListener("cart");
                }

                adapterCartDataGuest.notifyDataSetChanged();
                adapterCartSummary.notifyDataSetChanged();
                txt_total.setText("Total Price: 00 Rs");


                //getting the current date and time


                // storing the currnet date and time and order number for myOrder node
                Map myOrder = new HashMap<>();
                myOrder.put("orderDate", currentDate);
                myOrder.put("orderTime", currentTime);
                myOrder.put("orderNumber", Integer.toString(currentOrderNumber));
                myOrder.put("orderStatus", "Pending");
                myOrder.put("total_order_amount", String.valueOf(TotalPrice));
                myOrder.put("memberType", "guest");
                myOrder.put("hotelName", HotelName);


                //updating myOrder

                mMyOrder.child(Integer.toString(currentOrderNumber)).updateChildren(myOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        HashMap<String, String> userDetails = new HashMap<>();
                        userDetails.put("Name", name);
                        userDetails.put("phone", phone);
                        userDetails.put("HotelName", HotelName);
                        userDetails.put("orderNumber", Integer.toString(currentOrderNumber));
                        userDetails.put("orderDate", currentDate);
                        userDetails.put("orderTime", currentTime);
                        userDetails.put("orderStatus", "Pending");
                        userDetails.put("total_order_amount", String.valueOf(TotalPrice));
                        userDetails.put("memberType", "guest");


                        mOrder.child(HotelName).child(Integer.toString(currentOrderNumber)).setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                valueEventListener3 = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists() && !isProcess) {

                                            try {

                                                HashMap<String, Object> hotelList = new HashMap<>();
                                                hotelList.put("hotelName", HotelName);
                                                hotelList.put("itemImage", "");
                                                hotelList.put("memberShip", "Guest");
                                                mHotelRef.child(HotelName).updateChildren(hotelList);
                                                isProcess = true;
                                                currentPayment = snapshot.child(HotelName).child("payment").getValue().toString();
                                                TotalOrderNumber = snapshot.child(HotelName).child("OrderNumber").getValue().toString();

                                                Log.v("currentPrice", "" + currentPayment);
                                                double Sum = Double.parseDouble(currentPayment) + TotalPrice;
                                                TotalOrderNumberInNumber = Integer.parseInt(TotalOrderNumber) + 1;
                                                Log.v("pirce", "" + Sum);
                                                HashMap<String, Object> updatePrice = new HashMap<>();
                                                updatePrice.put("payment", Double.toString(Sum));
                                                updatePrice.put("OrderNumber", String.valueOf(TotalOrderNumberInNumber));
                                                mPaymentHotelList.child(HotelName).updateChildren(updatePrice).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        //  Toast.makeText(Vendor.getAppContext(), ""+key, Toast.LENGTH_SHORT).show();

                                                        FragmentManager fragmentManager = getFragmentManager();
                                                        FragmentTransaction transaction = null;
                                                        if (fragmentManager != null) {
                                                            transaction = fragmentManager.beginTransaction();
                                                            transaction.setReorderingAllowed(true);

                                                            // Replace whatever is in the fragment_container view with this fragment
                                                            transaction.replace(R.id.fragment_container, new HomeFragment(), null);

                                                            // Commit the transaction
                                                            transaction.commit();

                                                        }
                                                        prepareNotificationMessage(Integer.toString(currentOrderNumber - 1));
                                                    }
                                                });
                                            } catch (Exception e) {
                                                Log.v("firstException", "" + e.getMessage());
                                            }


                                        } else if (!snapshot.exists()) {


                                            try {
                                                mPaymentHotelList.child(HotelName).child("hotelName").setValue(HotelName).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        mPaymentHotelList.child(HotelName).child("OrderNumber").setValue("1").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                mPaymentHotelList.child(HotelName).child("payment").setValue(String.valueOf(TotalPrice)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(Vendor.getAppContext(), "sucessful", Toast.LENGTH_SHORT).show();
                                                                        FragmentManager fragmentManager = getFragmentManager();
                                                                        FragmentTransaction transaction = null;
                                                                        if (fragmentManager != null) {
                                                                            transaction = fragmentManager.beginTransaction();
                                                                            transaction.setReorderingAllowed(true);

                                                                            // Replace whatever is in the fragment_container view with this fragment
                                                                            transaction.replace(R.id.fragment_container, new HomeFragment(), null);

                                                                            // Commit the transaction
                                                                            transaction.commit();

                                                                        }
                                                                        prepareNotificationMessage(Integer.toString(currentOrderNumber - 1));
                                                                    }
                                                                });
                                                            }
                                                        });


                                                    }
                                                });
                                            } catch (Exception e) {
                                                Log.v("Exce", "" + e.getMessage());
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                };


                                mPaymentHotelList.orderByChild("hotelName").equalTo(HotelName).addValueEventListener(valueEventListener3);

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Vendor.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "On Check Out Button " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


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


    public void prepareNotificationMessage(String orderId) {
        //when user place order, send notification to vendor

        String NOTIFICATION_TOPIC = "/topics/" + Constants.FCM_TOPIC;
        String NOTIFICATION_TITLE = "New Order:  " + orderId;
        String NOTIFICATION_MEASSAGE = "A New Order is placed";
        String NOTIFICATION_TYPE = "NewOrder";


        //prepare json
        JSONObject notificationJO = new JSONObject();
        JSONObject notificationBodyJO = new JSONObject();

        try {
            //what to send
            notificationBodyJO.put("notificationType", NOTIFICATION_TYPE);
            notificationBodyJO.put("buyerUid", userId);
            notificationBodyJO.put("sellerUid", shopUid);
            notificationBodyJO.put("orderId", orderId);
            notificationBodyJO.put("NotificationTitle", NOTIFICATION_TITLE);
            notificationBodyJO.put("notificationMessage", NOTIFICATION_MEASSAGE);


            //where to send
            notificationJO.put("to", NOTIFICATION_TOPIC);
            notificationJO.put("data", notificationBodyJO);

        } catch (Exception e) {

            Log.v("exe", "" + e.getMessage());
        }

        sendFCMNotification(notificationJO, orderId);


    }

    private void sendFCMNotification(JSONObject notificationJO, String oredrID) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationJO, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //after sending fcm start order details activity


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = null;
                if (fragmentManager != null) {
                    transaction = fragmentManager.beginTransaction();
                    transaction.setReorderingAllowed(true);

                    // Replace whatever is in the fragment_container view with this fragment
                    transaction.replace(R.id.fragment_container, new HomeFragment(), null);

                    // Commit the transaction
                    transaction.commit();

                }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mRef != null && valueEventListener != null) {
                mRef.removeEventListener(valueEventListener);
            }

            if (TotalProduct != null && valueEventListener1 != null && valueEventListener2 != null) {
                TotalProduct.removeEventListener(valueEventListener1);
                TotalProduct.removeEventListener(valueEventListener2);
            }


            if (mPaymentHotelList != null && valueEventListener3 != null) {
                mPaymentHotelList.removeEventListener(valueEventListener3);
            }
        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "On Destroy: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void loadItemSummary() {

        try {
            cartSummary = new ArrayList<>();

            if (userType.equalsIgnoreCase("Gold") || userType.equalsIgnoreCase("Silver") || userType.equalsIgnoreCase("Bronze")
                    || userType.equalsIgnoreCase("Fix")) {
                if (cartDataArrayList.size() == 0) {
                    cartSummary.clear();
                    return;
                }
                EasyDB easyDB = EasyDB.init(getContext(), "ITEM_DB")
                        .setTableName("ITEMS_TABLE")
                        .addColumn(new Column("Item_Name", new String[]{"text", "unique"}))
                        .addColumn(new Column("Item_Price", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Final_Cost", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Quantity", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Unit", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Image", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Gms", new String[]{"text", "not null"}))
                        .doneTableColumn();


                Cursor res = easyDB.getAllData();

                while (res.moveToNext()) {
                    String itemName = res.getString(1);
                    String itemQuantity = res.getString(4);
                    String itemUnit = res.getString(5);
                    String itemGms = res.getString(7);
                    String final_price = res.getString(3);

                    CartData cartDataSummary = new CartData("" + itemName, "" + itemQuantity, "" + itemUnit, "" + itemGms,""+final_price);

                    cartSummary.add(cartDataSummary);
                }

                adapterCartSummary = new AdapterCartSummary(getContext(), cartSummary);
                recyclerSummary.setAdapter(adapterCartSummary);
            } else if (userType.equals("guest")) {

                if (cartDataArrayListGuest.size() != 0) {
                    cartSummary.clear();

                }
                EasyDB easyDB = EasyDB.init(getContext(), "ITEM_GUEST_DB")
                        .setTableName("ITEMS_GUEST_TABLE")
                        .addColumn(new Column("Item_Name", new String[]{"text", "unique"}))
                        .addColumn(new Column("Item_Price", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Final_Cost", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Quantity", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Unit", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Image", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Gms", new String[]{"text", "not null"}))
                        .doneTableColumn();


                Cursor res = easyDB.getAllData();

                while (res.moveToNext()) {
                    String itemName = res.getString(1);
                    String itemQuantity = res.getString(4);
                    String itemUnit = res.getString(5);
                    String itemGms = res.getString(7);
                    String final_price = res.getString(3);

                    CartData cartDataSummary = new CartData("" + itemName, "" + itemQuantity, "" + itemUnit, "" + itemGms, ""+final_price);

                    cartSummary.add(cartDataSummary);
                }

                adapterCartSummary = new AdapterCartSummary(getContext(), cartSummary);
                recyclerSummary.setAdapter(adapterCartSummary);
            }
        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "On Total Summary: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    private void loadAllProducts() {


        //loading all products from cart node
        try {
            if (userType.equalsIgnoreCase("Gold") || userType.equalsIgnoreCase("Silver") || userType.equalsIgnoreCase("Bronze")
                    || userType.equalsIgnoreCase("Fix")) {

                cartDataArrayList = new ArrayList<>();


                txt_total.setVisibility(View.INVISIBLE);

                EasyDB easyDB = EasyDB.init(getContext(), "ITEM_DB")
                        .setTableName("ITEMS_TABLE")
                        .addColumn(new Column("Item_Name", new String[]{"text", "unique"}))
                        .addColumn(new Column("Item_Price", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Final_Cost", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Quantity", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Unit", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Image", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Gms", new String[]{"text", "not null"}))
                        .doneTableColumn();

                Cursor res = easyDB.getAllData();
                while (res.moveToNext()) {
                    String itemName = res.getString(1);
                    String itemPrice = res.getString(2);
                    String itemFinalCost = res.getString(3);
                    String itemQuantity = res.getString(4);
                    String itemUnit = res.getString(5);
                    String itemImage = res.getString(6);
                    String itemGms = res.getString(7);

                    TotalPrice = TotalPrice + Double.parseDouble(itemFinalCost);

                    CartData cartData = new CartData("" + itemName
                            , "" + itemPrice
                            , "" + itemFinalCost
                            , "" + itemQuantity
                            , "" + itemUnit
                            , "" + itemImage
                            , "" + itemGms
                    );

                    cartDataArrayList.add(cartData);
                }

                adapterCartData = new AdapterCartData(getContext(), cartDataArrayList);
                recyclerCart.setAdapter(adapterCartData);


            } else if (userType.equals("guest")) {
                cartDataArrayListGuest = new ArrayList<>();


                EasyDB easyDB = EasyDB.init(getContext(), "ITEM_GUEST_DB")
                        .setTableName("ITEMS_GUEST_TABLE")
                        .addColumn(new Column("Item_Name", new String[]{"text", "unique"}))
                        .addColumn(new Column("Item_Price", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Final_Cost", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Quantity", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Unit", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Image", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Gms", new String[]{"text", "not null"}))
                        .doneTableColumn();

                Cursor res = easyDB.getAllData();
                while (res.moveToNext()) {
                    String itemName = res.getString(1);
                    String itemPrice = res.getString(2);
                    String itemFinalCost = res.getString(3);
                    String itemQuantity = res.getString(4);
                    String itemUnit = res.getString(5);
                    String itemImage = res.getString(6);
                    String itemGms = res.getString(7);

                    TotalPrice = TotalPrice + Double.parseDouble(itemFinalCost);

                    CartData cartData = new CartData("" + itemName
                            , "" + itemPrice
                            , "" + itemFinalCost
                            , "" + itemQuantity
                            , "" + itemUnit
                            , "" + itemImage
                            , "" + itemGms
                    );

                    cartDataArrayListGuest.add(cartData);
                }

                txt_total.setText("Total Price: " + "Rs " + Double.toString(TotalPrice));

                adapterCartDataGuest = new AdapterCartDataGuest(getContext(), cartDataArrayListGuest);
                recyclerCart.setAdapter(adapterCartDataGuest);


            }
        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "On Load Data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


}
