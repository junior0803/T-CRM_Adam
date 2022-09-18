package com.bts.adamcrm.model;

import com.bts.adamcrm.util.TimeUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Customer {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("mobile_phone")
    @Expose
    public String mobile_phone;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("town")
    @Expose
    public String town;
    @SerializedName("postal_code")
    @Expose
    public String postal_code;
    @SerializedName("further_note")
    @Expose
    public String further_note;

    @SerializedName("state")
    @Expose
    public int state;
    @SerializedName("remind_date")
    @Expose
    public String reminder_date;
    @SerializedName("category_id")
    @Expose
    public int category_id;
    @SerializedName("sms_sent")
    @Expose
    public int sms_sent;
    @SerializedName("attached_files")
    @Expose
    public String attached_files;
    @SerializedName("created_at")
    @Expose
    public String Date_created;
    @SerializedName("updated_at")
    @Expose
    public String Date_updated;

    public Customer() {
    }

    public Customer(int id, String title, String mobile_phone, String email, String name,
                    String address, String town, String postal_code, String further_note,
                    int state, String reminder_date, int category_id, int sms_sent,
                    String attached_files, String date_created, String date_updated) {
        this.id = id;
        this.title = title;
        this.mobile_phone = mobile_phone;
        this.email = email;
        this.name = name;
        this.address = address;
        this.town = town;
        this.postal_code = postal_code;
        this.further_note = further_note;
        this.state = state;
        this.reminder_date = reminder_date;
        this.category_id = category_id;
        this.sms_sent = sms_sent;
        this.attached_files = attached_files;
        Date_created = date_created;
        Date_updated = date_updated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getFurther_note() {
        return further_note;
    }

    public void setFurther_note(String further_note) {
        this.further_note = further_note;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getReminder_date() {
        return reminder_date;
    }

    public void setReminder_date(String reminder_date) {
        this.reminder_date =
                TimeUtils.formatDate(reminder_date, TimeUtils.UI_DATE_TIME_FORMAT, TimeUtils.DB_DATE_TIME_FORMAT);
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getSms_sent() {
        return sms_sent;
    }

    public void setSms_sent(int sms_sent) {
        this.sms_sent = sms_sent;
    }

    public String getAttached_files() {
        return attached_files;
    }

    public void setAttached_files(String attached_files) {
        this.attached_files = attached_files;
    }

    public int isSms_sent() {
        return sms_sent;
    }

    public String getDate_created() {
        return Date_created;
    }

    public void setDate_created(String date_created) {
        Date_created =
                TimeUtils.formatDate(date_created, TimeUtils.UI_DATE_TIME_FORMAT, TimeUtils.DB_DATE_TIME_FORMAT);
    }

    public String getDate_updated() {
        return Date_updated;
    }

    public void setDate_updated(String date_updated) {
        Date_updated =
                TimeUtils.formatDate(date_updated, TimeUtils.UI_DATE_TIME_FORMAT, TimeUtils.DB_DATE_TIME_FORMAT);
    }
}
