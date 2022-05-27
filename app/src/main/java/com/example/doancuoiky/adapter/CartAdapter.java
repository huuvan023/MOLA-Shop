package com.example.doancuoiky.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.doancuoiky.GlobalVariable;
import com.example.doancuoiky.R;
import com.example.doancuoiky.activity.ProductDetailActivity;
import com.example.doancuoiky.fragment.CartFragment;
import com.example.doancuoiky.modal.Cart;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends BaseAdapter{

    private IClickOnDeleteProductInCart iClickOnDeleteProductInCart;

    // dung interface de callback su kien ra ben ngoai -> Fragment Cart
    public interface IClickOnDeleteProductInCart{
        // dinh nghia cho method muon xu ly
        void onClickDeleteProductInCart(int index); // truyền vào index -> vị trí cần xóa
    }

    public void onDelete(IClickOnDeleteProductInCart listener){
        this.iClickOnDeleteProductInCart = listener;
    }

    Context myContext;
    int myLayout;
    List<Cart> arrayCart;
    TextView cartProductName,cartProductDescription,cartProductPrice,cartProductCount,cartProductPriceSale, tvProductSalePercent;
    Button btnMinus,btnPlus,btnDeleteProduct,btnDetail;
    ImageView imgCartProduct;



    // constructor
    public CartAdapter(Context context,int layout, List<Cart> cartList){
        myContext = context;
        myLayout = layout;
        arrayCart = cartList;
    }

    @Override
    public int getCount() {
        return arrayCart.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(myLayout,null);

        anhXa(view,i);


        // nút giảm số lượng
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentCount = arrayCart.get(i).getCount();

                if(currentCount > 1){
                    currentCount -= 1;
                }

                GlobalVariable.arrayCart.get(i).setCount(currentCount);
                String _count_minus = arrayCart.get(i).getCount() +  "";
                cartProductCount.setText(_count_minus);
                CartFragment.updateTotalPrice();
                notifyDataSetChanged();

            }

        });

        // nút tăng số lượng
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentCount = arrayCart.get(i).getCount();
                currentCount += 1;
                GlobalVariable.arrayCart.get(i).setCount(currentCount);
                String _count_plus = arrayCart.get(i).getCount() + "";
                cartProductCount.setText(_count_plus);
                CartFragment.updateTotalPrice();
                notifyDataSetChanged();
            }
        });

        // nút xóa sản phẩm khỏi giỏ hàng
        btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                iClickOnDeleteProductInCart.onClickDeleteProductInCart(i);
            }
        });

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = GlobalVariable.arrayCart.get(i).getID();

                Intent intent = new Intent(view.getContext(), ProductDetailActivity.class);
                intent.putExtra("productDetail",id);
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }

    private void anhXa(View view,int i) {
        // Ánh xạ và gán giá trị
        cartProductName = view.findViewById(R.id.tv_cart_product_name);
        cartProductDescription = view.findViewById(R.id.tv_cart_product_description);
        cartProductPrice = view.findViewById(R.id.tv_cart_product_price);
        cartProductCount = view.findViewById(R.id.tv_cart_product_count);
        btnPlus = view.findViewById(R.id.btn_cart_plus);
        btnMinus = view.findViewById(R.id.btn_cart_minus);
        btnDeleteProduct = view.findViewById(R.id.btn_delete_product);
        imgCartProduct = view.findViewById(R.id.img_cart_product);
        btnDetail = view.findViewById(R.id.btn_cart_detail);
        cartProductPriceSale = view.findViewById(R.id.tv_cart_product_price_sale);
        tvProductSalePercent = view.findViewById(R.id.tv_cart_product_sale_percent);

        // gán giá trị
        cartProductName.setText(arrayCart.get(i).getName());
        cartProductDescription.setText(arrayCart.get(i).getDescription());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        String _price = "Giá: "  + decimalFormat.format(arrayCart.get(i).getPrice()) + " đ";
        cartProductPrice.setText(_price);

        String _count = arrayCart.get(i).getCount() + "";
        cartProductCount.setText(_count);
        Picasso.get()
                .load(arrayCart.get(i).getCartProductImg())
                .into(imgCartProduct);


        int price_sale = (arrayCart.get(i).getPrice() /100) * arrayCart.get(i).getSale();
        String salePercent = "-" + arrayCart.get(i).getSale() + "%";
        String sale = decimalFormat.format(arrayCart.get(i).getPrice()- price_sale) + " đ";

        cartProductPrice.setText(_price);

        if(arrayCart.get(i).getSale() == 0){
            cartProductPriceSale.setVisibility(View.GONE);
            tvProductSalePercent.setVisibility(View.GONE);
        }else{
            cartProductPrice.setPaintFlags(cartProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            cartProductPrice.setTextColor(Color.rgb(0,0,0));
            cartProductPrice.setTextSize(14f);
            tvProductSalePercent.setText(salePercent);
            tvProductSalePercent.setVisibility(View.VISIBLE);

            cartProductPriceSale.setVisibility(View.VISIBLE);
            cartProductPriceSale.setText(sale);

        }


        int currentCount = arrayCart.get(i).getCount();
        if(currentCount <= 1){
            btnMinus.setEnabled(false);
            notifyDataSetChanged();
        }
        else if(currentCount >= 10){
            btnPlus.setEnabled(false);
            notifyDataSetChanged();
        }
    }
}
