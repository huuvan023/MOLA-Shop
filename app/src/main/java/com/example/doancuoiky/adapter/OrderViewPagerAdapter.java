package com.example.doancuoiky.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.doancuoiky.fragment.AllOderFragment;
import com.example.doancuoiky.fragment.CartFragment;
import com.example.doancuoiky.fragment.HomeFragment;
import com.example.doancuoiky.fragment.PendingOrderFragment;
import com.example.doancuoiky.fragment.ProductFragment;
import com.example.doancuoiky.fragment.ProfileFragment;
import com.example.doancuoiky.fragment.SearchFragment;
import com.example.doancuoiky.fragment.ShippingOderFragment;
import com.example.doancuoiky.fragment.SuccessOderFragment;

public class OrderViewPagerAdapter extends FragmentStatePagerAdapter {
    public OrderViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AllOderFragment();
            case 1:
                return new PendingOrderFragment();
            case 2:
                return new ShippingOderFragment();
            case 3:
                return new SuccessOderFragment();
            default:
                return new AllOderFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";

        switch (position){
            case 0:
                title = "Tất cả";
                break;
            case 1:
                title = "Chờ xử lý";
                break;
            case 2:
                title = "Đang giao";
                break;
            case 3:
                title = "Hoàn tất";
                break;
            default:
                title = "Tất cả";
                break;
        }

        return title;
    }
}
