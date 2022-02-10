package com.bts.adamcrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Invoice {
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("comment")
    @Expose
    public String comment;
    @SerializedName("due_total")
    @Expose
    public String due_total;
    @SerializedName("edt_company")
    @Expose
    public String edt_company;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("exclude_vat")
    @Expose
    public String exclude_vat;
    @SerializedName("file")
    @Expose
    public String file;
    @SerializedName("file2")
    @Expose
    public String file2;
    @SerializedName("file_address")
    @Expose
    public String file_address;
    @SerializedName("file_address2")
    @Expose
    public String file_address2;
    @SerializedName("include_total")
    @Expose
    public String include_total;
    @SerializedName("invoiceItemList")
    @Expose
    public List<InvoiceItem> invoiceItemList;
    @SerializedName("invoice_date")
    @Expose
    public String invoice_date;
    @SerializedName("invoice_no")
    @Expose
    public String invoice_no;
    @SerializedName("key")
    @Expose
    public String key;
    @SerializedName("logo")
    @Expose
    public String logo;
    @SerializedName("logo2")
    @Expose
    public String logo2;
    @SerializedName("mobile_no")
    @Expose
    public String mobile_no;
    @SerializedName("payed_amount")
    @Expose
    public String payed_amount;
    @SerializedName("vat_amount")
    @Expose
    public String vat_amount;

    public Invoice() {
    }

    public Invoice(String invoice_no, String fileaddr, String fileaddr2, String email, String date,
                   String mobile_no, String address, String company, List<InvoiceItem> list, String exclude_vat,
                   String vat_amount, String include_total, String payed_amount, String due_total,
                   String comment, String logo, String logo2, String file, String file2) {
        this.invoice_no = invoice_no;
        this.file_address = fileaddr;
        this.file_address2 = fileaddr2;
        this.email = email;
        this.invoice_date = date;
        this.mobile_no = mobile_no;
        this.address = address;
        this.edt_company = company;
        this.invoiceItemList = list;
        this.exclude_vat = exclude_vat;
        this.vat_amount = vat_amount;
        this.include_total = include_total;
        this.payed_amount = payed_amount;
        this.due_total = due_total;
        this.comment = comment;
        this.logo = logo;
        this.logo2 = logo2;
        this.file = file;
        this.file2 = file2;
    }

    public String getFile_address2() {
        return this.file_address2;
    }

    public void setFile_address2(String str) {
        this.file_address2 = str;
    }

    public String getFile2() {
        return this.file2;
    }

    public void setFile2(String str) {
        this.file2 = str;
    }

    public String getFile_address() {
        return this.file_address;
    }

    public void setFile_address(String str) {
        this.file_address = str;
    }

    public String getInvoice_no() {
        return this.invoice_no;
    }

    public void setInvoice_no(String str) {
        this.invoice_no = str;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public String getInvoice_date() {
        return this.invoice_date;
    }

    public void setInvoice_date(String str) {
        this.invoice_date = str;
    }

    public String getMobile_no() {
        return this.mobile_no;
    }

    public void setMobile_no(String str) {
        this.mobile_no = str;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public String getEdt_company() {
        return this.edt_company;
    }

    public void setEdt_company(String str) {
        this.edt_company = str;
    }

    public List<InvoiceItem> getInvoiceItemList() {
        return this.invoiceItemList;
    }

    public void setInvoiceItemList(List<InvoiceItem> list) {
        this.invoiceItemList = list;
    }

    public String getLogo2() {
        return this.logo2;
    }

    public void setLogo2(String str) {
        this.logo2 = str;
    }

    public String getExclude_vat() {
        return this.exclude_vat;
    }

    public void setExclude_vat(String str) {
        this.exclude_vat = str;
    }

    public String getVat_amount() {
        return this.vat_amount;
    }

    public void setVat_amount(String str) {
        this.vat_amount = str;
    }

    public String getInclude_total() {
        return this.include_total;
    }

    public void setInclude_total(String str) {
        this.include_total = str;
    }

    public String getPayed_amount() {
        return this.payed_amount;
    }

    public void setPayed_amount(String str) {
        this.payed_amount = str;
    }

    public String getDue_total() {
        return this.due_total;
    }

    public void setDue_total(String str) {
        this.due_total = str;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String str) {
        this.comment = str;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String str) {
        this.key = str;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String str) {
        this.logo = str;
    }

    public String getFile() {
        return this.file;
    }

    public void setFile(String str) {
        this.file = str;
    }
}
