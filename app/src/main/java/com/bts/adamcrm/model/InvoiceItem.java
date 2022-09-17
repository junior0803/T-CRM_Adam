package com.bts.adamcrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceItem {
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("price")
    @Expose
    public String price;
    @SerializedName("quantity")
    @Expose
    public int quantity;

    public InvoiceItem() {
    }

    public InvoiceItem(String des, String price, int quantity) {
        this.description = des;
        this.price = price;
        this.quantity = quantity;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String str) {
        this.price = str;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int i) {
        this.quantity = i;
    }
}
