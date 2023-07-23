package com.example.finance;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cuberto.liquid_swipe.LiquidPager;
import com.example.finance.liquidswipe.ViewPager;

public class activity_liquidswipe extends AppCompatActivity {


    LiquidPager pager;
    com.example.finance.liquidswipe.ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquidswipe);
        pager = findViewById(R.id.pager);
        viewPager= new ViewPager(getSupportFragmentManager());
        pager.setAdapter(viewPager);


    }
}