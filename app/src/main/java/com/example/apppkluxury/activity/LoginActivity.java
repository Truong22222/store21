package com.example.apppkluxury.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apppkluxury.R;
import com.example.apppkluxury.retrofit.ApiPhuKien;
import com.example.apppkluxury.retrofit.RetrofitClient;
import com.example.apppkluxury.utils.utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    TextView txtRegister, txtResetPass;
    EditText email,password;
    Button btnLogin;
    ApiPhuKien apiPhuKien;
    CompositeDisposable composite = new CompositeDisposable();
    boolean isLogin = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initControl();
    }

    private void initControl() {
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }

        });
        txtResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResetPassActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = email.getText().toString().trim();
                String str_pass = password.getText().toString().trim();
                if(TextUtils.isEmpty(str_email)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Email", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_pass)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Password", Toast.LENGTH_SHORT).show();
                }else {
                    //save

                    Paper.book().write("email", str_email);
                    Paper.book().write("password", str_pass);

                    login(str_email,str_pass);
                }
            }
        });
    }

    private void initView() {
        Paper.init(this);
        apiPhuKien = RetrofitClient.getInstance(utils.BASE_URL).create(ApiPhuKien.class);
        txtRegister = findViewById(R.id.txtRegister);
        txtResetPass = findViewById(R.id.txtResetPass);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        // read data
        if (Paper.book().read("email") != null && Paper.book().read("password") != null ) {
            email.setText(Paper.book().read("email"));
            password.setText(Paper.book().read("password"));
            if(Paper.book().read("isLogin") != null){
                boolean flag = Paper.book().read("isLogin");
                if (flag){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            login(Paper.book().read("email"),Paper.book().read("password"));

                        }
                    }, 1000);
                }
            }
        }
    }

    private void login(String email, String pass) {

        composite.add(apiPhuKien.login(email, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()) {
                                isLogin = true;
                                Paper.book().write("isLogin", isLogin);
                                utils.user_current = userModel.getResult().get(0);

                                // Luu thong tin user
                                Paper.book().write("user", userModel.getResult().get(0));
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage() ,Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(utils.user_current.getEmail()!=null && utils.user_current.getPass() != null) {
            email.setText(utils.user_current.getEmail());
            password.setText(utils.user_current.getPass());

        }
    }
}