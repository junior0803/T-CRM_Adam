package com.bts.adamcrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attachment {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("file_path")
    @Expose
    public String file_path;

    public Attachment(String file_path){
        this.file_path = file_path;
    }

    public Attachment(int id, String file_path){
        this.id = id;
        this.file_path = file_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }
}
