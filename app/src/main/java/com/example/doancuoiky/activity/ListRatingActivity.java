package com.example.doancuoiky.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doancuoiky.GlobalVariable;
import com.example.doancuoiky.R;
import com.example.doancuoiky.adapter.RatingAndCommentAdapter;
import com.example.doancuoiky.modal.RatingAndComment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListRatingActivity extends AppCompatActivity {

    ListView lvRatingAndComment;
    List<RatingAndComment> mListRateAndComment;
    ImageView ivGoBack;
    RelativeLayout layoutNoDataReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rating);

        anhXa();
        ivGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        Intent intent = getIntent();
        String id_product = intent.getStringExtra("gotoRatingAndComment");

        setDataRatingAndComment(id_product);


    }

    private void anhXa() {
        lvRatingAndComment = findViewById(R.id.lv_rating_and_comment);
        ivGoBack = findViewById(R.id.iv_back_rating_and_comment);
        layoutNoDataReturn = findViewById(R.id.layout_no_rating_and_comment);
        if (mListRateAndComment == null) {
            mListRateAndComment = new ArrayList<>();
        }
    }

    private void setDataRatingAndComment(final String id_product) {
        mListRateAndComment.clear();
        StringRequest request = new StringRequest(StringRequest.Method.POST, GlobalVariable.GET_PRODUCT_RATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray data = object.getJSONArray("data");
                    if (data.length() == 0) {
                        layoutNoDataReturn.setVisibility(View.VISIBLE);
                    } else {
                        layoutNoDataReturn.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = (JSONObject) data.get(i);
                        String id_user = obj.getString("id_user");
                        int rate = Integer.parseInt(obj.getString("rate"));
                        String ratedate = obj.getString("ratedate");
                        String comment = obj.getString("cmt");

                        mListRateAndComment.add(new RatingAndComment(
                                id_user,
                                id_product,
                                rate,
                                ratedate,
                                comment
                        ));

                    }
                    RatingAndCommentAdapter adapter = new RatingAndCommentAdapter(ListRatingActivity.this,
                            R.layout.item_rating_and_comment, mListRateAndComment);
                    lvRatingAndComment.setAdapter(adapter);


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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_product", id_product);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(ListRatingActivity.this);
        queue.add(request);
    }

}