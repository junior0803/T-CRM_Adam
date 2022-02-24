package com.bts.adamcrm.model;

import com.google.gson.annotations.SerializedName;

public class StockItem {

    @SerializedName("id")
    public int id;

    @SerializedName("q")
    public String quantity;

    @SerializedName("mq")
    public String minimum_quantity;

    @SerializedName("description")
    public String description;

    @SerializedName("pno")
    public String pno;

    @SerializedName("is_shopping")
    public int is_shopping;

    @SerializedName("type")
    public int type;

    @SerializedName("created_at")
    public String created_at;

    @SerializedName("updated_at")
    public String updated_at;

    public StockItem() {
    }

    public StockItem(int id, String quantity, String minimum_quantity, String description, String pno, int is_shopping, int type) {
        this.id = id;
        this.quantity = quantity;
        this.minimum_quantity = minimum_quantity;
        this.description = description;
        this.pno = pno;
        this.is_shopping = is_shopping;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMinimum_quantity() {
        return minimum_quantity;
    }

    public void setMinimum_quantity(String minimum_quantity) {
        this.minimum_quantity = minimum_quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public int getIs_shopping() {
        return is_shopping;
    }

    public void setIs_shopping(int is_shopping) {
        this.is_shopping = is_shopping;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
