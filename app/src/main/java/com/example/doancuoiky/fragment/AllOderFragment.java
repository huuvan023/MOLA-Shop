package com.example.doancuoiky.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.doancuoiky.GlobalVariable;
import com.example.doancuoiky.R;
import com.example.doancuoiky.activity.OrderDetailActivity;
import com.example.doancuoiky.adapter.OrderAdapter;
import com.example.doancuoiky.modal.Order;

import java.util.ArrayList;

public class AllOderFragment extends Fragment {

    public AllOderFragment() {
        // Required empty public constructor
    }

    ListView lvOrder;
    RelativeLayout noticeOrderIsEmpty;
    OrderAdapter orderAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_all_oder, container, false);

        anhXa(view);
        if(GlobalVariable.arrayOrder.size() == 0){
            lvOrder.setVisibility(View.GONE);
            noticeOrderIsEmpty.setVisibility(View.VISIBLE);
        }else{
            lvOrder.setVisibility(View.VISIBLE);
            noticeOrderIsEmpty.setVisibility(View.GONE);
        }

        orderAdapter.gotoDetail(new OrderAdapter.IClickGoToDetail() {
            @Override
            public void onClickGoToDetail(int position) {
                Intent intent = new Intent(view.getContext(), OrderDetailActivity.class);
                intent.putExtra("gotoOrderDetail",position);
                intent.putExtra("orderID",GlobalVariable.arrayOrder.get(position).getId_bill_order());
                intent.putExtra("orderStatus",GlobalVariable.arrayOrder.get(position).getOrder_status());
                intent.putExtra("orderTotal",GlobalVariable.arrayOrder.get(position).getTotal());
                view.getContext().startActivity(intent);
            }
        });


        return view;

    }

    private void anhXa(View view) {
        lvOrder = view.findViewById(R.id.lv_all_order_fragment_all_order);
        noticeOrderIsEmpty = view.findViewById(R.id.layout_notice_order_empty_all_order);

        orderAdapter = new OrderAdapter(getContext(),R.layout.item_order,GlobalVariable.arrayOrder);
        lvOrder.setAdapter(orderAdapter);

    }
}
