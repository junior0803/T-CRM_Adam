package com.bts.adamcrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("key")
    @Expose
    public String key;
    @SerializedName("title")
    @Expose
    public String title;

    public Category() {
    }

    public Category(String title){
        this.title = title;
    }

    public Category(String title, String key){
        this.title = title;
        this.key = key;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getKey(){
        return this.key;
    }

    public void setKey(String key){
        this.key = key;
    }
}
