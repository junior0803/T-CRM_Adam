package com.bts.adamcrm.model;

import com.google.gson.annotations.SerializedName;

public class Task {
    @SerializedName("title")
    public String title;

    public Task(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String str){
        this.title = str;
    }
}
