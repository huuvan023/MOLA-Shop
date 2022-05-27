package com.example.doancuoiky.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SeekBar;
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private ImageView goBack;
    private Button signUp;
    private EditText edtEmail,edtUsername,edtFullName,edtPassword,edtPhoneNumber,edtConfirmPassword;
    private ProgressBar progressBar;
    private boolean isValidEmail = false;
    private boolean isValidPhone = false;

    RadioButton rdMale,rdFemale;

    String _email, _username, _name, _phone, _password, _confirmPassword;
    String _gender = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        anhXa();

        validation();

        radioButtonClick();

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkData()){

                    _email = edtEmail.getText().toString().trim();
                    _username = edtUsername.getText().toString().trim();
                    _name = GlobalVariable.validateNameFirstUpperCase(edtFullName.getText().toString().trim()) ;
                    _phone = edtPhoneNumber.getText().toString().trim();
                    _password = edtPassword.getText().toString().trim();
                    _confirmPassword = edtConfirmPassword.getText().toString().trim();

                    if(!_password.equals(_confirmPassword)){
                        edtConfirmPassword.setError("Mật khẩu không trùng khớp");
                    }
                    else{
                        onSignUp();
                    }
                }
                else {
                    checkError();
                }
            }
        });
    }

    private void radioButtonClick() {
        rdMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _gender = "0";
            }
        });
        rdFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _gender = "1";
            }
        });
    }

    private void validation() {
        validateEmail();
        validateUsername();
        validateFullName();
        validatePhoneNumber();
        validatePassword();
    }

    private void anhXa() {
        goBack = findViewById(R.id.icon_back_toolbar_sign_up);
        signUp = findViewById(R.id.btn_sign_up_sign_up);
        edtEmail = findViewById(R.id.edt_email_sign_up);
        edtUsername = findViewById(R.id.edt_username_sign_up);
        edtFullName = findViewById(R.id.edt_full_name_sign_up);
        edtPhoneNumber = findViewById(R.id.edt_phone_sign_up);
        edtPassword= findViewById(R.id.edt_password_sign_up);
        edtConfirmPassword= findViewById(R.id.edt_confirm_password_sign_up);
        rdFemale = findViewById(R.id.rd_gender_female_sign_up);
        rdMale = findViewById(R.id.rd_gender_male_sign_up);
        progressBar = findViewById(R.id.progressBar_check_password);

        edtPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length() > 0){
                    progressBar.setVisibility(View.VISIBLE);
                }
                else{
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int strength = 0;
                if (edtPassword.getText().toString().trim().matches(".*[a-z].*")){
                        strength+=10;
                }
                if (edtPassword.getText().toString().trim().matches(".*[A-Z].*")){
                    strength+=10;
                }
                if (edtPassword.getText().toString().trim().matches(".*[0-9].*")){
                    strength+=10;
                }
                if (edtPassword.getText().toString().trim().matches(".*[@#$%&+=^].*")){
                    strength+=10;
                }


                if(edtPassword.getText().length() > 0 && edtPassword.getText().length() < 8){
                    strength+=10;
                }
                else if(edtPassword.getText().length() >= 8 && edtPassword.getText().length() <= 12){
                    strength+=30;
                }
                else if(edtPassword.getText().length() > 12){
                    strength+=40;
                }

                progressBar.setProgress(strength);
            }
        });
    }

    private void validateEmail(){
        edtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    setErrorEmail();
                }
            }
        });
    }

    private void validateUsername() {
        edtUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    setErrorUsername();
                }
            }
        });
    }

    private  void validateFullName(){
        edtFullName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    setErrorFullName();
                }
            }
        });
    }

    private void validatePhoneNumber(){
        edtPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    setErrorPhoneNumber();
                }
            }
        });
    }

    private void validatePassword(){
        edtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    setErrorPassword();
                }
            }
        });
    }



    // Kiểm tra độ dài nhập ở edittext có hợp lệ
    private boolean checkData(){

        int edtUsernameLength = edtUsername.getText().toString().trim().length();
        int edtPasswordLength = edtPassword.getText().toString().trim().length();


        if(edtUsernameLength >= 8 && edtPasswordLength >= 8 && isValidEmail && isValidPhone){
            return true;
        }
        else{
            return false;
        }
    }

    private void setErrorEmail(){
        if (edtEmail.getText().toString().trim().length() <= 0) {
            edtEmail.setError("Vui lòng nhập email");
            isValidEmail = false;
        } else if (!edtEmail.getText().toString().trim().matches(GlobalVariable.EMAIL_PATTERN)) {
            edtEmail.setError("Định dạng email không hợp lệ");
            isValidEmail = false;
        } else {
            edtEmail.setError(null);
            isValidEmail = true;
        }
    }

    private void setErrorUsername(){
        if (edtUsername.getText().toString().trim().length() <= 0) {
            edtUsername.setError("Vui lòng nhập tên tài khoản");
        } else if (edtUsername.getText().toString().trim().length() > 0
                && edtUsername.getText().toString().trim().length() < 8) {
            edtUsername.setError("Tên đăng nhập phải dài ít nhất 8 ký tự");
        } else {
            edtUsername.setError(null);
        }
    }

    private void setErrorFullName(){
        if (edtFullName.getText().toString().trim().length() <= 0) {
            edtFullName.setError("Vui lòng nhập họ tên của bạn");
        } else {
            edtFullName.setError(null);
        }
    }

    private void setErrorPhoneNumber(){
        if (edtPhoneNumber.getText().toString().trim().length() <= 0) {
            edtPhoneNumber.setError("Vui lòng nhập số điện thoại");
            isValidPhone = false;
        } else if (!edtPhoneNumber.getText().toString().trim().matches(GlobalVariable.PHONE_PATTERN)) {
            edtPhoneNumber.setError("Định dạng số điện thoại không hợp lệ");
            isValidPhone = false;
        } else {
            edtPhoneNumber.setError(null);
            isValidPhone = true;
        }
    }

    private void setErrorPassword(){
        if (edtPassword.getText().toString().trim().length() <= 0) {
            edtPassword.setError("Vui lòng nhập mật khẩu");
        } else if (!edtPassword.getText().toString().trim().matches(GlobalVariable.PASSWORD_PATTERN)) {
            edtPassword.setError("Định dạng mật khẩu không hợp lệ, mật khẩu phải gồm 8 ký tự bao gồm ký tự thường," +
                    " in hoa, chữ số và ký tự đặc biệt");
        } else {
            edtPassword.setError(null);
        }
    }

    private void checkError() {
        if(!isValidEmail){
            setErrorEmail();
        }
        if(!isValidPhone){
            setErrorPhoneNumber();
        }
        if(edtUsername.getText().toString().trim().length() < 8){
            setErrorUsername();
        }
        if(edtPassword.getText().toString().trim().length() < 8){
            setErrorPassword();
        }
        if(edtFullName.getText().toString().trim().length() == 0){
            setErrorFullName();
        }
    }

    private void onSignUp(){
        StringRequest request = new StringRequest(Request.Method.POST, GlobalVariable.USER_SIGN_UP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject result = object.getJSONObject("result");

                            int code = result.getInt("code");
                            if(code == 0){

                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);

                                builder.setTitle("Thông báo");
                                builder.setMessage("Đăng ký thành công, đăng nhập ngay");

                                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                                        startActivity(intent);
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
                                Toast.makeText(SignUpActivity.this, "Email hoặc tên đăng nhập đã tồn tại",
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SignUpActivity.this, "error => " + e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUpActivity.this, "Email hoặc tên đăng nhập đã tồn tại",
                        Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", edtEmail.getText().toString().trim());
                params.put("loginname", edtUsername.getText().toString().trim());
                params.put("username", GlobalVariable.validateNameFirstUpperCase(edtFullName.getText().toString().trim()));
                params.put("phone_number", edtPhoneNumber.getText().toString().trim());
                params.put("userpassword", edtPassword.getText().toString().trim());
                params.put("gender", _gender);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);

        queue.add(request);

    }
}

