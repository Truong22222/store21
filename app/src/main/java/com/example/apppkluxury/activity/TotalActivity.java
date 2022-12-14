package com.example.apppkluxury.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;


import com.example.apppkluxury.R;
import com.example.apppkluxury.retrofit.ApiPhuKien;
import com.example.apppkluxury.retrofit.RetrofitClient;
import com.example.apppkluxury.utils.utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TotalActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtemail, txtphone, txttotal;
    EditText address;
    AppCompatButton btnOder;
    CompositeDisposable composite = new CompositeDisposable();
    ApiPhuKien apiPhuKien;
    long total;
    int totalItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);
        initView();
        countItem();
        initControl();
    }

    private void countItem() {
        totalItem = 0;
        for (int i = 0;i < utils.arraybyCart.size(); i++) {
            totalItem = totalItem + utils.arraybyCart.get(i).getAmount();
        }
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        DecimalFormat decFormat = new DecimalFormat("###,###,###");

        total = getIntent().getLongExtra("total",0);

        txttotal.setText(decFormat.format(total));
        txtemail.setText(utils.user_current.getEmail());
        txtphone.setText(utils.user_current.getPhone());


        btnOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_address = address.getText().toString().trim();
                if(TextUtils.isEmpty(str_address)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ !", Toast.LENGTH_SHORT).show();
                }else{
                    // post data
                    String str_email = utils.user_current.getEmail();
                    String str_phone = utils.user_current.getPhone();
                    int id = utils.user_current.getId();
                    Log.d("test",new Gson().toJson(utils.arraybyCart));
                    composite.add(apiPhuKien.createOrder(str_email,str_phone, String.valueOf(total), id, str_address, totalItem, new Gson().toJson(utils.arraybyCart))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        Toast.makeText(getApplicationContext(), "Thanh cong", Toast.LENGTH_SHORT).show();
                                        utils.arraybyCart.clear();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));






                }

            }
        });
    }

    private void initView() {
        apiPhuKien = RetrofitClient.getInstance(utils.BASE_URL).create(ApiPhuKien.class);
        toolbar = findViewById(R.id.toolbar);
        txtemail = findViewById(R.id.txtEmail);
        txtphone = findViewById(R.id.txtPhone);
        address = findViewById(R.id.edtAddress);
        txttotal = findViewById(R.id.txtTotal);
        btnOder = findViewById(R.id.btnoder);
    }

    @Override
    protected void onDestroy() {
        composite.clear();
        super.onDestroy();
    }
}