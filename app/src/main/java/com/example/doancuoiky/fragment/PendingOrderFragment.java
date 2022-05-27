package com.example.doancuoiky.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doancuoiky.GlobalVariable;
import com.example.doancuoiky.R;
import com.example.doancuoiky.activity.OrderDetailActivity;
import com.example.doancuoiky.adapter.OrderAdapter;
import com.example.doancuoiky.modal.Order;

import java.util.ArrayList;

public class PendingOrderFragment extends Fragment {

    ListView lvPendingOrder;
    ArrayList<Order> arrayPendingOrder;
    RelativeLayout noticeOrderIsEmpty;
    OrderAdapter orderAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_pending_oder, container, false);

        anhXa(view);

        if(arrayPendingOrder.size() == 0){
            lvPendingOrder.setVisibility(View.GONE);
            noticeOrderIsEmpty.setVisibility(View.VISIBLE);
        }else{
            lvPendingOrder.setVisibility(View.VISIBLE);
            noticeOrderIsEmpty.setVisibility(View.GONE);
        }

        orderAdapter.gotoDetail(new OrderAdapter.IClickGoToDetail() {
            @Override
            public void onClickGoToDetail(int position) {
                Intent intent = new Intent(view.getContext(), OrderDetailActivity.class);
                intent.putExtra("gotoOrderDetail",position);
                intent.putExtra("orderID",arrayPendingOrder.get(position).getId_bill_order());
                intent.putExtra("orderStatus",arrayPendingOrder.get(position).getOrder_status());
                intent.putExtra("orderTotal",arrayPendingOrder.get(position).getTotal());
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }

    private void anhXa(View view) {
        lvPendingOrder = view.findViewById(R.id.lv_pending_order_fragment_pending_order);
        noticeOrderIsEmpty = view.findViewById(R.id.layout_notice_order_empty_pending_order);
        if(arrayPendingOrder == null ){
            arrayPendingOrder = new ArrayList<>();
            for(int i = 0;i < GlobalVariable.arrayOrder.size();i++){
                if(GlobalVariable.arrayOrder.get(i).getOrder_status() == 0){
                    arrayPendingOrder.add(GlobalVariable.arrayOrder.get(i));
                }
            }
        }
        else{
            arrayPendingOrder.clear();
            for(int i = 0;i < GlobalVariable.arrayOrder.size();i++){
                if(GlobalVariable.arrayOrder.get(i).getOrder_status() == 0){
                    arrayPendingOrder.add(GlobalVariable.arrayOrder.get(i));
                }
            }
        }
        orderAdapter = new OrderAdapter(getContext(),R.layout.item_order,arrayPendingOrder);
        lvPendingOrder.setAdapter(orderAdapter);
    }
}
