package com.bts.adamcrm.model;

import com.bts.adamcrm.util.TimeUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Invoice {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("invoice_no")
    @Expose
    public String invoice_no;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("invoice_date")
    @Expose
    public String invoice_date;
    @SerializedName("mobile_num")
    @Expose
    public String mobile_num;
    @SerializedName("to")
    @Expose
    public String to;
    @SerializedName("from_address")
    @Expose
    public String from_address;
    @SerializedName("items")
    @Expose
    //public List<InvoiceItem> items;
    public String items;
    @SerializedName("excluding_vat")
    @Expose
    public String exclude_vat;
    @SerializedName("vat_amount")
    @Expose
    public String vat_amount;
    @SerializedName("invoice_total")
    @Expose
    public String invoice_total;
    @SerializedName("payed_amount")
    @Expose
    public String payed_amount;
    @SerializedName("due_total")
    @Expose
    public String due_total;
//    @SerializedName("address")
//    @Expose
//    public String address;
    @SerializedName("comment")
    @Expose
    public String comment;
    @SerializedName("customer_id")
    @Expose
    public String customer_id;
    @SerializedName("preset1")
    @Expose
    public String preset1;
    @SerializedName("preset2")
    @Expose
    public String preset2;
    @SerializedName("created_at")
    @Expose
    public String created_at;
    @SerializedName("updated_at")
    @Expose
    public String updated_at;

    public Invoice(){
    }

    public Invoice(int id, String invoice_no, String email, String invoice_date, String mobile_num,
                   String to, String from_address, String items, String exclude_vat, String vat_amount,
                   String invoice_total, String payed_amount, String due_total, String comment, String customer_id,
                   String logo1, String logo2, String created_at, String updated_at) {
        this.id = id;
        this.invoice_no = invoice_no;
        this.email = email;
        this.invoice_date = invoice_date;
        this.mobile_num = mobile_num;
        this.to = to;
        this.from_address = from_address;
        this.items = items;
        this.exclude_vat = exclude_vat;
        this.vat_amount = vat_amount;
        this.invoice_total = invoice_total;
        this.payed_amount = payed_amount;
        this.due_total = due_total;
        this.comment = comment;
        this.customer_id = customer_id;
        this.preset1 = logo1;
        this.preset2 = logo2;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date =
                TimeUtils.formatDate(invoice_date, TimeUtils.UI_DATE_FORMAT, TimeUtils.DB_DATE_TIME_FORMAT);
    }

    public String getMobile_num() {
        return mobile_num;
    }

    public void setMobile_num(String mobile_num) {
        this.mobile_num = mobile_num;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom_address() {
        return from_address;
    }

    public void setFrom_address(String from_address) {
        this.from_address = from_address;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getExclude_vat() {
        return exclude_vat;
    }

    public void setExclude_vat(String exclude_vat) {
        this.exclude_vat = exclude_vat;
    }

    public String getVat_amount() {
        return vat_amount;
    }

    public void setVat_amount(String vat_amount) {
        this.vat_amount = vat_amount;
    }

    public String getInvoice_total() {
        return invoice_total;
    }

    public void setInvoice_total(String invoice_total) {
        this.invoice_total = invoice_total;
    }

    public String getPayed_amount() {
        return payed_amount;
    }

    public void setPayed_amount(String payed_amount) {
        this.payed_amount = payed_amount;
    }

    public String getDue_total() {
        return due_total;
    }

    public void setDue_total(String due_total) {
        this.due_total = due_total;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getLogo1() {
        return preset1;
    }

    public void setLogo1(String logo1) {
        this.preset1 = logo1;
    }

    public String getLogo2() {
        return preset2;
    }

    public void setLogo2(String logo2) {
        this.preset2 = logo2;
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
