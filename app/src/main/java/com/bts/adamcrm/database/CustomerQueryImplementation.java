package com.bts.adamcrm.database;

import static com.bts.adamcrm.MyApplication.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.bts.adamcrm.model.Customer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomerQueryImplementation {

    public CustomerQueryImplementation(Context context){
    }

    // Customer Table Operation
    public long insertCustomer(Customer customer){

        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.CUSTOMER_TITLE, customer.getTitle());
        contentValues.put(Constants.CUSTOMER_PHONE, customer.getMobile_phone());
        contentValues.put(Constants.CUSTOMER_EMAIL, customer.getEmail());
        contentValues.put(Constants.CUSTOMER_NAME, customer.getName());
        contentValues.put(Constants.CUSTOMER_ADDRESS, customer.getAddress());
        contentValues.put(Constants.CUSTOMER_TOWN, customer.getTown());
        contentValues.put(Constants.CUSTOMER_CODE, customer.getPostal_code());
        contentValues.put(Constants.CUSTOMER_NOTE, customer.getFurther_note());
        contentValues.put(Constants.CUSTOMER_STATE, customer.getState());
        contentValues.put(Constants.CUSTOMER_REMINDER, customer.getReminder_date());
        contentValues.put(Constants.CUSTOMER_CATEGORY_ID, customer.getCategory_id());
        contentValues.put(Constants.CUSTOMER_SMS, customer.getSms_sent());
        contentValues.put(Constants.CUSTOMER_ATTACH, customer.getAttached_files());
        contentValues.put(Constants.CUSTOMER_CREATE, customer.getDate_created());
        contentValues.put(Constants.CUSTOMER_UPDATE, customer.getDate_updated());

        try {
            id = sqLiteDatabase.insertOrThrow(Constants.TABLE_CUSTOMER, null, contentValues);
        } catch (SQLiteException e){
            Log.d("Adams", "Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }
        return id;
    }

    public List<Customer> getAllCustomer(){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.query(Constants.TABLE_CUSTOMER, null, null, null, null, null, null, null);

            if(cursor!=null)
                if(cursor.moveToFirst()){
                    List<Customer> CustomerList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Constants.CUSTOMER_ID));
                        String title = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_TITLE));
                        String mobile_phone = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_PHONE));
                        String email = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_EMAIL));
                        String name = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_NAME));
                        String address = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_ADDRESS));
                        String town = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_TOWN));
                        String postal_code = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_CODE));
                        String further_note = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_NOTE));
                        int state = cursor.getInt(cursor.getColumnIndex(Constants.CUSTOMER_STATE));
                        String remind_date = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_REMINDER));
                        int category_id = cursor.getInt(cursor.getColumnIndex(Constants.CUSTOMER_CATEGORY_ID));
                        int sms_sent = cursor.getInt(cursor.getColumnIndex(Constants.CUSTOMER_SMS));
                        String attached_files = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_ATTACH));
                        String created_at = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_CREATE));
                        String updated_at = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_UPDATE));

                        CustomerList.add(new Customer(id, title, mobile_phone, email, name, address, town, postal_code,
                                further_note, state, remind_date, category_id, sms_sent, attached_files, created_at, updated_at));

                    }   while (cursor.moveToNext());
                    return CustomerList;
                }
        } catch (Exception e){
            Log.d("Adam", "Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }
        return Collections.emptyList();
    }

    public Customer getStudentById(int id){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        Customer customer = null;
        try {

            cursor = sqLiteDatabase.query(Constants.TABLE_CUSTOMER, null,
                    Constants.CUSTOMER_ID+ " = ? ", new String[]{String.valueOf(id)},
                    null, null, null);

            if(cursor.moveToFirst()){
                String title = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_TITLE));
                String mobile_phone = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_PHONE));
                String email = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_EMAIL));
                String name = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_NAME));
                String address = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_ADDRESS));
                String town = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_TOWN));
                String postal_code = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_CODE));
                String further_note = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_NOTE));
                int state = cursor.getInt(cursor.getColumnIndex(Constants.CUSTOMER_STATE));
                String remind_date = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_REMINDER));
                int category_id = cursor.getInt(cursor.getColumnIndex(Constants.CUSTOMER_CATEGORY_ID));
                int sms_sent = cursor.getInt(cursor.getColumnIndex(Constants.CUSTOMER_SMS));
                String attached_files = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_ATTACH));
                String created_at = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_CREATE));
                String updated_at = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_UPDATE));

                customer = new Customer(id, title, mobile_phone, email, name, address, town, postal_code,
                        further_note, state, remind_date, category_id, sms_sent, attached_files, created_at, updated_at);
            }
        } catch (Exception e){
            Log.d("Adam", "Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }
        return customer;
    }

    public long updateCustomerInfo(Customer customer){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.CUSTOMER_TITLE, customer.getTitle());
        contentValues.put(Constants.CUSTOMER_PHONE, customer.getMobile_phone());
        contentValues.put(Constants.CUSTOMER_EMAIL, customer.getEmail());
        contentValues.put(Constants.CUSTOMER_NAME, customer.getName());
        contentValues.put(Constants.CUSTOMER_ADDRESS, customer.getAddress());
        contentValues.put(Constants.CUSTOMER_TOWN, customer.getTown());
        contentValues.put(Constants.CUSTOMER_CODE, customer.getPostal_code());
        contentValues.put(Constants.CUSTOMER_NOTE, customer.getFurther_note());
        contentValues.put(Constants.CUSTOMER_STATE, customer.getState());
        contentValues.put(Constants.CUSTOMER_REMINDER, customer.getReminder_date());
        contentValues.put(Constants.CUSTOMER_CATEGORY_ID, customer.getCategory_id());
        contentValues.put(Constants.CUSTOMER_SMS, customer.getSms_sent());
        contentValues.put(Constants.CUSTOMER_ATTACH, customer.getAttached_files());
        contentValues.put(Constants.CUSTOMER_CREATE, customer.getDate_created());
        contentValues.put(Constants.CUSTOMER_UPDATE, customer.getDate_updated());

        try {
            rowCount = sqLiteDatabase.update(Constants.TABLE_CUSTOMER, contentValues,
                    Constants.CUSTOMER_ID + " = ? ",
                    new String[] {String.valueOf(customer.getId())});
        } catch (SQLiteException e){
            Log.d("Adam", "Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }
        return rowCount;
    }

    public long deleteCustomerById(int id) {
        long deletedRowCount = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            deletedRowCount = sqLiteDatabase.delete(Constants.TABLE_CUSTOMER,
                    Constants.CUSTOMER_ID + " = ? ",
                    new String[]{ String.valueOf(id)});
        } catch (SQLiteException e){
            Log.d("Adam", "Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deletedRowCount;
    }

    public boolean deleteAllCustomers(){
        boolean deleteStatus = false;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            //for "1" delete() method returns number of deleted rows
            //if you don't want row count just use delete(TABLE_NAME, null, null)
            sqLiteDatabase.delete(Constants.TABLE_CUSTOMER, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Constants.TABLE_CUSTOMER);

            if(count==0)
                deleteStatus = true;

        } catch (SQLiteException e){
            Log.d("Adam", "Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }
        return deleteStatus;
    }
}
