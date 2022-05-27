package com.example.doancuoiky.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doancuoiky.GlobalVariable;
import com.example.doancuoiky.R;
import com.example.doancuoiky.adapter.OrderDetailAdapter;
import com.example.doancuoiky.adapter.PhotoAdapter;
import com.example.doancuoiky.adapter.PhotoProductAdapter;
import com.example.doancuoiky.adapter.ProductAdapter;
import com.example.doancuoiky.fragment.CartFragment;
import com.example.doancuoiky.fragment.ProductFragment;
import com.example.doancuoiky.modal.Cart;
import com.example.doancuoiky.modal.Order;
import com.example.doancuoiky.modal.OrderDetail;
import com.example.doancuoiky.modal.Photo;
import com.example.doancuoiky.modal.PhotoProduct;
import com.example.doancuoiky.modal.Product;
import com.example.doancuoiky.modal.RatingAndComment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView tvProductName, tvProductPrice, tvDescription, tvProductPriceSale, tvProductSalePercent;
    private ImageView goBack;
    Toolbar toolbar;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private Button addToCart;
    private int pos = 0;
    ArrayAdapter<Product> adapter;
    ListView description;
    ArrayList<String> arraySpecifications;
    int indexProductInCart = -1;
    Button btnGotoRatingAndComment;

    TextView tvRatingPercent, tvTotalRate, tvOneStar, tvTwoStar, tvThreeStar, tvFourStar, tvFiveStar;
    int oneStar = 0, twoStar = 0, threeStar = 0, fourStar = 0, fiveStar = 0, totalRate = 0;
    RatingBar rtbInComment;
    LinearLayout layoutAddRatingAndComment;
    RatingBar rtbAddRating,rtbTotal;
    EditText edtAddComment;
    Button btnAddRatingAndComment;
    TextView tvQuantityComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        anhXa();

        setSupportActionBar(toolbar);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        final Intent intent = getIntent();
        String idProduct = intent.getStringExtra("productDetail");

        for (int ii = 0; ii < GlobalVariable.arrayProduct.size(); ii++) {
            if (GlobalVariable.arrayProduct.get(ii).getProductID().equals(idProduct)) {
                pos = ii;
                break;
            }
        }

        final String id_product = GlobalVariable.arrayProduct.get(pos).getProductID();
        setRateData(id_product);

        if(GlobalVariable.isLogin) {
            // kiểm tra xem sản phẩm đã mua hay chưa, nếu đã mua mới đc đánh giá + cmt
            checkProductIsPurchase(id_product);
        }

        btnAddRatingAndComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddSubmitRatingAndComment(id_product);
            }
        });

        onAddToCart(pos);
        btnGotoRatingAndComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ProductDetailActivity.this, ListRatingActivity.class);
                intent1.putExtra("gotoRatingAndComment",id_product);
                startActivity(intent1);
            }
        });


        setPhotoAdapter(GlobalVariable.arrayProduct.get(pos).getProductID());
        setDataSpecifications(GlobalVariable.arrayProduct.get(pos).getProductID());

        tvProductName.setText(GlobalVariable.arrayProduct.get(pos).getProductName());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        String _price = decimalFormat.format(GlobalVariable.arrayProduct.get(pos).getProductPrice()) + " đ";


        int price_sale = (GlobalVariable.arrayProduct.get(pos).getProductPrice() /100) * GlobalVariable.arrayProduct.get(pos).getSale();

        String salePercent = "-" + GlobalVariable.arrayProduct.get(pos).getSale() + "%";
        String sale = decimalFormat.format(GlobalVariable.arrayProduct.get(pos).getProductPrice()- price_sale) + " đ";

        tvProductPrice.setText(_price);

        if(GlobalVariable.arrayProduct.get(pos).getSale() == 0){
            tvProductPriceSale.setVisibility(View.GONE);
            tvProductSalePercent.setVisibility(View.GONE);
        }else{
            tvProductPrice.setPaintFlags(tvProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvProductPrice.setTextColor(Color.rgb(0,0,0));
            tvProductPrice.setTextSize(14f);
            tvProductSalePercent.setText(salePercent);
            tvProductSalePercent.setVisibility(View.VISIBLE);


            tvProductPriceSale.setVisibility(View.VISIBLE);
            tvProductPriceSale.setText(sale);
        }

        tvDescription.setText(GlobalVariable.arrayProduct.get(pos).getProductDescription());

    }

    private void onAddToCart(final int pos) {
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!GlobalVariable.isLogin) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Bạn chưa đăng nhập, đăng nhập ngay ?");

                    builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(ProductDetailActivity.this, LoginActivity.class));
                        }
                    });

                    builder.setNegativeButton("Để sau", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });

                    builder.show();
                } else {
                    // them vao gio hang
                    GlobalVariable.arrayProduct.get(pos).setAddToCart(true);
                    ProductFragment.productAdapter.notifyDataSetChanged();

                    String id = GlobalVariable.arrayProduct.get(pos).getProductID();
                    String typeId = GlobalVariable.arrayProduct.get(pos).getProductTypeID();
                    String name = GlobalVariable.arrayProduct.get(pos).getProductName();
                    String description = GlobalVariable.arrayProduct.get(pos).getProductDescription();
                    int price = GlobalVariable.arrayProduct.get(pos).getProductPrice();
                    String image = GlobalVariable.arrayProduct.get(pos).getProductImage();
                    int sale =  GlobalVariable.arrayProduct.get(pos).getSale();

                    if (GlobalVariable.arrayCart.size() > 0) {
                        for (int i = 0; i < GlobalVariable.arrayCart.size(); i++) {
                            if (GlobalVariable.arrayCart.get(i).getID().equals(id)) {
                                indexProductInCart = i;
                                break;
                            }
                        }
                    }
                    if (indexProductInCart == -1) {
                        Cart cart = new Cart(id, typeId, name, description, price, image,sale, 1);
                        GlobalVariable.arrayCart.add(cart);
                        MainActivity.setCountProductInCart(GlobalVariable.arrayCart.size());
                    } else {
                        Cart cart = GlobalVariable.arrayCart.get(indexProductInCart);

                        int newCount = cart.getCount();
                        if (cart.getCount() < 10) {
                            newCount += 1;
                        }
                        GlobalVariable.arrayCart.set(indexProductInCart, new Cart(id, typeId, name, description, price, image,sale, newCount));
                        CartFragment.updateTotalPrice();
                        CartFragment.cartAdapter.notifyDataSetChanged();
                    }
                    Toast.makeText(ProductDetailActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void anhXa() {
        btnGotoRatingAndComment = findViewById(R.id.btn_goto_comment_and_rating);
        layoutAddRatingAndComment = findViewById(R.id.layout_add_rating_and_comment);
        tvProductName = findViewById(R.id.product_detail_name);
        tvProductPrice = findViewById(R.id.product_detail_price);
        tvProductPriceSale = findViewById(R.id.product_detail_price_sale);
        goBack = findViewById(R.id.iv_back_product_detail);
        toolbar = findViewById(R.id.toolbar_product_detail);
        viewPager = findViewById(R.id.view_pager_photo_detail_photo);
        circleIndicator = findViewById(R.id.circle_indicator_detail);
        addToCart = findViewById(R.id.btn_add_product_to_cart);
        tvDescription = findViewById(R.id.tv_description_detail_activity);
        description = findViewById(R.id.lv_description);
        tvProductSalePercent = findViewById(R.id.product_detail_sale_percent);

        rtbTotal = findViewById(R.id.rtb_total_rating_product_title);
        tvQuantityComment = findViewById(R.id.tv_quantity_rating_product_title);
        tvRatingPercent = findViewById(R.id.rate_percent_detail_activity);
        tvTotalRate = findViewById(R.id.number_of_comment);
        tvOneStar = findViewById(R.id.tv_one_star);
        tvTwoStar = findViewById(R.id.tv_two_star);
        tvThreeStar = findViewById(R.id.tv_three_star);
        tvFourStar = findViewById(R.id.tv_four_star);
        tvFiveStar = findViewById(R.id.tv_five_star);
        rtbInComment = findViewById(R.id.rating_bar_in_comment);

        rtbAddRating = findViewById(R.id.rtg_add_rating_product_detail);
        edtAddComment = findViewById(R.id.edt_input_add_comment_product_detail);
        btnAddRatingAndComment = findViewById(R.id.btn_add_rating_and_comment);

        description.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                view.onTouchEvent(motionEvent);
                return true;
            }
        });

    }

    private void setDataSpecifications(final String idProduct) {
        arraySpecifications = new ArrayList<>();

        final StringRequest request = new StringRequest(StringRequest.Method.GET, GlobalVariable.PRODUCT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray data = object.getJSONArray("data");
                    int position = 0;
                    for (int index = 0; index < data.length(); index++) {
                        JSONObject result = (JSONObject) data.get(index);
                        String id = result.getString("id_product");
                        if (idProduct.equals(id)) {
                            position = index;
                        }
                    }
                    JSONObject result = (JSONObject) data.get(position);
                    JSONArray specifications = result.getJSONArray("specifications");
                    for (int i = 0; i < specifications.length(); i++) {
                        arraySpecifications.add(specifications.get(i).toString());
                    }
                    adapter = new ArrayAdapter(ProductDetailActivity.this, android.R.layout.simple_list_item_1, arraySpecifications);
                    description.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(ProductDetailActivity.this);
        queue.add(request);
    }


    private void setPhotoAdapter(final String idProduct) {
        final ArrayList<PhotoProduct> imgss = new ArrayList<>();
        StringRequest request = new StringRequest(StringRequest.Method.POST, GlobalVariable.PRODUCT_IMAGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray data = object.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject img = (JSONObject) data.get(i);
                                String image = img.getString("image");
                                imgss.add(new PhotoProduct(image));
                            }

                            PhotoProductAdapter photoAdapter = new PhotoProductAdapter(ProductDetailActivity.this, imgss);
                            viewPager.setAdapter(photoAdapter);
                            circleIndicator.setViewPager(viewPager);
                            photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("DATA1", "error1: " + e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DATA1", "error2: " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("productID", idProduct);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(ProductDetailActivity.this);
        queue.add(request);

    }


    // them icon gio hang vao thanh toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // su kien khi icon gio hang tren thanh toolbar dc click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.ic_cart_toolbar) {   //this item has your app icon
            Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class);
            intent.putExtra("gotoCart", "cart");

            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRateData(final String id_product) {
        StringRequest request = new StringRequest(StringRequest.Method.POST, GlobalVariable.GET_PRODUCT_RATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray data = object.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = (JSONObject) data.get(i);
                        int rate = Integer.parseInt(obj.getString("rate"));
                        String idProduct = obj.getString("id_product");
                        String idUser = obj.getString("id_user");
                        String comment = obj.getString("cmt");
                        float rating = Float.parseFloat(obj.getString("rate"));

                        if(GlobalVariable.isLogin) {
                            if (idProduct.equals(id_product) &&
                                    idUser.equals(GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_ID_USER))) {
                                rtbAddRating.setRating(rating);
                                edtAddComment.setText(comment);
                            }
                        }

                        switch (rate) {
                            case 1:
                                oneStar++;
                                break;
                            case 2:
                                twoStar++;
                                break;
                            case 3:
                                threeStar++;
                                break;
                            case 4:
                                fourStar++;
                                break;
                            case 5:
                                fiveStar++;
                                break;
                        }
                        totalRate += rate;
                    }

                    tvOneStar.setText(String.valueOf(oneStar));
                    tvTwoStar.setText(String.valueOf(twoStar));
                    tvThreeStar.setText(String.valueOf(threeStar));
                    tvFourStar.setText(String.valueOf(fourStar));
                    tvFiveStar.setText(String.valueOf(fiveStar));

                    // số người đánh giá
                    int numberOfRate = oneStar + twoStar + threeStar + fourStar + fiveStar;
                    String _quantity = "(" + numberOfRate + " đánh giá)";
                    tvQuantityComment.setText(_quantity);
                    String totalComment = numberOfRate + " nhận xét";

                    tvTotalRate.setText(totalComment);
                    if(numberOfRate == 0){
                        tvRatingPercent.setText("0");
                        rtbInComment.setRating(0);
                    }else{
                        float percent = (float)totalRate/numberOfRate;
                        tvRatingPercent.setText(String.valueOf(percent));

                        rtbTotal.setRating(percent);
                        rtbInComment.setRating(percent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAGTEST", "onErrorResponse: ");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_product", id_product);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(ProductDetailActivity.this);
        queue.add(request);
    }


    // kiểm tra xem sản phẩm đã mua hay chưa, nếu đã mua mới đc đánh giá + cmt
    private void checkProductIsPurchase(final String id_product){
        StringRequest request = new StringRequest(StringRequest.Method.GET, GlobalVariable.GET_ORDER_URL,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray data = object.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject dataObject = (JSONObject) data.get(i);
                        String id_user = dataObject.getString("id_user");

                        if(GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_ID_USER).equals(id_user)){
                            final String id_bill = dataObject.getString("id_bill");
                            RequestQueue queue1 = Volley.newRequestQueue(ProductDetailActivity.this);
                            StringRequest request1 = new StringRequest(StringRequest.Method.POST,
                                    GlobalVariable.GET_ORDER_DETAIL_URL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject object1 = new JSONObject(response);
                                        JSONArray data = object1.getJSONArray("data");
                                        for(int j = 0;j < data.length(); j++){
                                            JSONObject obj1 = (JSONObject) data.get(j);
                                            String idProduct = obj1.getString("id_product");
                                            if(idProduct.equals(id_product)){
                                                layoutAddRatingAndComment.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            }){
                                @Override
                                public Map<String, String> getHeaders() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("Authorization",GlobalVariable.TOKEN);
                                    return params;
                                }

                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("id_bill",id_bill);
                                    return params;
                                }
                            };
                            queue1.add(request1);

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", GlobalVariable.TOKEN);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(ProductDetailActivity.this);
        queue.add(request);
    }

    private void onAddRatingAndComment(final String id_product){
        StringRequest request = new StringRequest(StringRequest.Method.POST, GlobalVariable.ADD_RATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject result = object.getJSONObject("result");
                    int code = Integer.parseInt(result.getString("code"));
                    if(code == 0){
                        Toast.makeText(ProductDetailActivity.this, "Thêm nhận xét thành công", Toast.LENGTH_SHORT).show();
                        setRateData(id_product);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization",GlobalVariable.TOKEN);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_product",id_product);
                params.put("rate",rtbAddRating.getRating()+"");
                params.put("cmt",edtAddComment.getText().toString());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(ProductDetailActivity.this);
        queue.add(request);
    }

    private void onUpdateRatingAndComment(final String id_product){
        StringRequest request = new StringRequest(StringRequest.Method.POST, GlobalVariable.UPDATE_RATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject result = object.getJSONObject("result");
                    int code = Integer.parseInt(result.getString("code"));
                    if(code == 0){
                        Toast.makeText(ProductDetailActivity.this, "Cập nhật nhận xét thành công", Toast.LENGTH_SHORT).show();
                        setRateData(id_product);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization",GlobalVariable.TOKEN);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_product",id_product);
                params.put("rate",rtbAddRating.getRating()+"");
                params.put("cmt",edtAddComment.getText().toString());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(ProductDetailActivity.this);
        queue.add(request);
    }

    private void onAddSubmitRatingAndComment(final String id_product){
        StringRequest request = new StringRequest(StringRequest.Method.POST,
                GlobalVariable.GET_PRODUCT_RATE_URL, new Response.Listener<String>() {
            boolean flag = false;
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray data = object.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = (JSONObject) data.get(i);
                        String idUser = obj.getString("id_user");
                        if(idUser.equals(GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_ID_USER))){
                            flag = true;
                        }
                    }

                    if(flag){
                        //cap nhat danh gia
                        onUpdateRatingAndComment(id_product);
                    }else {
                        //them danh gia moi
                        onAddRatingAndComment(id_product);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAGTEST", "onErrorResponse: ");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_product", id_product);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(ProductDetailActivity.this);
        queue.add(request);
    }


}