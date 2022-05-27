package com.example.doancuoiky.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.doancuoiky.GlobalVariable;
import com.example.doancuoiky.R;
import com.example.doancuoiky.adapter.OrderViewPagerAdapter;
import com.example.doancuoiky.modal.Order;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class OrderManagementActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ImageView goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder_management);

        anhXa();

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void anhXa() {
        mTabLayout = findViewById(R.id.tab_layout_order);
        mViewPager = findViewById(R.id.view_pager_order);
        goBack = findViewById(R.id.iv_back_order_management);

        OrderViewPagerAdapter adapter = new OrderViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(adapter);

        mTabLayout.setupWithViewPager(mViewPager);

        // chuyen den man hinh tuong ung
        Intent intent = getIntent();
        String toProfile = intent.getStringExtra("gotoOrderManagement");
        if (toProfile != null && toProfile.contentEquals("all")) {
            TabLayout.Tab tab = mTabLayout.getTabAt(0);
            assert tab != null;
            tab.select();
        }
        if (toProfile != null && toProfile.contentEquals("pending")) {
            TabLayout.Tab tab = mTabLayout.getTabAt(1);
            assert tab != null;
            tab.select();
        }
        if (toProfile != null && toProfile.contentEquals("shipping")) {
            TabLayout.Tab tab = mTabLayout.getTabAt(2);
            assert tab != null;
            tab.select();
        }
        if (toProfile != null && toProfile.contentEquals("success")) {
            TabLayout.Tab tab = mTabLayout.getTabAt(3);
            assert tab != null;
            tab.select();
        }

//        if(GlobalVariable.arrayOrder == null){
//            GlobalVariable.arrayOrder = new ArrayList<>();
//
//            GlobalVariable.arrayOrder.add(new Order("01","11-03-2020 12:15:03",0,20000000));
//            GlobalVariable.arrayOrder.add(new Order("02","11-03-2020 12:15:03",0,18000000));
//            GlobalVariable.arrayOrder.add(new Order("03","11-03-2020 12:15:03",1,23500000));
//            GlobalVariable.arrayOrder.add(new Order("04","11-03-2020 12:15:03",2,8000000));
//        }

    }
}