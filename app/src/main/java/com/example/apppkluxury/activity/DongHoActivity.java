package com.example.apppkluxury.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.apppkluxury.R;
import com.example.apppkluxury.adapter.DongHoAdapter;
import com.example.apppkluxury.model.Product;
import com.example.apppkluxury.retrofit.ApiPhuKien;
import com.example.apppkluxury.retrofit.RetrofitClient;
import com.example.apppkluxury.utils.utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DongHoActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiPhuKien apiPhuKien;
    CompositeDisposable composite = new CompositeDisposable();
    int page = 1;
    int type_id;
    DongHoAdapter adapterDH;
    List<Product> prodnewList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dong_ho);
        apiPhuKien = RetrofitClient.getInstance(utils.BASE_URL).create(ApiPhuKien.class);
        type_id = getIntent().getIntExtra("type_id", 4);

        AnhXa();
        ActionToolBar();
        getData(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading == false){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == prodnewList.size()-1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //add null
                prodnewList.add(null);
                adapterDH.notifyItemInserted(prodnewList.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //remove null
                prodnewList.remove(prodnewList.size()-1);
                adapterDH.notifyItemRemoved(prodnewList.size());
                page=page+1;
                getData(page);
                adapterDH.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    private void getData(int page) {
        composite.add(apiPhuKien.getProductByType(page, type_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productModel -> {
                            if(productModel.isSuccess()){
                                if(adapterDH == null){
                                    prodnewList = productModel.getResult();
                                    adapterDH = new DongHoAdapter(getApplicationContext(), prodnewList);
                                    recyclerView.setAdapter(adapterDH);
                                }else {
                                    int location = prodnewList.size()-1;
                                    int amountadd = productModel.getResult().size();
                                    for (int i=0;i<amountadd;i++) {
                                        prodnewList.add(productModel.getResult().get(i));

                                    }
                                    adapterDH.notifyItemRangeInserted(location, amountadd);
                                }

                            }else {
                                Toast.makeText(getApplicationContext(), "end product", Toast.LENGTH_LONG).show();
                                isLoading = true;
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "not connected server !!!", Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_dh);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        prodnewList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        composite.clear();
    }
}