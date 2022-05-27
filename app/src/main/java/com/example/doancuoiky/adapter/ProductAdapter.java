package com.example.doancuoiky.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancuoiky.modal.Product;
import com.example.doancuoiky.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> mListProduct;
    private IClickAddToCartListener iClickAddToCartListener;
    private IClickGotoDetailListener iClickGotoDetailListener;
    private RelativeLayout relativeLayout;

    // dung interface de callback su kien ra ben ngoai
    public interface IClickAddToCartListener{
        // dinh nghia cho method muon xu ly
        void onClickAddToCart(ImageView imgAddToCart,Product product);
    }

    public interface IClickGotoDetailListener{
        // dinh nghia cho method muon xu ly
        void onClickGotoDetail(int pos);
    }

    public void gotoDetail(IClickGotoDetailListener listener){
        this.iClickGotoDetailListener = listener;
    }

    public void setData(List<Product> list,IClickAddToCartListener listener){
        this.mListProduct = list;
        this.iClickAddToCartListener = listener;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

        final Product product = mListProduct.get(position);
        if(product == null){
            return;
        }

        Picasso.get()
                .load(product.getProductImage())
                .into(holder.imgProduct);
        holder.tvProductName.setText(product.getProductName());
        holder.tvProductDescription.setText(product.getProductDescription());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        String _price = "Giá: " + decimalFormat.format(product.getProductPrice()) +" đ";

        int price_sale = (product.getProductPrice() /100) * product.getSale();
        String salePercent = "-" + product.getSale() + "%";
        String sale = decimalFormat.format(product.getProductPrice() - price_sale) + " đ";

        if(product.getSale() == 0){
            holder.tvProductSalePercent.setVisibility(View.GONE);
            holder.tvProductPrice.setText(_price);
            holder.tvProductPrice.setTextColor(Color.rgb(244,67,54));
            holder.tvProductPrice.setPaintFlags(holder.tvProductPriceSale.getPaintFlags() | Paint.ANTI_ALIAS_FLAG);
            holder.tvProductPriceSale.setVisibility(View.GONE);
        }else{
            holder.tvProductPrice.setText(_price);
            holder.tvProductPrice.setPaintFlags(holder.tvProductPriceSale.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvProductPrice.setTextColor(Color.rgb(0,0,0));
            holder.tvProductPrice.setTextSize(14f);
            holder.tvProductSalePercent.setText(salePercent);
            holder.tvProductSalePercent.setVisibility(View.VISIBLE);

            holder.tvProductPriceSale.setVisibility(View.VISIBLE);
            holder.tvProductPriceSale.setText(sale);
        }

        if(product.isAddToCart()){
            holder.imgAddToCart.setBackgroundResource(R.drawable.bg_gray_corner_6);
        }
        else{
            holder.imgAddToCart.setBackgroundResource(R.drawable.bg_red_corner_6);
        }

        // bat su kien
        holder.imgAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!product.isAddToCart()){
                    iClickAddToCartListener.onClickAddToCart(holder.imgAddToCart,product);
                }
            }
        });

        // den man hinh chi tiet
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyDataSetChanged();
                iClickGotoDetailListener.onClickGotoDetail(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListProduct != null){
            return mListProduct.size();
        }
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgProduct;
        private TextView tvProductName;
        private TextView tvProductDescription;
        private TextView tvProductPrice;
        private ImageView imgAddToCart;
        private TextView tvProductPriceSale;
        private TextView tvProductSalePercent;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.img_product);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductDescription = itemView.findViewById(R.id.tv_product_description);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            imgAddToCart = itemView.findViewById(R.id.img_add_to_cart);
            relativeLayout = itemView.findViewById(R.id.layout_product);
            tvProductPriceSale = itemView.findViewById(R.id.tv_product_price_sale);
            tvProductSalePercent = itemView.findViewById(R.id.tv_product_sale_percent);
        }
    }

}
