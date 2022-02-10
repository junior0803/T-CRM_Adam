package com.bts.adamcrm.model;

import java.util.HashMap;
import java.util.Map;

public class User {
    public Long date_logged_in;
    public String id;

    public User() {
    }

    public User(String str, Long date) {
        this.id = str;
        this.date_logged_in = date;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", this.id);
        hashMap.put("date_logged_in", this.date_logged_in);
        return hashMap;
    }
}
