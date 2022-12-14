package com.example.apppkluxury.model;

public class Type_Pro {
    int id;
    String type_img, type_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType_img() {
        return type_img;
    }

    public void setType_img(String type_img) {
        this.type_img = type_img;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public Type_Pro( String type_img, String type_name) {

        this.type_img = type_img;
        this.type_name = type_name;
    }
}
