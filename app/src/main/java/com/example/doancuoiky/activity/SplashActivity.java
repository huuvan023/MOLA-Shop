package com.example.doancuoiky.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doancuoiky.GlobalVariable;
import com.example.doancuoiky.R;
import com.example.doancuoiky.modal.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private Button btnGotoLogin, btnGotoMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        anhXa();
        loadDataFromServer();

        setOnClick();


    }

    private void setOnClick() {
        btnGotoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        });
        btnGotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        });
    }

    private void anhXa() {
        btnGotoLogin = findViewById(R.id.btn_goto_login);
        btnGotoMain = findViewById(R.id.btn_goto_main);

        if (GlobalVariable.arrayProduct == null) {
            GlobalVariable.arrayProduct = new ArrayList<>();
        }
    }

    public void loadDataFromServer() {
        GlobalVariable.arrayProduct.clear();
        StringRequest request = new StringRequest(StringRequest.Method.GET, GlobalVariable.PRODUCT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray data = object.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {

                                JSONObject result = (JSONObject) data.get(i);

                                final String id_product = result.getString("id_product");
                                final String product_name = result.getString("product_name");
                                final String price = result.getString("price");
                                final String product_type = result.getString("product_type");
                                final String desciption = result.getString("desciption");
                                final String sale = result.getString("sale");
                                final String productImage = result.getString("link");

                                StringRequest request2 = new StringRequest(StringRequest.Method.POST,
                                        GlobalVariable.GET_PRODUCT_RATE_URL, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject object = new JSONObject(response);
                                            JSONArray data = object.getJSONArray("data");
                                            int oneStar = 0, twoStar = 0, threeStar = 0, fourStar = 0, fiveStar = 0, totalRate = 0, numberOfRate = 0;
                                            for (int i = 0; i < data.length(); i++) {
                                                JSONObject obj = (JSONObject) data.get(i);

                                                int rate = Integer.parseInt(obj.getString("rate"));

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

                                            // số người đánh giá
                                            numberOfRate = oneStar + twoStar + threeStar + fourStar + fiveStar;


                                            if (numberOfRate == 0) {
                                                if (sale.equals("null") || sale.equals("0")) {
                                                    GlobalVariable.arrayProduct.add(new Product(id_product, product_type, product_name,
                                                            desciption, Integer.parseInt(price), productImage, 0f, 0));
                                                } else {
                                                    GlobalVariable.arrayProduct.add(new Product(id_product, product_type, product_name,
                                                            desciption, Integer.parseInt(price), productImage, 0f, Integer.parseInt(sale)));
                                                }
                                            } else {
                                                float percent = (float) totalRate / numberOfRate;
                                                if (sale.equals("null") || sale.equals("0")) {
                                                    GlobalVariable.arrayProduct.add(new Product(id_product, product_type, product_name,
                                                            desciption, Integer.parseInt(price), productImage, percent, 0));
                                                } else {
                                                    GlobalVariable.arrayProduct.add(new Product(id_product, product_type, product_name,
                                                            desciption, Integer.parseInt(price), productImage, percent, Integer.parseInt(sale)));
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
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("id_product", id_product);
                                        return params;
                                    }
                                };
                                RequestQueue queue2 = Volley.newRequestQueue(SplashActivity.this);
                                queue2.add(request2);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        RequestQueue queue = Volley.newRequestQueue(SplashActivity.this);
        queue.add(request);
    }

}


