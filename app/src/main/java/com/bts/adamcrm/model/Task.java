package com.bts.adamcrm.model;

public class Task {
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
