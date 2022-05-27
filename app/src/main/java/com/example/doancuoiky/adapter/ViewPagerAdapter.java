package com.example.doancuoiky.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.doancuoiky.fragment.CartFragment;
import com.example.doancuoiky.fragment.HomeFragment;
import com.example.doancuoiky.fragment.ProductFragment;
import com.example.doancuoiky.fragment.ProfileFragment;
import com.example.doancuoiky.fragment.SearchFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new ProductFragment();
            case 2:
                return new SearchFragment();
            case 3:
                return new CartFragment();
            case 4:
                return new ProfileFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
