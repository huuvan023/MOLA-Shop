package com.example.doancuoiky.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doancuoiky.GlobalVariable;
import com.example.doancuoiky.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ChangeInfoActivity extends AppCompatActivity {

    private ImageView goBackChangeInfo;
    private RadioButton male, female;
    private Button update;
    private TextView tvBirthday;
    private EditText edtFullName, edtEmail, edtIdentify, edtPhoneNumber, edtAddress;
    String _name, _email, _citizen, _phone, _address, _gender, _birthday, _avatar;
    private ImageView avatar, openFile;
    public static ArrayList<String> arrayListAvatar;
    int REQUEST_CODE_AVATAR = 123;
    String avatarImageName = "";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        anhXa();
        setOnClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_AVATAR && resultCode == RESULT_OK && data != null) {
            avatarImageName = data.getStringExtra("imageSelected");

            String PACKAGE_NAME = getApplicationContext().getPackageName();
            int idImg = getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + avatarImageName,
                    null, null);
            avatar.setImageResource(idImg);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setOnClick() {
        goBackChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ChangeInfoActivity.this, ListAvatarActivity.class),
                        REQUEST_CODE_AVATAR);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDataInfo();
                onChangeInfo();
            }

        });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(ChangeInfoActivity.this, MainActivity.class);
        intent.putExtra("gotoProfile", "profile");
        startActivity(intent);
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        String getDate = GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_BIRTHDAY);
        int YEAR , MONTH , DATE ;

        if(getDate.length() > 0){
            getDate = GlobalVariable.formatDateInVN(getDate);
            YEAR = Integer.parseInt(getDate.substring(6, 10));
            MONTH = Integer.parseInt(getDate.substring(3, 5)) - 1; // tháng render từ 0-11
            DATE = Integer.parseInt(getDate.substring(0, 2));
        }else {
            YEAR = calendar.get(Calendar.YEAR);
            MONTH = calendar.get(Calendar.MONTH);
            DATE = calendar.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dateOfMonth) {
                String strDay, strMonth;
                if ((month + 1) < 10) {
                    strMonth = "0" + (month + 1);
                } else {
                    strMonth = "" + (month + 1);
                }

                if (dateOfMonth < 10) {
                    strDay = "0" + dateOfMonth;
                } else {
                    strDay = "" + dateOfMonth;
                }

                String strDate = strDay + "/" + strMonth + "/" + year;

                tvBirthday.setText(strDate);
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();
    }

    private void anhXa() {
        goBackChangeInfo = findViewById(R.id.iv_back_change_info);
        male = findViewById(R.id.rd_gender_male_change_info);
        female = findViewById(R.id.rd_gender_female_change_info);
        update = findViewById(R.id.btn_update_change_info);
        tvBirthday = findViewById(R.id.tv_birthday_change_info);
        edtFullName = findViewById(R.id.edt_username_change_info);
        edtEmail = findViewById(R.id.edt_email_change_info);
        edtIdentify = findViewById(R.id.edt_identification_card_change_info);
        edtPhoneNumber = findViewById(R.id.edt_phone_number_change_info);
        edtAddress = findViewById(R.id.edt_address_change_info);
        avatar = findViewById(R.id.img_avatar_change_info);
        openFile = findViewById(R.id.img_open_folder);

        // gán giá trị
        String[] listAvatarName = getResources().getStringArray(R.array.list_avatar);
        arrayListAvatar = new ArrayList<>(Arrays.asList(listAvatarName));

        if (GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_AVATAR).length() > 0) {

            String avatarName = GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_AVATAR);

            String PACKAGE_NAME = getApplicationContext().getPackageName();
            int idImg = getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + avatarName,
                    null, null);
            avatar.setImageResource(idImg);
        }

        edtFullName.setText(GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_USER_NAME));
        edtEmail.setText(GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_EMAIL));
        edtIdentify.setText(GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_CITIZEN_IDENTIFICATION));
        String getBirthday = GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_BIRTHDAY);

        if (getBirthday.length() == 0) {
            tvBirthday.setText("");
        } else {
            tvBirthday.setText(GlobalVariable.formatDateInVN(getBirthday));
        }

        edtPhoneNumber.setText(GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_PHONE_NUMBER));
        edtAddress.setText(GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_ADDRESS));

        if (GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_GENDER).length() == 0 ||
                GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_GENDER).equals("0")) {
            male.setChecked(true);
            female.setChecked(false);
        } else {
            male.setChecked(false);
            female.setChecked(true);
        }

        getDataInfo();

    }

    private void getDataInfo() {
        _name = edtFullName.getText().toString();
        _email = edtEmail.getText().toString();

        if (tvBirthday.getText().toString().length() == 0) {
            _birthday = "";
        } else {
            _birthday = formatDateDatabase(tvBirthday.getText().toString()); // format date theo dinh dang database
        }

        if (male.isChecked()) {
            _gender = "0";
        } else {
            _gender = "1";
        }
        _citizen = edtIdentify.getText().toString();
        _phone = edtPhoneNumber.getText().toString();
        _address = edtAddress.getText().toString();
        if (avatarImageName.length() == 0) {
            _avatar = GlobalVariable.arrayProfile.get(GlobalVariable.INDEX_AVATAR);
        } else {
            _avatar = avatarImageName;
        }
    }

    private void onChangeInfo() {

        StringRequest request = new StringRequest(Request.Method.POST, GlobalVariable.USER_UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject result = object.getJSONObject("result");

                            int code = result.getInt("code");
                            if(code == 0){

                                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeInfoActivity.this);

                                builder.setTitle("Thông báo");
                                builder.setMessage("Xác nhận cập nhật ?");

                                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        setDataArrayProfile();
                                        goToMainActivity();
                                        finish();
                                    }
                                });

                                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                builder.show();
                            }
                            else{
                                Toast.makeText(ChangeInfoActivity.this, "Cập nhật thất bại1",
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChangeInfoActivity.this, "Cập nhật thất bại => " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChangeInfoActivity.this, "Cập nhật thất bại",
                        Toast.LENGTH_LONG).show();

            }
        }){

            @Override
            protected Map<String, String> getParams()
            {
                getDataInfo();
                Map<String, String>  params = new HashMap<>();
                params.put("username",GlobalVariable.validateNameFirstUpperCase(_name));
                params.put("email", _email);
                params.put("birthday", _birthday);
                params.put("gender", _gender);
                params.put("citizen_identification", _citizen+"");
                params.put("phone_number", _phone);
                params.put("address", _address);
                params.put("avatar",_avatar);
                return params;
            }

            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", GlobalVariable.TOKEN);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(ChangeInfoActivity.this);

        queue.add(request);
    }

    private void setDataArrayProfile() {
        getDataInfo();
        GlobalVariable.arrayProfile.set(GlobalVariable.INDEX_USER_NAME, GlobalVariable.validateNameFirstUpperCase(_name));
        GlobalVariable.arrayProfile.set(GlobalVariable.INDEX_ADDRESS, _address);
        GlobalVariable.arrayProfile.set(GlobalVariable.INDEX_CITIZEN_IDENTIFICATION, _citizen);
        GlobalVariable.arrayProfile.set(GlobalVariable.INDEX_PHONE_NUMBER, _phone);
        GlobalVariable.arrayProfile.set(GlobalVariable.INDEX_GENDER, _gender);
        GlobalVariable.arrayProfile.set(GlobalVariable.INDEX_AVATAR, _avatar);

        GlobalVariable.arrayProfile.set(GlobalVariable.INDEX_BIRTHDAY, _birthday);
    }

    // doi lai cho dung dinh dang cua database yyyy-mm-dd
    private String formatDateDatabase(String date) {
        String year = date.substring(6, 10);
        String month = date.substring(3, 5);
        String day = date.substring(0, 2);

        return year + "-" + month + "-" + day;
    }

}