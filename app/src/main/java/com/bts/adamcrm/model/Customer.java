package com.bts.adamcrm.model;

import androidx.core.app.NotificationCompat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("attachments")
    @Expose
    List<Attachment> attachments = new ArrayList();
    @SerializedName("category")
    @Expose
    public Category category;
    @SerializedName("date_completed")
    @Expose
    public String date_completed;
    @SerializedName("date_created")
    @Expose
    public String date_created;
    @SerializedName("date_updated")
    @Expose
    public String date_updated;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("further_note")
    @Expose
    public String further_note;
    @SerializedName("invoices")
    @Expose
    List<Invoice> invoices = new ArrayList();
    @SerializedName("key")
    @Expose
    public String key;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("postal_code")
    @Expose
    public String postal_code;
    @SerializedName(NotificationCompat.CATEGORY_REMINDER)
    @Expose
    public boolean reminder = false;
    @SerializedName("reminder_date")
    @Expose
    public String reminder_date;
    @SerializedName("sms_sent")
    @Expose
    public boolean sms_sent = false;
    @SerializedName("sort")
    @Expose
    public int sort;
    @SerializedName(NotificationCompat.CATEGORY_STATUS)
    @Expose
    public int status;
    @SerializedName("task_title")
    @Expose
    public String task_title;
    @SerializedName("town")
    @Expose
    public String town;

    public Customer() {
    }

    public Customer(String title, String mobile, String email, String name, String address, String town,
                    String code, String createDate, String updateDate, String completeDate,
                    String note, int status, int sort, boolean reminder, String reminderDate,
                    Category category, boolean sentSms, List<Attachment> attachlist, List<Invoice> invoices) {
        this.task_title = title;
        this.mobile = mobile;
        this.email = email;
        this.name = name;
        this.address = address;
        this.town = town;
        this.postal_code = code;
        this.date_created = createDate;
        this.date_updated = updateDate;
        this.further_note = note;
        this.status = status;
        this.sort = sort;
        this.reminder = reminder;
        this.reminder_date = reminderDate;
        this.date_completed = completeDate;
        this.category = category;
        this.sms_sent = sentSms;
        this.attachments = attachlist;
        this.invoices = invoices;
    }

    public int getSort() {
        return this.sort;
    }

    public void setSort(int i) {
        this.sort = i;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String str) {
        this.key = str;
    }

    public String getTask_title() {
        return this.task_title;
    }

    public void setTask_title(String str) {
        this.task_title = str;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String str) {
        this.mobile = str;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public String getTown() {
        return this.town;
    }

    public void setTown(String str) {
        this.town = str;
    }

    public String getPostal_code() {
        return this.postal_code;
    }

    public void setPostal_code(String str) {
        this.postal_code = str;
    }

    public String getDate_created() {
        return this.date_created;
    }

    public void setDate_created(String str) {
        this.date_created = str;
    }

    public String getDate_completed() {
        return this.date_completed;
    }

    public void setDate_completed(String str) {
        this.date_completed = str;
    }

    public String getFurther_note() {
        return this.further_note;
    }

    public void setFurther_note(String str) {
        this.further_note = str;
    }

    public boolean isReminder() {
        return this.reminder;
    }

    public void setReminder(boolean z) {
        this.reminder = z;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public String getReminder_date() {
        return this.reminder_date;
    }

    public void setReminder_date(String str) {
        this.reminder_date = str;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category2) {
        this.category = category2;
    }

    public boolean isSms_sent() {
        return this.sms_sent;
    }

    public void setSms_sent(boolean z) {
        this.sms_sent = z;
    }

    public List<Attachment> getAttachments() {
        return this.attachments;
    }

    public void setAttachments(List<Attachment> list) {
        this.attachments = list;
    }

    public List<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(List<Invoice> list) {
        this.invoices = list;
    }

    public String getDate_updated() {
        return this.date_updated;
    }

    public void setDate_updated(String str) {
        this.date_updated = str;
    }
}
