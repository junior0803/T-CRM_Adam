package com.bts.adamcrm.model;

import com.google.android.gms.common.internal.ImagesContract;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attachment {
    @SerializedName("date_delete")
    @Expose
    public String date_delete;
    @SerializedName("key")
    @Expose
    public String key;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName(ImagesContract.URL)
    @Expose
    public String url;

    public Attachment(String name, String url, String date){
        this.name = name;
        this.url = url;
        this.date_delete = date;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getUrl(){
        return this.url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getDate_delete(){
        return this.date_delete;
    }

    public void setDate_delete(String date){
        this.date_delete = date;
    }

    public String getKey(){
        return this.key;
    }

    public void setKey(String key){
        this.key = key;
    }
}
