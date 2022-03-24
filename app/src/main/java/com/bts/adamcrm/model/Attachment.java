package com.bts.adamcrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attachment {
    @SerializedName("file_path")
    @Expose
    public String file_path;

    public Attachment(String file_path){
        this.file_path = file_path;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }
}
