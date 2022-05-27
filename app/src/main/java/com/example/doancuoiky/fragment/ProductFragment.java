package com.example.doancuoiky.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancuoiky.AnimationUtil;
import com.example.doancuoiky.GlobalVariable;
import com.example.doancuoiky.activity.LoginActivity;
import com.example.doancuoiky.activity.ProductDetailActivity;
import com.example.doancuoiky.modal.Cart;
import com.example.doancuoiky.modal.Product;
import com.example.doancuoiky.adapter.ProductAdapter;
import com.example.doancuoiky.R;
import com.example.doancuoiky.activity.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class ProductFragment extends Fragment  {

    private RecyclerView rcvProduct;
    private TextView noticeNoDataReturn;
    private View mView;
    private MainActivity mainActivity;
    @SuppressLint("StaticFieldLeak")
    static public ProductAdapter productAdapter;
    private Spinner sortSpinner;
    static ArrayList<Product> mArrayProduct;
    LinearLayout filter;
    String filterType = "", filterPrice = "";
    Dialog dialog ;
    TextView tvFilterType,tvFilterPrice;
    CardView cvFilterType,cvFilterPrice;


    private RadioButton rdAllProduct,rdMobile,rdLaptop,rdPriceType1,rdPriceType2,rdPriceType3,rdPriceType4;
    private Button btnCancel,btnSubmit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_product, container, false);
        mainActivity = (MainActivity) getActivity();

        anhXa(mView);

        setAdapterRecycleViewProduct();

        productAdapter.gotoDetail(new ProductAdapter.IClickGotoDetailListener() {
            @Override
            public void onClickGotoDetail(int pos) {
                String idProduct = mArrayProduct.get(pos).getProductID();
                Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                intent.putExtra("productDetail", idProduct);
                Objects.requireNonNull(getContext()).startActivity(intent);

            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(Objects.requireNonNull(getContext()));
                dialog.setContentView(R.layout.dialog_product_filter);
                dialog.setCanceledOnTouchOutside(false); // không tắt dialog khi nhấn ra bên ngoài
                dialog.setCancelable(false); // không tắt dialog khi nhấn phím back
                dialogAnhXa(dialog);

                dialogSetOnClick(dialog);
                dialog.show();
            }
        });

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        noSort();
                        break;
                    case 1:
                        ascendingSortPrice();
                        break;
                    case 2:
                        descendingSortPrice();
                        break;
                    case 3:
                        descendingSortRating();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        return  mView;
    }

    private void checkDataReturn() {
        if(mArrayProduct.isEmpty()){
            rcvProduct.setVisibility(View.GONE);
            noticeNoDataReturn.setVisibility(View.VISIBLE);
        }
        else{
            rcvProduct.setVisibility(View.VISIBLE);
            noticeNoDataReturn.setVisibility(View.GONE);
        }

    }

    private void dialogSetOnClick(final Dialog dialog) {

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                filterType = "";
                filterPrice = "";
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filterPrice.length() == 0 && filterType.length() == 0){
                    Toast.makeText(mainActivity, "Vui lòng chọn tiêu chí muốn lọc", Toast.LENGTH_SHORT).show();
                }
                else{
                    mArrayProduct.clear();

                    setTextFilterBy();

                    // lọc tất cả sản phẩm + lọc theo giá
                    if(filterType.equals("ALL") || filterType.length() == 0){
                        if(filterPrice.length() == 0){
                            mArrayProduct.addAll(GlobalVariable.arrayProduct);
                        }
                        else if(filterPrice.equals("TYPE1")){
                            for(int i = 0;i < GlobalVariable.arrayProduct.size();i++){
                                final int price = GlobalVariable.arrayProduct.get(i).getProductPrice();
                                if(price <= 5000000){
                                    mArrayProduct.add(GlobalVariable.arrayProduct.get(i));
                                }
                            }
                        }
                        else if(filterPrice.equals("TYPE2")){
                            for(int i = 0;i < GlobalVariable.arrayProduct.size();i++){
                                final int price = GlobalVariable.arrayProduct.get(i).getProductPrice();
                                if(price > 5000000 && price <= 10000000){
                                    mArrayProduct.add(GlobalVariable.arrayProduct.get(i));
                                }
                            }

                        }
                        else if(filterPrice.equals("TYPE3")){
                            for(int i = 0;i < GlobalVariable.arrayProduct.size();i++){
                                final int price = GlobalVariable.arrayProduct.get(i).getProductPrice();
                                if(price > 10000000 && price <= 20000000){
                                    mArrayProduct.add(GlobalVariable.arrayProduct.get(i));
                                }
                            }

                        }
                        else if(filterPrice.equals("TYPE4")){
                            for(int i = 0;i < GlobalVariable.arrayProduct.size();i++){
                                final int price = GlobalVariable.arrayProduct.get(i).getProductPrice();
                                if(price > 20000000){
                                    mArrayProduct.add(GlobalVariable.arrayProduct.get(i));
                                }
                            }
                        }

                    }

                    // lọc tất cả sản phẩm điện thoại + lọc theo giá
                    else if(filterType.equals("MOBILE")){
                        if(filterPrice.length() == 0){
                            mArrayProduct.addAll(GlobalVariable.arrayMobile);
                        }
                        else if(filterPrice.equals("TYPE1")){
                            for(int i = 0;i < GlobalVariable.arrayMobile.size();i++){
                                final int price = GlobalVariable.arrayMobile.get(i).getProductPrice();
                                if(price <= 5000000){
                                    mArrayProduct.add(GlobalVariable.arrayMobile.get(i));
                                }
                            }
                        }
                        else if(filterPrice.equals("TYPE2")){
                            for(int i = 0;i < GlobalVariable.arrayMobile.size();i++){
                                final int price = GlobalVariable.arrayMobile.get(i).getProductPrice();
                                if(price > 5000000 && price <= 10000000){
                                    mArrayProduct.add(GlobalVariable.arrayMobile.get(i));
                                }
                            }
                        }
                        else if(filterPrice.equals("TYPE3")){
                            for(int i = 0;i < GlobalVariable.arrayMobile.size();i++){
                                final int price = GlobalVariable.arrayMobile.get(i).getProductPrice();
                                if(price > 10000000 && price <= 20000000){
                                    mArrayProduct.add(GlobalVariable.arrayMobile.get(i));
                                }
                            }
                        }
                        else if(filterPrice.equals("TYPE4")){
                            for(int i = 0;i < GlobalVariable.arrayMobile.size();i++){
                                final int price = GlobalVariable.arrayMobile.get(i).getProductPrice();
                                if(price > 20000000){
                                    mArrayProduct.add(GlobalVariable.arrayMobile.get(i));
                                }
                            }
                        }
                    }

                    // lọc tất cả sản phẩm laptop + lọc theo giá
                    else if(filterType.equals("LAPTOP")){
                        if(filterPrice.length() == 0){
                            mArrayProduct.addAll(GlobalVariable.arrayLaptop);
                        }
                        else if(filterPrice.equals("TYPE1")){
                            for(int i = 0;i < GlobalVariable.arrayLaptop.size();i++){
                                final int price = GlobalVariable.arrayLaptop.get(i).getProductPrice();
                                if(price <= 5000000){
                                    mArrayProduct.add(GlobalVariable.arrayLaptop.get(i));
                                }
                            }
                        }
                        else if(filterPrice.equals("TYPE2")){
                            for(int i = 0;i < GlobalVariable.arrayLaptop.size();i++){
                                final int price = GlobalVariable.arrayLaptop.get(i).getProductPrice();
                                if(price > 5000000 && price <= 10000000){
                                    mArrayProduct.add(GlobalVariable.arrayLaptop.get(i));
                                }
                            }
                        }
                        else if(filterPrice.equals("TYPE3")){
                            for(int i = 0;i < GlobalVariable.arrayLaptop.size();i++){
                                final int price = GlobalVariable.arrayLaptop.get(i).getProductPrice();
                                if(price > 10000000 && price <= 20000000){
                                    mArrayProduct.add(GlobalVariable.arrayLaptop.get(i));
                                }
                            }
                        }
                        else if(filterPrice.equals("TYPE4")){
                            for(int i = 0;i < GlobalVariable.arrayLaptop.size();i++){
                                final int price = GlobalVariable.arrayLaptop.get(i).getProductPrice();
                                if(price > 20000000){
                                    mArrayProduct.add(GlobalVariable.arrayLaptop.get(i));
                                }
                            }
                        }
                    }


                    productAdapter.notifyDataSetChanged();
                    checkDataReturn(); // kiểm tra có kết qủa trả về hay ko, nếu ko => hiện thông báo

                    filterType = "";
                    filterPrice = "";
                    dialog.dismiss();
                }
            }
        });

        rdAllProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterType = "ALL";
            }
        });

        rdMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterType = "MOBILE";
            }
        });

        rdLaptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterType = "LAPTOP";
            }
        });

        rdPriceType1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterPrice = "TYPE1";
            }
        });

        rdPriceType2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterPrice = "TYPE2";
            }
        });

        rdPriceType3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterPrice = "TYPE3";
            }
        });

        rdPriceType4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterPrice = "TYPE4";
            }
        });

    }

    private void setTextFilterBy() {
        if(filterType.length() == 0 || filterType.equals("ALL")){
            // text : toàn bộ
            cvFilterType.setVisibility(View.VISIBLE);
            tvFilterType.setText(getString(R.string.text_all_product));
        }
        else if(filterType.equals("MOBILE")){
            // text: điện thoại
            cvFilterType.setVisibility(View.VISIBLE);
            tvFilterType.setText(getString(R.string.text_mobile_product));
        }
        else if(filterType.equals("LAPTOP")){
            // text: laptop
            cvFilterType.setVisibility(View.VISIBLE);
            tvFilterType.setText(getString(R.string.text_laptop_product));
        }

        if(filterPrice.length() == 0){
            tvFilterPrice.setText("");
            cvFilterPrice.setVisibility(View.GONE);
        }
        else if(filterPrice.equals("TYPE1")){
            // dưới 5 triệu
            cvFilterPrice.setVisibility(View.VISIBLE);
            tvFilterPrice.setText(getString(R.string.text_filter_price_type1));
        }
        else if(filterPrice.equals("TYPE2")){
            // từ 5 - 10 triệu
            cvFilterPrice.setVisibility(View.VISIBLE);
            tvFilterPrice.setText(getString(R.string.text_filter_price_type2));
        }
        else if(filterPrice.equals("TYPE3")){
            // từ 10 - 20 triệu
            cvFilterPrice.setVisibility(View.VISIBLE);
            tvFilterPrice.setText(getString(R.string.text_filter_price_type3));
        }
        else if(filterPrice.equals("TYPE4")){
            // trên 20 triệu
            cvFilterPrice.setVisibility(View.VISIBLE);
            tvFilterPrice.setText(getString(R.string.text_filter_price_type3));
        }

    }

    private void dialogAnhXa(Dialog dialog) {
        rdAllProduct = dialog.findViewById(R.id.rd_filter_all_product);
        rdMobile = dialog.findViewById(R.id.rd_filter_mobile);
        rdLaptop = dialog.findViewById(R.id.rd_filter_laptop);
        rdPriceType1 = dialog.findViewById(R.id.rd_filter_price_type1);
        rdPriceType2 = dialog.findViewById(R.id.rd_filter_price_type2);
        rdPriceType3 = dialog.findViewById(R.id.rd_filter_price_type3);
        rdPriceType4 = dialog.findViewById(R.id.rd_filter_price_type4);
        btnCancel = dialog.findViewById(R.id.btn_cancel_filter);
        btnSubmit = dialog.findViewById(R.id.btn_submit_filter);


    }

    private void setAdapterRecycleViewProduct() {
        rcvProduct = mView.findViewById(R.id.rcv_product);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity);
        rcvProduct.setLayoutManager(linearLayoutManager);

        productAdapter = new ProductAdapter();
        productAdapter.setData(mArrayProduct, new ProductAdapter.IClickAddToCartListener() {
            @Override
            public void onClickAddToCart(final ImageView imgAddToCart, final Product product) {
                AnimationUtil.translateAnimation(mainActivity.getViewAnimation(), imgAddToCart,
                        mainActivity.getViewEndAnimation(), new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        if(!GlobalVariable.isLogin){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Thông báo");
                            builder.setMessage("Bạn chưa đăng nhập, đăng nhập ngay");

                            builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(Objects.requireNonNull(getActivity()).getApplication(), LoginActivity.class));
                                }
                            });
                            builder.setNegativeButton("Để sau", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {}
                            });

                            builder.show();
                        }else{
                            product.setAddToCart(true);
                            imgAddToCart.setBackgroundResource(R.drawable.bg_gray_corner_6);
                            productAdapter.notifyDataSetChanged();

                            String id = product.getProductID();
                            String typeId = product.getProductTypeID();
                            String name = product.getProductName();
                            String description = product.getProductDescription();
                            int price = product.getProductPrice();
                            String image = product.getProductImage();
                            int sale = product.getSale();

                            Cart cart = new Cart(id,typeId,name,description,price,image,sale,1);
                            GlobalVariable.arrayCart.add(cart);

                            // tăng số lượng sản phẩm giỏ hàng lên 1
                            MainActivity.setCountProductInCart(GlobalVariable.arrayCart.size());
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
        rcvProduct.setAdapter(productAdapter);
    }

    private void anhXa(View mView) {
        sortSpinner = mView.findViewById(R.id.spinner_sort_product_fragment);
        filter = mView.findViewById(R.id.layout_filter);
        tvFilterType = mView.findViewById(R.id.tv_filter_type);
        tvFilterPrice = mView.findViewById(R.id.tv_filter_price);
        cvFilterType = mView.findViewById(R.id.cv_filter_type);
        cvFilterPrice = mView.findViewById(R.id.cv_filter_price);
        noticeNoDataReturn = mView.findViewById(R.id.tv_no_data_return);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.sort));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);

        if(mArrayProduct == null){
            mArrayProduct = new ArrayList<>();
            mArrayProduct.addAll(GlobalVariable.arrayProduct);
        }

        Collections.sort(mArrayProduct, new Comparator<Product>() {
            @Override
            public int compare(Product product1, Product product2) {
                return product1.getProductName().compareTo(product2.getProductName());
            }
        });

    }

    // khong sap xep
    private void noSort(){
        mArrayProduct.clear();
        mArrayProduct.addAll(GlobalVariable.arrayProduct);
        tvFilterPrice.setText("");
        tvFilterType.setText("");
        cvFilterType.setVisibility(View.GONE);
        cvFilterPrice.setVisibility(View.GONE);
        productAdapter.notifyDataSetChanged();
    }

    // sap xep giam dan theo gia
    private void descendingSortPrice(){
        Collections.sort(mArrayProduct, new Comparator<Product>() {
            @Override
            public int compare(Product product1, Product product2) {

                int firstPrice = product1.getProductPrice() - (product1.getProductPrice()/100)*product1.getSale();
                int secondPrice = product2.getProductPrice() - (product2.getProductPrice()/100)*product2.getSale();
                return Integer.compare(secondPrice , firstPrice);
            }
        });
        productAdapter.notifyDataSetChanged();
    }

    // sap xep tang dan theo gia
    private void ascendingSortPrice(){
        Collections.sort(mArrayProduct, new Comparator<Product>() {
            @Override
            public int compare(Product product1, Product product2) {
                int firstPrice = product1.getProductPrice() - (product1.getProductPrice()/100)*product1.getSale();
                int secondPrice = product2.getProductPrice() - (product2.getProductPrice()/100)*product2.getSale();
//                return Integer.compare(product1.getProductPrice() , product2.getProductPrice());
                return Integer.compare(firstPrice , secondPrice);
            }
        });
        productAdapter.notifyDataSetChanged();
    }

    // sap xep giam dan theo danh gia
    private void descendingSortRating(){

        Collections.sort(mArrayProduct, new Comparator<Product>() {
            @Override
            public int compare(Product product1, Product product2) {
                return Float.compare(product2.getRating(),product1.getRating());
            }
        });
        productAdapter.notifyDataSetChanged();

    }

    public static void showAllMobileProduct(){
        mArrayProduct.clear();
        mArrayProduct.addAll(GlobalVariable.arrayMobile);
        productAdapter.notifyDataSetChanged();
    }

    public static void showAllLaptopProduct(){
        mArrayProduct.clear();
        mArrayProduct.addAll(GlobalVariable.arrayLaptop);
        productAdapter.notifyDataSetChanged();
    }


}
