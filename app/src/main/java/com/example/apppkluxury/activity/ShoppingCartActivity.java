package com.example.apppkluxury.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.apppkluxury.R;
import com.example.apppkluxury.adapter.ShoppingCartAdapter;
import com.example.apppkluxury.model.Cart;
import com.example.apppkluxury.model.EventBus.toTalEvent;
import com.example.apppkluxury.utils.utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity {
    TextView cartEmpty, total;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnBy;
    ShoppingCartAdapter adapter;
    long toTal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        initView();
        initControl();
        toTalMoney();
    }

    private void toTalMoney() {
        toTal = 0;
        for(int i=0; i<utils.arraybyCart.size(); i++) {
            toTal = toTal + (utils.arraybyCart.get(i).getPrice() * (utils.arraybyCart.get(i).getAmount()));
        }
        DecimalFormat decFormat = new DecimalFormat("###,###,###");
        total.setText(decFormat.format(toTal)+"Đ");
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
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(utils.arrayCart.size() == 0 ){
            cartEmpty.setVisibility(View.VISIBLE);
        }else {
            adapter = new ShoppingCartAdapter(getApplicationContext(), utils.arrayCart);
            recyclerView.setAdapter(adapter);
        }
        btnBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TotalActivity.class);
                intent.putExtra("total", toTal);
                utils.arrayCart.clear();
                startActivity(intent);
            }
        });
    }

    private void initView() {
        cartEmpty = findViewById(R.id.txtcartisempty);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleviewshoppingcart);
        total = findViewById(R.id.txtTotal);
        btnBy = findViewById(R.id.btnByCart);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventToTal(toTalEvent event) {
        if(event != null){
            toTalMoney();
        }
    }
}