package com.bts.adamcrm.database;

public class Constants {
    //column names of Customer table
    public static final String TABLE_CUSTOMER = "customers";
    public static final String CUSTOMER_ID = "id";
    public static final String CUSTOMER_TITLE = "title";
    public static final String CUSTOMER_PHONE = "mobile_phone";
    public static final String CUSTOMER_EMAIL = "email";
    public static final String CUSTOMER_NAME = "name";
    public static final String CUSTOMER_ADDRESS = "address";
    public static final String CUSTOMER_TOWN = "town";
    public static final String CUSTOMER_CODE = "postal_code";
    public static final String CUSTOMER_NOTE = "further_note";
    public static final String CUSTOMER_STATE = "state";
    public static final String CUSTOMER_REMINDER = "remind_date";
    public static final String CUSTOMER_CATEGORY_ID = "category_id";
    public static final String CUSTOMER_SMS = "sms_sent";
    public static final String CUSTOMER_ATTACH = "attached_files";
    public static final String CUSTOMER_CREATE = "created_at";
    public static final String CUSTOMER_UPDATE = "updated_at";

    //column names of Category table
    public static final String TABLE_CATEGORY = "category";
    public static final String CATEGORY_ID = "id";
    public static final String CATEGORY_NAME = "name";
    public static final String CATEGORY_SORT = "sort";
    public static final String CATEGORY_CREATE = "created_at";
    public static final String CATEGORY_UPDATED = "updated_at";

    //column names of Attachment table
    public static final String TABLE_ATTACHMENT = "attachment";
    public static final String ATTACHMENT_ID = "id";
    public static final String ATTACHMENT_FILE = "file_path";

    // column names of Stock table
    public static final String TABLE_STOCK = "stock";
    public static final String STOCK_ID = "id";
    public static final String STOCK_QUANTITY = "q";
    public static final String STOCK_MINIMUM = "mq";
    public static final String STOCK_DESCRIPTION = "description";
    public static final String STOCK_PNO = "pno";
    public static final String STOCK_SHOPPING = "is_shopping";
    public static final String STOCK_TYPE = "type";
    public static final String STOCK_CREATE = "created_at";
    public static final String STOCK_UPDATE = "updated_at";

    // column name of Invoice table
    public static final String TABLE_INVOICE = "invoice";
    public static final String INVOICE_ID = "id";
    public static final String INVOICE_NO = "invoice_no";
    public static final String INVOICE_EMAIL = "email";
    public static final String INVOICE_DATE = "invoice_date";
    public static final String INVOICE_MOBILE = "mobile_num";
    public static final String INVOICE_TO_ADDR = "to";
    public static final String INVOICE_FROM_ADDR = "from_address";
    public static final String INVOICE_ITEMS = "items";
    public static final String INVOICE_EXCLUDING_VAT = "excluding_vat";
    public static final String INVOICE_VAT_AMOUNT = "vat_amount";
    public static final String INVOICE_TOTAL = "invoice_total";
    public static final String INVOICE_PAYED = "payed_amount";
    public static final String INVOICE_DUE = "due_total";
    public static final String INVOICE_COMMENT = "comment";
    public static final String INVOICE_CUSTOMER_ID = "customer_id";
    public static final String INVOICE_PRESET1 = "preset1";
    public static final String INVOICE_PRESET2 = "preset2";
}
