package com.example.doancuoiky.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.doancuoiky.GlobalVariable;
import com.example.doancuoiky.R;
import com.example.doancuoiky.activity.OrderDetailActivity;
import com.example.doancuoiky.modal.Order;
import com.example.doancuoiky.modal.RatingAndComment;

import java.text.DecimalFormat;
import java.util.List;

public class RatingAndCommentAdapter extends BaseAdapter {

    Context mContex;
    int mLayout;
    List<RatingAndComment> mListRateAndComments;



    public RatingAndCommentAdapter(Context context, int layout, List<RatingAndComment> listRateAndComments){
        mContex = context;
        mLayout = layout;
        mListRateAndComments = listRateAndComments;
    }

    @Override
    public int getCount() {
        return mListRateAndComments.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) mContex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(mLayout,null);

        anhXa(view,i);

        return view;
    }

    private void anhXa(View view, final int i) {
        TextView tvDateOrder = view.findViewById(R.id.tv_date_rate_and_comment);
        TextView tvComment = view.findViewById(R.id.tv_comment_rate_and_comment);
        TextView tvUser = view.findViewById(R.id.tv_id_user_rating_and_comment);
        RatingBar rtbRate = view.findViewById(R.id.rtb_in_rating_and_comment);

        tvDateOrder.setText(GlobalVariable.formatDateInVN(mListRateAndComments.get(i).getRatedate()));
        String _user = "user_" + mListRateAndComments.get(i).getId_user();
        tvUser.setText(_user);
        tvComment.setText(mListRateAndComments.get(i).getCmt());
        rtbRate.setRating(mListRateAndComments.get(i).getRate());

    }
}
