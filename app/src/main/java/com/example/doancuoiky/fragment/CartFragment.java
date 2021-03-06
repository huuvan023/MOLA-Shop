package com.example.doancuoiky.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.doancuoiky.GlobalVariable;
import com.example.doancuoiky.R;
import com.example.doancuoiky.activity.MainActivity;
import com.example.doancuoiky.adapter.CartAdapter;
import com.example.doancuoiky.adapter.PhotoAdapter;
import com.example.doancuoiky.adapter.ProductAdapter;
import com.example.doancuoiky.modal.Cart;
import com.example.doancuoiky.modal.Order;
import com.example.doancuoiky.modal.Photo;
import com.example.doancuoiky.modal.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingFormatArgumentException;
import java.util.Objects;
import java.util.Timer;

import me.relex.circleindicator.CircleIndicator;

public class CartFragment extends Fragment  {

    private LinearLayout oderContainer;
    private Button btnOder;
    private TextView tvNotice;
    public static TextView tvTotal;
    private Product product;
    String receiveAddress = "";


    ListView lvCart;
    public static CartAdapter cartAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        anhXa(view);

        // ki???m tra arrayCart c?? r???ng kh??ng
        checkData();

        // s??? ki???n khi nh???n v??o button ?????t h??ng
        btnOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String address = GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_ADDRESS);

                if(address.length() == 0 || address.equals("null")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Th??ng b??o");
                    builder.setMessage("Vui l??ng nh???p ?????a ch??? nh???n h??ng:");
                    builder.setPositiveButton("Nh???p ?????a ch???", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DialogInputReceiveAddress();
                        }
                    });

                    builder.show();

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Th??ng b??o");
                    builder.setMessage("B???n c?? mu???n giao h??ng ?????n ?????a ch??? n??y ? \n" +
                            GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_ADDRESS));
                    builder.setPositiveButton("?????ng ??", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            onCreateBill(GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_ADDRESS),
                                    updateTotalPrice(),GlobalVariable.arrayCart);
                        }
                    });

                    builder.setNegativeButton("?????a ch??? kh??c", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DialogInputReceiveAddress();
                        }
                    });

                    builder.show();
                }

            }
        });

        cartAdapter.onDelete(new CartAdapter.IClickOnDeleteProductInCart() {
            @Override
            public void onClickDeleteProductInCart(final int index) {

                AlertDialog.Builder alertDelete;
                alertDelete = new AlertDialog.Builder(getContext());
                alertDelete.setTitle("Th??ng b??o");
                alertDelete.setMessage("B???n c?? mu???n x??a s???n ph???m n??y kh???i gi??? h??ng?");

                alertDelete.setPositiveButton("C??", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // tr??? l???i tr???ng th??i enebal cho button th??m gi??? h??ng -> Fragment Product
                        // (??ang duy???t d???a tr??n t??n s???n ph???m) -> ?????i l???i id sau n??y
                        String name = GlobalVariable.arrayCart.get(index).getName();
                        for(int k = 0 ;k < GlobalVariable.arrayProduct.size();k ++){
                            if(GlobalVariable.arrayProduct.get(k).getProductName().equals(name)){
                                product = GlobalVariable.arrayProduct.get(k);
                                product.setAddToCart(false);
                            }
                        }

                        GlobalVariable.arrayCart.remove(index);
                        cartAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(),"X??a th??nh c??ng",Toast.LENGTH_SHORT).show();
                        MainActivity.setCountProductInCart(GlobalVariable.arrayCart.size());
                        checkData();

                        updateTotalPrice();
                    }
                });

                alertDelete.setNegativeButton("Kh??ng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                alertDelete.show();
            }
        });

        return  view;
    }

    // kiem tra neu gio hang rong thi hien thong bao gio hang rong
    private void checkData() {
        if(GlobalVariable.arrayCart.size() <= 0){
            cartAdapter.notifyDataSetChanged();
            tvNotice.setVisibility(View.VISIBLE);
            lvCart.setVisibility(View.INVISIBLE);
            oderContainer.setVisibility(LinearLayout.INVISIBLE);
        }
        else{
            cartAdapter.notifyDataSetChanged();
            oderContainer.setVisibility(LinearLayout.VISIBLE);
            tvNotice.setVisibility(View.INVISIBLE);
            lvCart.setVisibility(View.VISIBLE);
        }
    }

    private void anhXa(View view) {
        oderContainer = view.findViewById(R.id.order_container);
        btnOder = view.findViewById(R.id.btn_order);
        tvNotice = view.findViewById(R.id.tv_notice);
        tvTotal = view.findViewById(R.id.tv_total);
        lvCart = view.findViewById(R.id.lv_cart);

        if(GlobalVariable.arrayCart.size() > 0) {
            MainActivity.setCountProductInCart(GlobalVariable.arrayCart.size());
        }
        updateTotalPrice();
        cartAdapter = new CartAdapter(getContext(),R.layout.item_cart,GlobalVariable.arrayCart);
        lvCart.setAdapter(cartAdapter);
    }

    public static String updateTotalPrice(){
        int total = 0;
        for(int i = 0;i < GlobalVariable.arrayCart.size();i++){
            int count = GlobalVariable.arrayCart.get(i).getCount();
            int caculatePrice = GlobalVariable.arrayCart.get(i).getPrice() -
                    (GlobalVariable.arrayCart.get(i).getPrice()/100)*GlobalVariable.arrayCart.get(i).getSale();
            total += caculatePrice * count;
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        String _price = decimalFormat.format(total) + "??";

        tvTotal.setText(_price);
        return String.valueOf(total);
    }

    private void DialogInputReceiveAddress() {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.setContentView(R.layout.dialog_address);

        Button btnSubmit = dialog.findViewById(R.id.btn_submit_address);
        final EditText edtAddress = dialog.findViewById(R.id.edt_input_address);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               receiveAddress = edtAddress.getText().toString();
               AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
               builder.setTitle("Th??ng b??o");
               builder.setMessage("X??c nh???n giao h??ng ?????n ?????a ch???:\n" + receiveAddress);

               builder.setPositiveButton("X??c nh???n", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       onCreateBill(receiveAddress,updateTotalPrice() ,GlobalVariable.arrayCart);

                       dialog.dismiss();
                   }
               });
               builder.setNegativeButton("H???y", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {

                   }
               });

               builder.show();
            }
        });

        dialog.show();
    }

    private void onCreateBill(String addressDelivery,String total, ArrayList<Cart> carts){

        //Create Main jSon object
        JSONObject jsonParams = new JSONObject();

        try {
            //Add string params
            jsonParams.put("address_delivery", addressDelivery);
            jsonParams.put("total", total);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Create json array for products
        JSONArray array = new JSONArray();
        for(int ii = 0;ii < carts.size(); ii++){
            JSONObject objParams =  new JSONObject();
            try {
                objParams.put("id",carts.get(ii).getID());
                objParams.put("quanlity",String.valueOf(carts.get(ii).getCount()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Add the product to array
            array.put(objParams);
        }

        //Add array to main json object
        try {
            jsonParams.put("products",array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest json_request = new JsonObjectRequest(JsonObjectRequest.Method.POST, GlobalVariable.CREATE_BILL_URL, jsonParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject result = response.getJSONObject("result");
                    int code = Integer.parseInt(result.getString("code"));
                    if(code == 0){
                        Toast.makeText(getContext(), "?????t h??ng th??nh c??ng", Toast.LENGTH_SHORT).show();
                        GlobalVariable.arrayCart.clear();
                        ProfileFragment.setDataUserOrder(getContext());
                        checkData();
                        MainActivity.setCountProductInCart(0);
                        cartAdapter.notifyDataSetChanged();
                        // tr??? l???i tr???ng th??i enebal cho t???t c??? button th??m trong Product Fragment
                        for(int index = 0;index < GlobalVariable.arrayProduct.size();index++){
                            product = GlobalVariable.arrayProduct.get(index);
                            product.setAddToCart(false);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "fail1", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "fail2", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization",GlobalVariable.TOKEN);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        queue.add(json_request);
    }
}
