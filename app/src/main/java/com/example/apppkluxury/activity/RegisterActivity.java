package com.example.apppkluxury.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apppkluxury.R;
import com.example.apppkluxury.retrofit.ApiPhuKien;
import com.example.apppkluxury.retrofit.RetrofitClient;
import com.example.apppkluxury.utils.utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {
    EditText email, password, repass, name, phone;
    AppCompatButton button;
    ApiPhuKien apiPhuKien;
    CompositeDisposable composite = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initControl();
    }

    private void initView() {
        apiPhuKien = RetrofitClient.getInstance(utils.BASE_URL).create(ApiPhuKien.class);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        repass = (EditText) findViewById(R.id.repass);
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        button = findViewById(R.id.btnRegister);

    }

    private void initControl() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {
        String str_email = email.getText().toString().trim();
        String str_pass = password.getText().toString().trim();
        String str_repass = repass.getText().toString().trim();
        String str_name = name.getText().toString().trim();
        String str_phone = phone.getText().toString().trim();

        if(TextUtils.isEmpty(str_name)){
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(str_email)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Email", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(str_pass)){
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Password", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(str_repass)){
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập xác nhận lại Password", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(str_phone)){
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Phone", Toast.LENGTH_SHORT).show();
        }else {
            if(str_pass.equals(str_repass)){
                //post data
                composite.add(apiPhuKien.register(str_email,str_pass,str_name,str_phone)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    utils.user_current.setEmail(str_email);
                                    utils.user_current.setPass(str_pass);
                                    if(userModel.isSuccess()){
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        ));
            }else {
                Toast.makeText(getApplicationContext(), "Password không trùng khớp!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        composite.clear();
        super.onDestroy();
    }
}