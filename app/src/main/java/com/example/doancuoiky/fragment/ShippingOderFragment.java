package com.example.doancuoiky.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doancuoiky.GlobalVariable;
import com.example.doancuoiky.R;
import com.example.doancuoiky.activity.OrderDetailActivity;
import com.example.doancuoiky.adapter.OrderAdapter;
import com.example.doancuoiky.modal.Order;

import java.util.ArrayList;

public class ShippingOderFragment extends Fragment {

    ListView lvShippingOrder;
    ArrayList<Order> arrayShippingOrder;
    RelativeLayout noticeOrderIsEmpty;
    OrderAdapter orderAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_shipping_oder,container,false);

        anhXa(view);
        if(arrayShippingOrder.size() == 0){
            lvShippingOrder.setVisibility(View.GONE);
            noticeOrderIsEmpty.setVisibility(View.VISIBLE);
        }else{
            lvShippingOrder.setVisibility(View.VISIBLE);
            noticeOrderIsEmpty.setVisibility(View.GONE);
        }

        orderAdapter.gotoDetail(new OrderAdapter.IClickGoToDetail() {
            @Override
            public void onClickGoToDetail(int position) {
                Intent intent = new Intent(view.getContext(), OrderDetailActivity.class);
                intent.putExtra("gotoOrderDetail",position);
                intent.putExtra("orderID",arrayShippingOrder.get(position).getId_bill_order());
                intent.putExtra("orderStatus",arrayShippingOrder.get(position).getOrder_status());
                intent.putExtra("orderTotal",arrayShippingOrder.get(position).getTotal());
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }

    private void anhXa(View view) {
        lvShippingOrder = view.findViewById(R.id.lv_shipping_order_fragment_pending_order);
        noticeOrderIsEmpty = view.findViewById(R.id.layout_notice_order_empty_shipping_order);

        if(arrayShippingOrder == null){
            arrayShippingOrder = new ArrayList<>();
            for(int i = 0; i < GlobalVariable.arrayOrder.size(); i++){
                if(GlobalVariable.arrayOrder.get(i).getOrder_status() == 1){
                    arrayShippingOrder.add(GlobalVariable.arrayOrder.get(i));
                }
            }
        }
        else{
            arrayShippingOrder.clear();
            for(int i = 0;i < GlobalVariable.arrayOrder.size();i++){
                if(GlobalVariable.arrayOrder.get(i).getOrder_status() == 1){
                    arrayShippingOrder.add(GlobalVariable.arrayOrder.get(i));
                }
            }
        }

        orderAdapter = new OrderAdapter(getContext(),R.layout.item_order,arrayShippingOrder);
        lvShippingOrder.setAdapter(orderAdapter);
    }
}