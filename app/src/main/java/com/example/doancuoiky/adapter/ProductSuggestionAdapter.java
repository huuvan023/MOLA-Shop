package com.example.doancuoiky.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancuoiky.R;
import com.example.doancuoiky.modal.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductSuggestionAdapter extends RecyclerView.Adapter<ProductSuggestionAdapter.ItemHolder> {

    private IClickGotoProductDetail iClickGotoProductDetail;

    // dung interface de callback su kien ra ben ngoai -> Fragment Home
    public interface IClickGotoProductDetail{
        // dinh nghia cho method muon xu ly
        void onClickGotoDetail(String idProduct); // truyền vào index -> vị trí cần đến
    }

    public void onGotoDetail(IClickGotoProductDetail listener){
        this.iClickGotoProductDetail = listener;
    }

    Context context;
    ArrayList<Product> arrayProductSearch;


    public ProductSuggestionAdapter(Context context, ArrayList<Product> arrayProductSearch) {
        this.context = context;
        this.arrayProductSearch = arrayProductSearch;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_suggestion,null);
        ItemHolder itemHolder = new ItemHolder(view);

        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position){
        Product productNew = arrayProductSearch.get(position);

        holder.tvProductName.setText(productNew.getProductName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
//        String _format_price = "Giá: " + decimalFormat.format(productNew.getProductPrice())+ " đ";
//        holder.tvProductPrice.setText(_format_price);


        String _price = "Giá: " + decimalFormat.format(productNew.getProductPrice())+ " đ";

        int price_sale = (productNew.getProductPrice() /100) * productNew.getSale();
        String salePercent = "-" + productNew.getSale() + "%";
        String sale = decimalFormat.format(productNew.getProductPrice() - price_sale) + " đ";

        holder.tvProductPrice.setText(_price);

        if(price_sale > 0){
            holder.tvProductPrice.setPaintFlags(holder.tvProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvProductPrice.setTextColor(Color.rgb(0,0,0));
            holder.tvProductPrice.setTextSize(14f);
            holder.tvProductSalePercent.setText(salePercent);
            holder.tvProductSalePercent.setVisibility(View.VISIBLE);

            holder.tvProductPriceSale.setVisibility(View.VISIBLE);
            holder.tvProductPriceSale.setText(sale);
        }else{
            holder.tvProductPriceSale.setVisibility(View.GONE);
            holder.tvProductSalePercent.setVisibility(View.GONE);
        }

        Picasso.get()
                .load(productNew.getProductImage())
                .into(holder.imgProduct);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickGotoProductDetail.onClickGotoDetail(arrayProductSearch.get(position).getProductID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayProductSearch.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public ImageView imgProduct;
        public TextView tvProductName, tvProductDescription, tvProductPrice,tvProductPriceSale, tvProductSalePercent;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product_image_product_suggestion);
            tvProductName = itemView.findViewById(R.id.tv_product_name_product_suggestion);
            tvProductDescription = itemView.findViewById(R.id.tv_product_description_product_suggestion);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price_product_suggestion);
            tvProductPriceSale = itemView.findViewById(R.id.tv_product_price_sale_product_suggestion);
            tvProductSalePercent = itemView.findViewById(R.id.tv_product_sale_percent_product_suggestion);
        }
    }
}
