package com.example.doancuoiky.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.doancuoiky.R;
import com.example.doancuoiky.modal.Photo;
import com.example.doancuoiky.modal.PhotoProduct;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoProductAdapter extends PagerAdapter {

//    private HomeFragment mContext;
    Context mContext;
    private List<PhotoProduct> mListPhoto;

    public PhotoProductAdapter(Context mContext, List<PhotoProduct> mListPhoto) {
        this.mContext = mContext;
        this.mListPhoto = mListPhoto;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_photo,container,false);
        ImageView imgPhoto = view.findViewById(R.id.img_photo);

        PhotoProduct photo = mListPhoto.get(position);

        if(photo != null){
            Picasso.get()
                    .load(photo.getProductImage())
                    .into(imgPhoto);
        }

        // add view to view group
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if(mListPhoto != null){
            return mListPhoto.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
