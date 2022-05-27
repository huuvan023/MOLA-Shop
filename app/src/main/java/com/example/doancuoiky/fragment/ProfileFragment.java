package com.example.doancuoiky.fragment;


import android.content.Context;
import android.content.Intent;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doancuoiky.GlobalVariable;
import com.example.doancuoiky.R;

import com.example.doancuoiky.activity.ChangeInfoActivity;
import com.example.doancuoiky.activity.ChangePasswordActivity;

import com.example.doancuoiky.activity.LoginActivity;

import com.example.doancuoiky.activity.MainActivity;
import com.example.doancuoiky.activity.OrderManagementActivity;
import com.example.doancuoiky.modal.Order;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ProfileFragment extends Fragment {

    private LinearLayout changeInfo, changePassword, profileContainer, toggleProfile;
    private boolean isExpand = true; // biến check xem profile là expand hay collapse
    private ImageView imgToggleProfile;
    private ImageView imgAvatar;

    private TextView titleName, titleEmail, titleMemberSince;
    private TextView textName, textEmail, textBirthday, textGender, textCitizenIdentification, textAddress, textPhone;

    private LinearLayout profileNotLoggedIn, profileLogged, changeInfoPassword, gotoOrderManagement;
    private LinearLayout gotoPendingOrder, gotoShippingOrder, gotoSuccessOrder;
    private TextView profileIconNext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        anhXa(view);
        checkIsLogin();

        setOnClick();

        setData();

        setDataUserOrder(getContext());

        return view;
    }

    private void setData() {
        if (GlobalVariable.isLogin) {
            String name = GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_USER_NAME);
            String email = GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_EMAIL);
            String memberSince = GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_ACC_CREATE);
            String gender = GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_GENDER);
            String citizen_identification = GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_CITIZEN_IDENTIFICATION);
            String address = GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_ADDRESS);
            String phone = GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_PHONE_NUMBER);
            String birthday = GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_BIRTHDAY);
            String avatar = GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_AVATAR);

            if(avatar.length() > 0){
                String PACKAGE_NAME = Objects.requireNonNull(getContext()).getPackageName();
                int avatarName = getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + avatar,
                        null,null);
                imgAvatar.setImageResource(avatarName);
            }
            titleName.setText(name);
            titleEmail.setText(email);

            if (memberSince.length() > 0) {
                String year = memberSince.substring(0, 4);
                String month = memberSince.substring(5, 7);
                String day = memberSince.substring(8, 10);
                String formatDateMemberSince = getString(R.string.text_logged_member_since) + " " +
                        day + '/' + month + '/' + year;
                titleMemberSince.setText(formatDateMemberSince);
            }

            textName.setText(name);
            textEmail.setText(email);
            if (gender.equals("0")) {
                textGender.setText(getString(R.string.male));
            } else if (gender.equals("1")) {
                textGender.setText(getString(R.string.female));
            }
            textCitizenIdentification.setText(citizen_identification);
            textAddress.setText(address);
            textPhone.setText(phone);

            if(birthday.length() == 0 || birthday.equals("null")){
                textBirthday.setText("");
            }else {
                textBirthday.setText(GlobalVariable.formatDateInVN(birthday));
            }
        }
    }

    private void setOnClick() {
        profileNotLoggedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoLogin();
            }
        });

        toggleProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExpand) {
                    isExpand = false;
                    profileContainer.setVisibility(View.VISIBLE);
                    imgToggleProfile.setImageResource(R.drawable.icon_down);
                } else {
                    isExpand = true;
                    profileContainer.setVisibility(View.GONE);
                    imgToggleProfile.setImageResource(R.drawable.icon_right);
                }
            }
        });

        gotoOrderManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalVariable.isLogin) {
                    gotoOrderManagement("all");
                } else {
                    gotoLogin();
                }
            }
        });

        gotoPendingOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalVariable.isLogin) {
                    gotoOrderManagement("pending");
                } else {
                    gotoLogin();
                }
            }
        });

        gotoShippingOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalVariable.isLogin) {
                    gotoOrderManagement("shipping");
                } else {
                    gotoLogin();
                }
            }
        });

        gotoSuccessOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalVariable.isLogin) {
                    gotoOrderManagement("success");
                } else {
                    gotoLogin();
                }
            }
        });

        changeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(),
                        ChangeInfoActivity.class);
                startActivity(intent);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(),
                        ChangePasswordActivity.class);

                startActivity(intent);
            }
        });
    }

    private void gotoOrderManagement(String value) {
        Intent intent = new Intent(getActivity().getApplicationContext(), OrderManagementActivity.class);

        switch (value) {
            case "all":
                intent.putExtra("gotoOrderManagement", "all");
                break;
            case "pending":
                intent.putExtra("gotoOrderManagement", "pending");
                break;
            case "shipping":
                intent.putExtra("gotoOrderManagement", "shipping");
                break;
            case "success":
                intent.putExtra("gotoOrderManagement", "success");
                break;
        }
        startActivity(intent);
    }

    private void gotoLogin() {
        Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(),
                LoginActivity.class);
        startActivity(intent);
    }

    private void anhXa(View view) {

        changeInfo = view.findViewById(R.id.change_info);
        changePassword = view.findViewById(R.id.change_password);
        profileContainer = view.findViewById(R.id.layout_profile_container);
        toggleProfile = view.findViewById(R.id.show_hide_profile);
        imgToggleProfile = view.findViewById(R.id.img_show_hide_profile);
        imgAvatar = view.findViewById(R.id.img_avatar_profile_fragment);
        titleName = view.findViewById(R.id.title_name_logged_profile_fragment);
        titleEmail = view.findViewById(R.id.title_email_logged_profile_fragment);
        titleMemberSince = view.findViewById(R.id.title_member_since_logged_profile_fragment);

        textName = view.findViewById(R.id.tv_name_profile_fragment);
        textEmail = view.findViewById(R.id.tv_email_profile_fragment);
        textBirthday = view.findViewById(R.id.tv_birthday_profile_fragment);
        textGender = view.findViewById(R.id.tv_gender_profile_fragment);
        textCitizenIdentification = view.findViewById(R.id.tv_citizen_identification_profile_fragment);
        textAddress = view.findViewById(R.id.tv_address_profile_fragment);
        textPhone = view.findViewById(R.id.tv_phone_number_profile_fragment);

        profileNotLoggedIn = view.findViewById(R.id.profile_not_logged_in);
        profileLogged = view.findViewById(R.id.profile_logged);
        changeInfoPassword = view.findViewById(R.id.chane_info_password);
        profileIconNext = view.findViewById(R.id.icon_next_profile_fragment);

        gotoOrderManagement = view.findViewById(R.id.oder_management_profile_fragment);
        gotoPendingOrder = view.findViewById(R.id.layout_oder_pending_profile_fragment);
        gotoShippingOrder = view.findViewById(R.id.layout_oder_shipping_profile_fragment);
        gotoSuccessOrder = view.findViewById(R.id.layout_oder_success_profile_fragment);


    }

    private void checkIsLogin() {
        if (GlobalVariable.isLogin) {
            profileNotLoggedIn.setVisibility(View.GONE);
            profileLogged.setVisibility(View.VISIBLE);
            toggleProfile.setVisibility(View.VISIBLE);
            changeInfoPassword.setVisibility(View.VISIBLE);
            profileIconNext.setVisibility(View.GONE);
        } else {
            profileNotLoggedIn.setVisibility(View.VISIBLE);
            profileLogged.setVisibility(View.GONE);
            toggleProfile.setVisibility(View.GONE);
            changeInfoPassword.setVisibility(View.GONE);
            profileIconNext.setVisibility(View.VISIBLE);
        }
    }

    public static void setDataUserOrder(Context context){
        if(GlobalVariable.arrayProfile != null) {
            GlobalVariable.arrayOrder.clear();
            StringRequest request = new StringRequest(StringRequest.Method.GET, GlobalVariable.GET_ORDER_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject object = new JSONObject(response);
                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject dataObject = (JSONObject) data.get(i);
                            String id_user = dataObject.getString("id_user");
                            if (id_user.equals(GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_ID_USER))) {
                                GlobalVariable.arrayOrder.add(new Order(
                                        dataObject.getString("id_bill"),
                                        formatDate(dataObject.getString("date_order")),
                                        Integer.parseInt(dataObject.getString("bill_status")),
                                        Integer.parseInt(dataObject.getString("total"))
                                ));
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

            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(request);
        }
    }

    private static String formatDate(String date){
        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        return day + '/' + month + '/' + year;
    }
}
