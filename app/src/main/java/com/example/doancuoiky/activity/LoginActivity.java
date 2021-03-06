package com.example.doancuoiky.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doancuoiky.GlobalVariable;
import com.example.doancuoiky.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private Button forgotPassword, signIn, signUp;
    private EditText textInputUsername, textInputPassword;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        anhXa();

        validateUsername();
        validatePassword();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkData()){
                    onLogin();
                }
                else {
                    setError();
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent2);
            }
        });
    }

    private void anhXa() {

        signIn = findViewById(R.id.btn_sign_in);

        signUp = findViewById(R.id.btn_sign_up);
        forgotPassword = findViewById(R.id.btn_forgot_password_in_login);

        textInputUsername = findViewById(R.id.edt_username_login);
        textInputPassword = findViewById(R.id.edt_password_login);
    }

    private void validateUsername() {
        textInputUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    setErrorUsername();
                }
            }
        });
    }

    private void validatePassword() {
        textInputPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    setErrorPassword();
                }
            }
        });
    }

    private void setError(){
        if(textInputUsername.getText().length() < 8){
            setErrorUsername();
        }
        if(textInputPassword.getText().length() < 8){
            setErrorPassword();
        }

    }

    // Ki???m tra ????? d??i nh???p ??? edittext c?? h???p l???
    private boolean checkData(){
        int edtUsernameLength = textInputUsername.getText().toString().trim().length();
        int edtPasswordLength = textInputPassword.getText().toString().trim().length();

        if(edtUsernameLength < 8 || edtPasswordLength < 8){
            return false;
        }
        else{
            return true;
        }
    }

    private void setErrorUsername(){
        if (textInputUsername.getText().toString().length() <= 0) {
            textInputUsername.setError("Vui l??ng nh???p email ho???c t??n t??i kho???n");
        } else if (textInputUsername.getText().toString().length() > 0 &&
                textInputUsername.getText().toString().length() < 8) {
            textInputUsername.setError("T??n ????ng nh???p ph???i d??i ??t nh???t 8 k?? t???");
        } else {
            textInputUsername.setError(null);
        }
    }

    private void setErrorPassword(){
        if (textInputPassword.getText().toString().length() <= 0) {
            textInputPassword.setError("Vui l??ng nh???p m???t kh???u");
        } else if (textInputPassword.getText().toString().length() > 0 &&
                textInputPassword.getText().toString().length() < 8) {
            textInputPassword.setError("M???t kh???u ph???i ch???a ??t nh???t 8 k?? t??? bao g???m ch??? th?????ng, " +
                    "ch??? hoa v?? k?? t??? ?????c bi???t");
        } else {
            textInputPassword.setError(null);
        }
    }

    private void onLogin(){
        StringRequest request = new StringRequest(Request.Method.POST, GlobalVariable.LOGIN_URL,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject result = object.getJSONObject("result");

                    int code = result.getInt("code");
                    if(code == 1){
                        Toast.makeText(LoginActivity.this, "l???i ????ng nh???p", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        JSONObject data = object.getJSONObject("data");
                        final String token = data.getString("token");

                        dialog = ProgressDialog.show(LoginActivity.this, "", "??ang ????ng nh???p...",
                                true);

                        dialog.show();
                        hideKeyboard();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("loginTrue", true);
                                GlobalVariable.TOKEN = token;
                                startActivity(intent);
                                finish();
                                dialog.dismiss();
                            }
                        }, 2000); // 2000 milliseconds delay
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "error => " + e.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "T??n ????ng nh???p ho???c m???t kh???u kh??ng ch??nh x??c!",Toast.LENGTH_LONG ).show();
            }
        }){

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("loginname", textInputUsername.getText().toString().trim());
                params.put("userpassword", textInputPassword.getText().toString().trim());

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) {

            }
        });
        queue.add(request);

    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),0);
    }
}