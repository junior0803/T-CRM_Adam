package com.bts.adamcrm.model;

public class StockItem {
    public String description;
    public Long key;
    public Long minimum_quantity;
    public Long pno;
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
