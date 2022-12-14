package com.example.apppkluxury.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.apppkluxury.R;
import com.example.apppkluxury.adapter.OrderAdapter;
import com.example.apppkluxury.retrofit.ApiPhuKien;
import com.example.apppkluxury.retrofit.RetrofitClient;
import com.example.apppkluxury.utils.utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HistoryOderActivity extends AppCompatActivity {
    CompositeDisposable composite = new CompositeDisposable();
    ApiPhuKien apiPhuKien;
    RecyclerView recyclerOrder;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_oder);
        initView();
        initToolbar();
        getOrder();

    }

    private void getOrder() {
        apiPhuKien = RetrofitClient.getInstance(utils.BASE_URL).create(ApiPhuKien.class);
        composite.add(apiPhuKien.historyOrder(utils.user_current.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        orderModel -> {
                            OrderAdapter adapter = new OrderAdapter(orderModel.getResult(), getApplicationContext());
                            recyclerOrder.setAdapter(adapter);
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            String iii = throwable.getMessage();
                            System.out.println(iii);
                        }
                ));
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        apiPhuKien = RetrofitClient.getInstance(utils.BASE_URL).create(ApiPhuKien.class);
        recyclerOrder = findViewById(R.id.recycleview_order);
        toolbar = findViewById(R.id.toolbar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerOrder.setLayoutManager(layoutManager);
    }

    @Override
    protected void onDestroy() {
        composite.clear();
        super.onDestroy();
    }
}