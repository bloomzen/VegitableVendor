package com.example.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


import com.example.vendor.Adapters.MyAdapter;
import com.example.vendor.UserScreens.HomeActivity;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class slider extends AppCompatActivity {


    ViewPager viewPager;
    MyAdapter myAdapter;
    SliderView sliderView;
    RelativeLayout btn_getStarted;


    //ImageSlider imageSlider = findViewById(R.id.slider);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        sliderView = findViewById(R.id.image_slider);
        btn_getStarted = findViewById(R.id.btn_getStarted);

        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.onboard_thriid);
        images.add(R.drawable.onboard_second);
        images.add(R.drawable.onboard_first);



        myAdapter = new MyAdapter(images);

        sliderView.setSliderAdapter(myAdapter);
        sliderView.setAutoCycle(true);

        btn_getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });






    }
}
