package com.bts.adamcrm.database;

import static com.bts.adamcrm.MyApplication.*;
import static com.bts.adamcrm.database.Constants.*;

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

public class CustomerQueryImplementation implements QueryContract.CustomerQuery{

    // Customer Table Operation
    public void insertCustomer(Customer customer, QueryResponse<Customer> response){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = getContentValuesForCustomer(customer);

        try {
            long id = sqLiteDatabase.insertOrThrow(TABLE_CUSTOMER, null, contentValues);
            if(id > 0) {
                response.onSuccess(customer);
                customer.setId((int) id);
            }
            else
                response.onFailure("Failed to create customer. Unknown Reason!");
        } catch (SQLiteException e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }

    public void getAllCustomers(QueryResponse<List<Customer>> response){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.query(TABLE_CUSTOMER, null, null, null, null, null, null, null);
            if(cursor != null && cursor.moveToFirst()){
                List<Customer> CustomerList = new ArrayList<>();
                do {
                    Customer customer = getCustomerFromCursor(cursor);
                    CustomerList.add(customer);

                }   while (cursor.moveToNext());
                response.onSuccess(CustomerList);
            } else {
                response.onFailure("There are no customer in database");
            }
        } catch (Exception e){
            response.onFailure(e.getMessage());
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }
    }

    public void getCustomerById(int customerId, QueryResponse<Customer> response){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        Customer customer = null;
        try {
            cursor = sqLiteDatabase.query(TABLE_CUSTOMER, null,
                    Constants.CUSTOMER_ID+ " = ? ", new String[]{String.valueOf(customerId)},
                    null, null, null);

            if(cursor!=null && cursor.moveToFirst()){
                customer = getCustomerFromCursor(cursor);
                response.onSuccess(customer);
            } else {
                response.onFailure("Customer not found with this ID in database");
            }
        } catch (Exception e){
            response.onFailure(e.getMessage());
        } finally {
            if (cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }
    }

    public void updateCustomerInfo(Customer customer, QueryResponse<Boolean> response){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = getContentValuesForCustomer(customer);

        try {
            rowCount = sqLiteDatabase.update(TABLE_CUSTOMER, contentValues,
                    Constants.CUSTOMER_ID + " = ? ", new String[] {String.valueOf(customer.getId())});
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

    public void deleteCustomerById(int CustomerId, QueryResponse<Boolean> response) {
        long deletedRowCount = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            deletedRowCount = sqLiteDatabase.delete(TABLE_CUSTOMER,
                    Constants.CUSTOMER_ID + " = ? ",
                    new String[]{ String.valueOf(CustomerId)});
            if (deletedRowCount > 0)
                response.onSuccess(true);
            else
                response.onFailure("Failed to delete customer. Unknown reason");
        } catch (SQLiteException e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }

    public void deleteAllCustomers(QueryResponse<Boolean> response){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            //for "1" delete() method returns number of deleted rows
            //if you don't want row count just use delete(TABLE_NAME, null, null)
            sqLiteDatabase.delete(TABLE_CUSTOMER, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_CUSTOMER);

            if(count==0) {
                response.onSuccess(true);
            } else
                response.onFailure("Failed to delete all customers. Unknown reason");
        } catch (SQLiteException e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }

    private ContentValues getContentValuesForCustomer(Customer customer){
        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.CUSTOMER_ID, customer.getId());
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

        return contentValues;
    }

    private Customer getCustomerFromCursor(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex(CUSTOMER_ID));
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

        return new Customer(id, title, mobile_phone, email, name, address, town, postal_code,
                further_note, state, remind_date, category_id, sms_sent, attached_files, created_at, updated_at);
    }
}
