package com.example.apppkluxury.model;

import java.util.List;

public class Type_prodModel {
    boolean success;
    String message;
    List<Type_Pro> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Type_Pro> getResult() {
        return result;
    }

    public void setResult(List<Type_Pro> result) {
        this.result = result;
    }



}
