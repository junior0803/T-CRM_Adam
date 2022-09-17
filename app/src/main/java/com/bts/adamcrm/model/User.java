package com.bts.adamcrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class User {

    @SerializedName("data[email]")
    public String email;

    @SerializedName("data[password]")
    public String password;


    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }
}
