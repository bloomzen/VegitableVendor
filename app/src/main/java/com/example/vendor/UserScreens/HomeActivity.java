package com.example.vendor.UserScreens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayDeque;
import java.util.Deque;

public class HomeActivity extends AppCompatActivity implements cartListener {

    //base activity for bottom navigation


    BottomNavigationView bottomNavigationView;
    Deque<Integer> integerDeque = new ArrayDeque<>(5);
    boolean flag = true;

    @Override
    protected void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("orderID")) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ViewOrdersFragment()).commit();
            }
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        onNewIntent(getIntent());


        try {
            bottomNavigationView = findViewById(R.id.bottom_navigation);


            integerDeque.push(R.id.nav_home);

            loadFragment(new HomeFragment());

            bottomNavigationView.setSelectedItemId(R.id.nav_home);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    int id = item.getItemId();
                    if(integerDeque.contains(id)){
                        if(id == R.id.nav_home){
                            if(integerDeque.size() != 1){
                                if(flag){
                                    integerDeque.addFirst(R.id.nav_home);
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
            Toast.makeText(Vendor.getAppContext(), "On Create Bottom Navigation: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName())
                .commit();
    }



    @Override
    public void navListener(String str) {
        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
    }

    private Fragment getFragment(int itemId){
        switch (itemId){
            case R.id.nav_home:
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                return new HomeFragment();

            case R.id. nav_categories:
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                return new CategoriesFragment();


            case R.id.nav_search:
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
                return new SearchFragment();


            case R.id.nav_cart:
                bottomNavigationView.getMenu().getItem(3).setChecked(true);
                return new CartFragment();


            case R.id.nav_profile:
                bottomNavigationView.getMenu().getItem(4).setChecked(true);
                return new ProfileFragment();

        }
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        return new HomeFragment();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();







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
}


