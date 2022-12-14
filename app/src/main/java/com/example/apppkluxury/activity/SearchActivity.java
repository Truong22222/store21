package com.example.apppkluxury.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apppkluxury.R;
import com.example.apppkluxury.adapter.DongHoAdapter;
import com.example.apppkluxury.adapter.ProductAdapter;
import com.example.apppkluxury.model.Product;
import com.example.apppkluxury.retrofit.ApiPhuKien;
import com.example.apppkluxury.retrofit.RetrofitClient;
import com.example.apppkluxury.utils.utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    EditText editTextSearch;
    DongHoAdapter dhAdapter;
    List<Product> arrayProduct;
    CompositeDisposable composite = new CompositeDisposable();
    ApiPhuKien apiPK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        ActionBar();
    }

    private void initView() {
        arrayProduct = new ArrayList<>();
        apiPK = RetrofitClient.getInstance(utils.BASE_URL).create(ApiPhuKien.class);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleview_search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        editTextSearch = findViewById(R.id.edtSearch);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0){
                    arrayProduct.clear();
                    dhAdapter = new DongHoAdapter(getApplicationContext(), arrayProduct);
                    recyclerView.setAdapter(dhAdapter);
                }else {
                    getDataSearch(charSequence.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


    }

    private void getDataSearch(String s) {
        arrayProduct.clear();
        composite.add(apiPK.search(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productModel -> {
                            if (productModel.isSuccess()) {
                                arrayProduct = productModel.getResult();
                                dhAdapter = new DongHoAdapter(getApplicationContext(), arrayProduct);
                                recyclerView.setAdapter(dhAdapter);
                            }
                            
                        }, 
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        composite.clear();
        super.onDestroy();
    }
}