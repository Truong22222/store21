package com.example.apppkluxury.model;

import java.io.Serializable;

public class Product implements Serializable {
    int id, type_id;
    String prod_price;
    String prod_img, prod_name, prod_detail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getProd_price() {
        return prod_price;
    }

    public void setProd_price(String prod_price) {
        this.prod_price = prod_price;
    }

    public String getProd_img() {
        return prod_img;
    }

    public void setProd_img(String prod_img) {
        this.prod_img = prod_img;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_detail() {
        return prod_detail;
    }

    public void setProd_detail(String prod_detail) {
        this.prod_detail = prod_detail;
    }


    public Product(int id, int type_id, String prod_price, String prod_img, String prod_name, String prod_detail) {
        this.id = id;
        this.type_id = type_id;
        this.prod_price = prod_price;
        this.prod_img = prod_img;
        this.prod_name = prod_name;
        this.prod_detail = prod_detail;
    }
}
