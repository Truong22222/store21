package com.example.apppkluxury.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.apppkluxury.R;
import com.example.apppkluxury.adapter.ProductAdapter;
import com.example.apppkluxury.adapter.TypeProdAdapter;
import com.example.apppkluxury.model.Product;
import com.example.apppkluxury.model.Type_Pro;
import com.example.apppkluxury.model.User;
import com.example.apppkluxury.retrofit.ApiPhuKien;
import com.example.apppkluxury.retrofit.RetrofitClient;
import com.example.apppkluxury.utils.utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listviewmanhinhchinh;
    NavigationView navigationView;
    RecyclerView recyclermanhinhchinh;
    ViewFlipper viewFlipper;
    DrawerLayout drawerLayout;
    CompositeDisposable composite = new CompositeDisposable();
    ApiPhuKien apiPK;
    ImageView imgSearch;
    //Product
    ProductAdapter prodAdapter;
    List<Product> arrayProduct;
    // Type product
    TypeProdAdapter typeProdAdapter;
    List<Type_Pro> arrayType;

    NotificationBadge badge;
    FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiPK = RetrofitClient.getInstance(utils.BASE_URL).create(ApiPhuKien.class);
        Paper.init(this);
        if(Paper.book().read("user") != null){
            User user = Paper.book().read("user");
            utils.user_current = user;
        }
        AnhXa();
        ActionBar();
        ActionViewFlipper();
        
        if (isConnected(this)){
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
            ActionViewFlipper();
            //getProduct();
            getTypeProduct();
            getProduct();
            getEventClick();

        }else {
            Toast.makeText(getApplicationContext(), "Not Internet, Please! do connected !", Toast.LENGTH_LONG).show();
        }
    }

    private void getEventClick() {
        listviewmanhinhchinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangChu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangChu);
                        break;
                    case 1:
                        Intent vongTay = new Intent(getApplicationContext(), DongHoActivity.class);
                        vongTay.putExtra("type_id", 2);
                        startActivity(vongTay);
                        break;
                    case 2:
                        Intent appleWatch = new Intent(getApplicationContext(), DongHoActivity.class);
                        appleWatch.putExtra("type_id", 3);

                        startActivity(appleWatch);
                    case 3:
                        Intent dongHo = new Intent(getApplicationContext(), DongHoActivity.class);
                        dongHo.putExtra("type_id", 4);
                        startActivity(dongHo);
                        break;
                    case 6:
                        Intent order = new Intent(getApplicationContext(), HistoryOderActivity.class);
                        startActivity(order);
                        break;
                    case 7:
                        // xoa key user
                        Paper.book().delete("user");
                        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(login);
                        finish();
                        break;
                }
            }
        });
    }

    private void getProduct() {
        composite.add(apiPK.getProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productModel -> {
                            if(productModel.isSuccess()) {
                                arrayProduct = productModel.getResult();
                                prodAdapter = new ProductAdapter(getApplicationContext(), arrayProduct);
                                recyclermanhinhchinh.setAdapter(prodAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Not connected server"+ throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                )
        );
    }

    private void getTypeProduct() {
        composite.add(apiPK.getTypeProd()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                   type_prodModel -> {
                       if (type_prodModel.isSuccess()) {
                           //Toast.makeText(getApplicationContext(), type_prodModel.getResult().get(0).getName(), Toast.LENGTH_LONG).show();
                           arrayType = type_prodModel.getResult();
                           arrayType.add(new Type_Pro("https://cdn-icons-png.flaticon.com/512/325/325145.png?w=740&t=st=1659360599~exp=1659361199~hmac=4bd42e949c5bb3ac45a2a031abfd618f50a3d14acca3425288215354ca38248c", "Đăng xuất"));
                           typeProdAdapter = new TypeProdAdapter(getApplicationContext(), arrayType);
                           listviewmanhinhchinh.setAdapter(typeProdAdapter);
                       }
                   }
                ));
    }


    private void ActionViewFlipper() {
        List<String> arrayQuangCao = new ArrayList<>();
        arrayQuangCao.add("https://vinasave.com/vnt_upload/UploadFile/thanh-ly-cua-hang-phu-kien-thoi-trang.jpg");
        arrayQuangCao.add("https://bossluxurywatch.vn/uploads/anh-dong-ho-dang-bao/5004/gia-ban-dh-2020/0w7a8370.jpg");
        arrayQuangCao.add("https://cdn3.dhht.vn/wp-content/uploads/2021/01/dong-ho-rolex-nam-nu-chinh-hang-gia-bao-nhieu-danh-gia-chi-tiet.jpg");
        for(int i=0; i<arrayQuangCao.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(arrayQuangCao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);

        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);

        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbarmanhinhchinh);
        navigationView = findViewById(R.id.navigationview);
        recyclermanhinhchinh = findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclermanhinhchinh.setLayoutManager(layoutManager);
        recyclermanhinhchinh.setHasFixedSize(true);
        badge = findViewById(R.id.menu_sl);
        frameLayout = findViewById(R.id.frameCart);
        imgSearch = findViewById(R.id.imgSearch);
        //*************************************************************
        listviewmanhinhchinh = findViewById(R.id.listviewmanhinhchinh);
        viewFlipper = findViewById(R.id.viewflipper);
        drawerLayout = findViewById(R.id.drawerlayout);

        //******* Khoi tao list product **********
        arrayProduct = new ArrayList<>();



        //**************************************************
        // khoi tao list type
        arrayType = new ArrayList<Type_Pro>();
        // Khoi tao adapter type
        if (utils.arrayCart == null){
            utils.arrayCart = new ArrayList<>();
        }else {
            int totalItem = 0;
            for (int i = 0;i < utils.arrayCart.size(); i++) {
                totalItem += utils.arrayCart.get(i).getAmount();
            }
            badge.setText(String.valueOf(totalItem));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cart = new Intent(getApplicationContext(), ShoppingCartActivity.class);
                startActivity(cart);
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem = 0;
        for (int i = 0;i < utils.arrayCart.size(); i++) {
            totalItem += utils.arrayCart.get(i).getAmount();
        }
        badge.setText(String.valueOf(totalItem));
    }

    private boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        composite.clear();
        super.onDestroy();

    }
}