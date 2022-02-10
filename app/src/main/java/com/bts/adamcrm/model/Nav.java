package com.bts.adamcrm.model;

public class Nav {
    public int icon;
    public String title;

    public Nav(String str, int i) {
        this.title = str;
        this.icon = i;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int i) {
        this.icon = i;
    }
}
