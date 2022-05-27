package com.example.doancuoiky.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class ForgotPasswordActivity extends AppCompatActivity {

    private ImageView goBackToLogin;
    private Button btnSendMail;
    private EditText edtEmail;
    private boolean isValidEmail = false;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        anhXa();

        goBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateEmail();
                if (isValidEmail) {
                    dialog = ProgressDialog.show(ForgotPasswordActivity.this, "", "Vui lòng chờ...",
                            true);
                    dialog.show();

                    onForgotPassword();
                }
            }
        });

    }

    private void onForgotPassword() {
        StringRequest request = new StringRequest(StringRequest.Method.POST, GlobalVariable.FORGOT_PASSWORD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    JSONObject object = new JSONObject(response);
                    JSONObject result = object.getJSONObject("result");
                    int code = Integer.parseInt(result.getString("code"));
                    if (code == 0)  {

                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                        builder.setTitle("Thông báo");
                        builder.setMessage("Mật khẩu của bạn đã được cập nhật, vui lòng kiểm tra hộp thư và đăng nhập lại");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
                                finish();
                            }
                        });
                        builder.show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ForgotPasswordActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ForgotPasswordActivity.this, "Email bạn vừa nhập không chính xác", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", edtEmail.getText().toString().trim());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(ForgotPasswordActivity.this);
        queue.add(request);
    }

    private void anhXa() {
        goBackToLogin = findViewById(R.id.icon_back_toolbar_forgot_password);
        btnSendMail = findViewById(R.id.btn_send_mail_forgot_password);
        edtEmail = findViewById(R.id.edt_email_forgot_password);

    }

    private void validateEmail() {
        String email = edtEmail.getText().toString().trim();

        if (email.length() <= 0) {
            edtEmail.setError("Vui lòng nhập email");
            isValidEmail = false;
        } else if (!email.matches(GlobalVariable.EMAIL_PATTERN)) {
            edtEmail.setError("Định dạng email không hợp lệ");
            isValidEmail = false;

        } else {
            edtEmail.setError(null);
            isValidEmail = true;
        }
    }
}
