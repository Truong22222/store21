package com.example.apppkluxury.model;

public class Item {
    int prod_id;
    String prod_name;
    int amount;
    String prod_img;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getImgProd() {
        return prod_img;
    }

    public void setImgProd(String imgProd) {
        this.prod_img = imgProd;
    }

    public int getProd_id() {
        return prod_id;
    }

    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }


}
