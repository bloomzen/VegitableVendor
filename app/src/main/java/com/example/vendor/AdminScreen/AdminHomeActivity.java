package com.example.vendor.AdminScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.vendor.R;
import com.example.vendor.UserScreens.CartFragment;
import com.example.vendor.UserScreens.CategoriesFragment;
import com.example.vendor.UserScreens.HomeFragment;
import com.example.vendor.UserScreens.ProfileFragment;
import com.example.vendor.UserScreens.SearchFragment;
import com.example.vendor.UserScreens.ViewOrdersFragment;
import com.example.vendor.UserScreens.cartListener;
import com.example.vendor.Vendor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayDeque;
import java.util.Deque;

public class AdminHomeActivity extends AppCompatActivity implements cartListener {


    BottomNavigationView bottomNavigationView;
    Deque<Integer> integerDeque = new ArrayDeque<>(3);
    boolean flag = true;


    @Override
    protected void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("orderID")) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_Admin_container, new AdminOrdersFragment()).commit();
            }
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);


        onNewIntent(getIntent());

        try {
            bottomNavigationView = findViewById(R.id.bottom_Admin_navigation);
            integerDeque.push(R.id.nav_hotelList);

            loadFragment(new AdminHotelListFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_hotelList);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    int id = item.getItemId();
                    if (integerDeque.contains(id)) {
                        if (id == R.id.nav_hotelList) {
                            if (integerDeque.size() != 1) {
                                if (flag) {
                                    integerDeque.addFirst(R.id.nav_hotelList);
                                    flag = false;
                                }
                            }


                        }

                        integerDeque.remove(id);


                    }

                    integerDeque.push(id);

                    loadFragment(getFragment(item.getItemId()));
                    return true;
                }
            });

        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "On Create" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_Admin_container, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    private Fragment getFragment(int itemId) {
        switch (itemId) {
            case R.id.nav_hotelList:
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                return new AdminHotelListFragment();

            case R.id.nav_addItem:
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                return new AdminAddItemsFragment();


            case R.id.nav_orderList:
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
                return new AdminOrdersFragment();


        }
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        return new AdminHotelListFragment();
    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            //super.onBackPressed();

            integerDeque.pop();
            if(!integerDeque.isEmpty()){
                //when deque is not empty
                loadFragment(getFragment(integerDeque.peek()));
            }else {
                //when dequw is empty
                finish();
            }
        }

    }

    @Override
    public void navListener(String str) {
        bottomNavigationView.getMenu().findItem(R.id.nav_hotelList).setChecked(true);
    }
}



