package com.bts.adamcrm.database;

import static com.bts.adamcrm.MyApplication.context;
import static com.bts.adamcrm.database.Constants.TABLE_CUSTOMER;
import static com.bts.adamcrm.database.Constants.TABLE_INVOICE;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.bts.adamcrm.model.Invoice;

import java.util.ArrayList;
import java.util.List;

public class InvoiceQueryImplementation implements QueryContract.InvoiceQuery{
    // invoice Table insert
    public void insertInvoice(Invoice invoice, QueryResponse<Invoice> response) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = getContentValuesForInvoice(invoice);

        try {
            long id = sqLiteDatabase.insertOrThrow(TABLE_INVOICE, null, contentValues);
            if (id > 0) {
                response.onSuccess(invoice);
                invoice.setId((int) id);
            }
            else
                response.onFailure("Failed to create invoice. Unknown Reason!");
        } catch (SQLiteException e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }

    public void getAllInvoices(QueryResponse<List<Invoice>> response){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.query(TABLE_INVOICE, null, null, null, null, null, null, null);
            if(cursor != null && cursor.moveToFirst()){
                List<Invoice> invoiceList = new ArrayList<>();
                do {
                    Invoice invoice = getInvoiceFromInvoice(cursor);
                    invoiceList.add(invoice);
                }   while (cursor.moveToNext());
                response.onSuccess(invoiceList);
            } else {
                response.onFailure("There are no invoice in database");
            }
        } catch (Exception e){
            response.onFailure(e.getMessage());
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }
    }

    public void getInvoiceById(int customerId, QueryResponse<List<Invoice>> response){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.query(TABLE_CUSTOMER, null,
                    Constants.INVOICE_CUSTOMER_ID+ " = ? ", new String[]{String.valueOf(customerId)},
                    null, null, null);
            if(cursor != null && cursor.moveToFirst()){
                List<Invoice> invoiceList = new ArrayList<>();
                do {
                    Invoice invoice = getInvoiceFromInvoice(cursor);
                    invoiceList.add(invoice);
                }   while (cursor.moveToNext());
                response.onSuccess(invoiceList);
            } else {
                response.onFailure("Invoice not found with this ID in database");
            }
        } catch (Exception e){
            response.onFailure(e.getMessage());
        } finally {
            if (cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }
    }

    public void updateInvoiceInfo(Invoice invoice, QueryResponse<Boolean> response){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = getContentValuesForInvoice(invoice);

        try {
            rowCount = sqLiteDatabase.update(TABLE_INVOICE, contentValues,
                    Constants.INVOICE_CUSTOMER_ID + " = ? ", new String[] {String.valueOf(invoice.getId())});
            if (rowCount > 0)
                response.onSuccess(true);
            else
                response.onFailure("No data is updated at all");
        } catch (SQLiteException e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }

    public void deleteInvoiceById(int id, QueryResponse<Boolean> response) {
        long deletedRowCount = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            deletedRowCount = sqLiteDatabase.delete(TABLE_CUSTOMER,
                    Constants.INVOICE_ID + " = ? ",
                    new String[]{ String.valueOf(id)});
            if (deletedRowCount > 0)
                response.onSuccess(true);
            else
                response.onFailure("Failed to delete invoice. Unknown reason");
        } catch (SQLiteException e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }

    public void deleteAllInvoices(QueryResponse<Boolean> response){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            //for "1" delete() method returns number of deleted rows
            //if you don't want row count just use delete(TABLE_NAME, null, null)
            sqLiteDatabase.delete(TABLE_INVOICE, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_INVOICE);
            if(count==0) {
                response.onSuccess(true);
            } else
                response.onFailure("Failed to delete all invoices. Unknown reason");
        } catch (SQLiteException e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }

    private ContentValues getContentValuesForInvoice(Invoice invoice){
        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.INVOICE_NO, invoice.getInvoice_no());
        contentValues.put(Constants.INVOICE_EMAIL, invoice.getEmail());
        contentValues.put(Constants.INVOICE_DATE, invoice.getInvoice_date());
        contentValues.put(Constants.INVOICE_MOBILE, invoice.getMobile_num());
        contentValues.put(Constants.INVOICE_TO_ADDR, invoice.getTo());
        contentValues.put(Constants.INVOICE_FROM_ADDR, invoice.getFrom_address());
        contentValues.put(Constants.INVOICE_ITEMS, invoice.getItems());
        contentValues.put(Constants.INVOICE_EXCLUDING_VAT, invoice.getExclude_vat());
        contentValues.put(Constants.INVOICE_VAT_AMOUNT, invoice.getVat_amount());
        contentValues.put(Constants.INVOICE_TOTAL, invoice.getInvoice_total());
        contentValues.put(Constants.INVOICE_PAYED, invoice.getPayed_amount());
        contentValues.put(Constants.INVOICE_DUE, invoice.getDue_total());
        contentValues.put(Constants.INVOICE_COMMENT, invoice.getComment());
        contentValues.put(Constants.INVOICE_CUSTOMER_ID, invoice.getCustomer_id());
        contentValues.put(Constants.INVOICE_PRESET1, invoice.getLogo1());
        contentValues.put(Constants.INVOICE_PRESET2, invoice.getLogo2());

        return contentValues;
    }

    private Invoice getInvoiceFromInvoice(Cursor cursor){
        String invoice_no = cursor.getString(cursor.getColumnIndex(Constants.INVOICE_NO));
        String email = cursor.getString(cursor.getColumnIndex(Constants.INVOICE_EMAIL));
        String invoice_date = cursor.getString(cursor.getColumnIndex(Constants.INVOICE_DATE));
        String mobile_num = cursor.getString(cursor.getColumnIndex(Constants.INVOICE_MOBILE));
        String to_addr = cursor.getString(cursor.getColumnIndex(Constants.INVOICE_TO_ADDR));
        String from_addr = cursor.getString(cursor.getColumnIndex(Constants.INVOICE_FROM_ADDR));
        String items = cursor.getString(cursor.getColumnIndex(Constants.INVOICE_ITEMS));
        String exclude_vat = cursor.getString(cursor.getColumnIndex(Constants.INVOICE_EXCLUDING_VAT));
        String vat_amount = cursor.getString(cursor.getColumnIndex(Constants.INVOICE_VAT_AMOUNT));
        String invoice_total = cursor.getString(cursor.getColumnIndex(Constants.INVOICE_TOTAL));
        String payed_amount = cursor.getString(cursor.getColumnIndex(Constants.INVOICE_PAYED));
        String due_total = cursor.getString(cursor.getColumnIndex(Constants.INVOICE_DUE));
        String comment = cursor.getString(cursor.getColumnIndex(Constants.INVOICE_COMMENT));
        int customer_id = cursor.getInt(cursor.getColumnIndex(Constants.INVOICE_CUSTOMER_ID));
        String preset1 = cursor.getString(cursor.getColumnIndex(Constants.INVOICE_PRESET1));
        String preset2 = cursor.getString(cursor.getColumnIndex(Constants.INVOICE_PRESET2));

        return new Invoice(invoice_no, email, invoice_date, mobile_num, to_addr, from_addr, items,
                exclude_vat, vat_amount, invoice_total, payed_amount, due_total, comment,
                customer_id, preset1, preset2);
    }
}
