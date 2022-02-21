package com.bts.adamcrm.model;

import com.google.gson.annotations.SerializedName;

public class StockItem {
    @SerializedName("description")
    public String description;

    @SerializedName("key")
    public Long key;

    @SerializedName("minimum_quantity")
    public Long minimum_quantity;

    @SerializedName("pno")
    public Long pno;

    @SerializedName("quantity")
    public Long quantity;

    public StockItem() {
    }

    public StockItem(Long pno, Long minquantity, Long quantity, Long key, String description) {
        this.pno = pno;
        this.minimum_quantity = minquantity;
        this.quantity = quantity;
        this.key = key;
        this.description = description;
    }

    public Long getPno() {
        return this.pno;
    }

    public void setPno(Long l) {
        this.pno = l;
    }

    public Long getMinimum_quantity() {
        return this.minimum_quantity;
    }

    public void setMinimum_quantity(Long l) {
        this.minimum_quantity = l;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long l) {
        this.quantity = l;
    }

    public Long getKey() {
        return this.key;
    }

    public void setKey(Long l) {
        this.key = l;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }
}
