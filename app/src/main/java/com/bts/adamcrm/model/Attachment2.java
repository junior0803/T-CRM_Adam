package com.bts.adamcrm.model;

import com.google.android.gms.common.internal.ImagesContract;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attachment2 {
    @SerializedName("key")
    @Expose
    public String key;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName(ImagesContract.URL)
    @Expose
    public String url;

    public Attachment2() {
    }

    public Attachment2(String name, String url){
        this.name = name;
        this.url = url;
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

    public String getKey(){
        return this.key;
    }

    public void setKey(String key){
        this.key = key;
    }
}
