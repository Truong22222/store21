package com.example.apppkluxury.retrofit;

import com.example.apppkluxury.model.OrderModel;
import com.example.apppkluxury.model.Product;
import com.example.apppkluxury.model.ProductModel;
import com.example.apppkluxury.model.Type_Pro;
import com.example.apppkluxury.model.Type_prodModel;
import com.example.apppkluxury.model.UserModel;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiPhuKien {
    @GET("getproduct.php")
    Observable<ProductModel> getProduct();

    @GET("gettypeprod.php")
    Observable<Type_prodModel> getTypeProd();

    @POST("getdetail.php")
    @FormUrlEncoded
    Observable<ProductModel> getProductByType(
            @Field("page") int page,
            @Field("type_id") int type_id

    );

    @POST("register.php")
    @FormUrlEncoded
    Observable<UserModel> register(
            @Field("email") String email,
            @Field("password") String password,
            @Field("name") String name,
            @Field("phone") String phone

    );
    @POST("login.php")
    @FormUrlEncoded
    Observable<UserModel> login(
            @Field("email") String email,
            @Field("password") String password

    );

    @POST("order.php")
    @FormUrlEncoded
    Observable<UserModel> createOrder(
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("total") String total,
            @Field("id_user") int id,
            @Field("address") String address,
            @Field("amount") int amount,
            @Field("detail") String detail

    );

    @POST("historyorder.php")
    @FormUrlEncoded
    Observable<OrderModel> historyOrder(
            @Field("id_user") int id
    );

    @POST("search.php")
    @FormUrlEncoded
    Observable<ProductModel> search(
            @Field("search") String search
    );
}
