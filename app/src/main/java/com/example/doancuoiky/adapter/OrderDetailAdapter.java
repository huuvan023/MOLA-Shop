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
import android.widget.Toast;

import com.example.doancuoiky.GlobalVariable;
import com.example.doancuoiky.R;
import com.example.doancuoiky.activity.ProductDetailActivity;
import com.example.doancuoiky.fragment.CartFragment;
import com.example.doancuoiky.modal.Cart;
import com.example.doancuoiky.modal.OrderDetail;
import com.example.doancuoiky.modal.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class OrderDetailAdapter extends BaseAdapter{


    Context myContext;
    int myLayout;
    List<OrderDetail> myOrderProductList;
    TextView productNameOrderDetail,productDescriptionOrderDetail,productCountOrderDetail;
    TextView productPriceOrderDetail,tvProductSalePercent, tvProductPriceSale;
    ImageView imgProductOrderDetail;
    Product product;
    Button btnGotoDetail;

    // constructor
    public OrderDetailAdapter(Context context, int layout, List<OrderDetail> orderProductList){
        myContext = context;
        myLayout = layout;
        myOrderProductList = orderProductList;
    }

    @Override
    public int getCount() {
        return myOrderProductList.size();
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

        btnGotoDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ProductDetailActivity.class);
                intent.putExtra("productDetail", myOrderProductList.get(i).getId_product());
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }

    private void anhXa(View view,int i) {

        // Ánh xạ và gán giá trị
        productNameOrderDetail = view.findViewById(R.id.tv_product_name_order_detail);
        productDescriptionOrderDetail = view.findViewById(R.id.tv_product_description_order_detail);
        productPriceOrderDetail = view.findViewById(R.id.tv_product_price_order_detail);
        productCountOrderDetail = view.findViewById(R.id.product_quantity_order_detail);
        imgProductOrderDetail = view.findViewById(R.id.img_product_order_detail);
        btnGotoDetail = view.findViewById(R.id.btn_product_detail_order_detail);
        tvProductSalePercent = view.findViewById(R.id.tv_product_price_sale_percent_order_detail);
        tvProductPriceSale = view.findViewById(R.id.tv_product_price_sale_order_detail);

        String id_product = myOrderProductList.get(i).getId_product();
        for(int index = 0;index < GlobalVariable.arrayProduct.size();index++){
            if(GlobalVariable.arrayProduct.get(index).getProductID().equals(id_product)){
                product = new Product(
                        id_product,
                        GlobalVariable.arrayProduct.get(index).getProductTypeID(),
                        GlobalVariable.arrayProduct.get(index).getProductName(),
                        GlobalVariable.arrayProduct.get(index).getProductDescription(),
                        GlobalVariable.arrayProduct.get(index).getProductPrice(),
                        GlobalVariable.arrayProduct.get(index).getProductImage(),
                        GlobalVariable.arrayProduct.get(index).getRating(),
                        GlobalVariable.arrayProduct.get(index).getSale()
                        );
                break;
            }
        }

        // gán giá trị
        productNameOrderDetail.setText(product.getProductName());
        productDescriptionOrderDetail.setText(product.getProductDescription());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        String _price = decimalFormat.format(product.getProductPrice()) + " đ";
        productPriceOrderDetail.setText(_price);

        int price_sale = (product.getProductPrice() /100) * product.getSale();
        String salePercent = "-" + product.getSale() + "%";
        String sale = decimalFormat.format(product.getProductPrice() - price_sale) + " đ";

        if(product.getSale() == 0){
            tvProductPriceSale.setVisibility(View.GONE);
            tvProductSalePercent.setVisibility(View.GONE);
        }else{
            productPriceOrderDetail.setPaintFlags(productPriceOrderDetail.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            productPriceOrderDetail.setTextColor(Color.rgb(0,0,0));
            productPriceOrderDetail.setTextSize(14f);
            tvProductSalePercent.setText(salePercent);
            tvProductSalePercent.setVisibility(View.VISIBLE);

            tvProductPriceSale.setVisibility(View.VISIBLE);
            tvProductPriceSale.setText(sale);
        }

        String _count = myOrderProductList.get(i).getQuanlity() + "";
        productCountOrderDetail.setText(_count);
        Picasso.get()
                .load(product.getProductImage())
                .into(imgProductOrderDetail);

    }
}
