package com.example.apppkluxury.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.apppkluxury.R;
import com.example.apppkluxury.model.Cart;
import com.example.apppkluxury.model.Product;
import com.example.apppkluxury.utils.utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {
    TextView namepd, pricepd, detailpd;
    Button btnAdd;
    ImageView imgpd;
    Spinner spinner;
    Toolbar toolbar;
    Product product;
    NotificationBadge badge;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();
        ActionToolBar();
        initData();
        initControl();
    }

    private void initControl() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddToCart();
            }
        });
    }

    private void AddToCart() {
        if (utils.arrayCart.size() > 0) {
            int amount = Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i = 0; i < utils.arrayCart.size(); i++) {
                if (utils.arrayCart.get(i).getIdProd()  == product.getId()) {
                    utils.arrayCart.get(i).setAmount(amount + utils.arrayCart.get(i).getAmount());
                    long price = Long.parseLong(String.valueOf(product.getProd_price())) + utils.arrayCart.get(i).getPrice();
                    utils.arrayCart.get(i).setPrice(price);
                    flag = true;

                }
            }
            if (flag == false) {
                long price = Long.parseLong(String.valueOf(product.getProd_price())) + amount;
                Cart cart = new Cart();
                cart.setPrice(price);
                cart.setAmount(amount);
                cart.setIdProd(product.getId());
                cart.setNameProd(product.getProd_name());
                cart.setImgProd(product.getProd_img());
                utils.arrayCart.add(cart);
            }
        }else {
            int amount = Integer.parseInt(spinner.getSelectedItem().toString());
            long price = Long.parseLong(String.valueOf(product.getProd_price())) + amount;
            Cart cart = new Cart();
            cart.setPrice(price);
            cart.setAmount(amount);
            cart.setIdProd(product.getId());
            cart.setNameProd(product.getProd_name());
            cart.setImgProd(product.getProd_img());
            utils.arrayCart.add(cart);
        }
        int totalItem = 0;
        for (int i = 0;i < utils.arrayCart.size(); i++) {
            totalItem += utils.arrayCart.get(i).getAmount();
        }
        badge.setText(String.valueOf(totalItem));
    }

    private void initData() {
        product = (Product) getIntent().getSerializableExtra("detail");
        namepd.setText(product.getProd_name());
        detailpd.setText(product.getProd_detail());
        Glide.with(getApplicationContext()).load(product.getProd_img()).into(imgpd);
        DecimalFormat decFormat = new DecimalFormat("###,###,###");
        pricepd.setText("Price: "+decFormat.format(Double.parseDouble(product.getProd_price()))+"vnd");
        Integer[] numbers = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, numbers);
        spinner.setAdapter(adapterspin);
    }

    private void initView() {
        namepd = findViewById(R.id.txtNameProd);
        pricepd = findViewById(R.id.txtPriceProd);
        detailpd = findViewById(R.id.txtdetaildesc);
        btnAdd = findViewById(R.id.btnAddToCart);
        spinner = findViewById(R.id.spinner);
        imgpd = findViewById(R.id.img_detail);
        toolbar = findViewById(R.id.toolbar_detail);
        badge = findViewById(R.id.menu_sl);
        FrameLayout frameCart = findViewById(R.id.frameCart);
        frameCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cart = new Intent(getApplicationContext(), ShoppingCartActivity.class);
                startActivity(cart);
            }
        });
        if (utils.arrayCart != null) {

            badge.setText(String.valueOf(utils.arrayCart.size()));
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        if(utils.arrayCart != null) {
            int totalItem = 0;
            for(int i=0; i<utils.arrayCart.size(); i++){
                totalItem += utils.arrayCart.get(i).getAmount();
            }
            badge.setText(String.valueOf(totalItem));
        }
    }
}