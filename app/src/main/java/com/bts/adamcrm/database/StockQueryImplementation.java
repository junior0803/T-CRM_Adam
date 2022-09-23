package com.bts.adamcrm.database;

import static com.bts.adamcrm.MyApplication.context;
import static com.bts.adamcrm.database.Constants.STOCK_ID;
import static com.bts.adamcrm.database.Constants.TABLE_STOCK;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.bts.adamcrm.model.StockItem;

import java.util.ArrayList;
import java.util.List;

public class StockQueryImplementation implements QueryContract.StockQuery{
    
    // Stock Table Operation
    public void insertStock(StockItem stock, QueryResponse<StockItem> response) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = getContentValuesForStock(stock);

        try {
            long id = sqLiteDatabase.insertOrThrow(TABLE_STOCK, null, contentValues);
            if(id > 0) {
                response.onSuccess(stock);
                stock.setId((int) id);
            }
            else
                response.onFailure("Failed to create stock. Unknown Reason!");
        } catch (SQLiteException e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }

    public void getAllStocks(QueryResponse<List<StockItem>> response){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.query(TABLE_STOCK, null, null, null, null, null, null, null);
            if(cursor != null && cursor.moveToFirst()){
                List<StockItem> StockList = new ArrayList<>();
                do {
                    StockItem stock = getStockFromCursor(cursor);
                    StockList.add(stock);
                }   while (cursor.moveToNext());
                response.onSuccess(StockList);
            } else {
                response.onFailure("There are no stock in database");
            }
        } catch (Exception e){
            response.onFailure(e.getMessage());
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }
    }

    public void getStocksInParts(int is_shopping, int type, QueryResponse<List<StockItem>> response){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.query(TABLE_STOCK, null, "is_shopping=? and type=?",
                    new String[]{String.valueOf(is_shopping), String.valueOf(type)}, null, null, null, null);
            if(cursor != null && cursor.moveToFirst()){
                List<StockItem> StockList = new ArrayList<>();
                do {
                    StockItem stock = getStockFromCursor(cursor);
                    StockList.add(stock);
                }   while (cursor.moveToNext());
                response.onSuccess(StockList);
            } else {
                response.onFailure("There are no stock in database");
            }
        } catch (Exception e){
            response.onFailure(e.getMessage());
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }
    }

    public void getStockById(int stockId, QueryResponse<StockItem> response){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        StockItem stock = null;
        try {
            cursor = sqLiteDatabase.query(TABLE_STOCK, null,
                    Constants.CUSTOMER_ID+ " = ? ", new String[]{String.valueOf(stockId)},
                    null, null, null);

            if(cursor!=null && cursor.moveToFirst()){
                stock = getStockFromCursor(cursor);
                response.onSuccess(stock);
            } else {
                response.onFailure("Student not found with this ID in database");
            }
        } catch (Exception e){
            response.onFailure(e.getMessage());
        } finally {
            if (cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }
    }

    public void updateStockInfo(StockItem stock, QueryResponse<Boolean> response){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = getContentValuesForStock(stock);


        try {
            rowCount = sqLiteDatabase.update(TABLE_STOCK, contentValues,
                    Constants.CUSTOMER_ID + " = ? ", new String[] {String.valueOf(stock.getId())});
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

    public void deleteStockById(int StockId, QueryResponse<Boolean> response) {
        long deletedRowCount = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            deletedRowCount = sqLiteDatabase.delete(TABLE_STOCK,
                    Constants.CUSTOMER_ID + " = ? ",
                    new String[]{ String.valueOf(StockId)});
            if (deletedRowCount > 0)
                response.onSuccess(true);
            else
                response.onFailure("Failed to delete stock. Unknown reason");
        } catch (SQLiteException e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }

    public void deleteAllStocks(QueryResponse<Boolean> response){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            //for "1" delete() method returns number of deleted rows
            //if you don't want row count just use delete(TABLE_NAME, null, null)
            sqLiteDatabase.delete(TABLE_STOCK, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_STOCK);

            if(count==0) {
                response.onSuccess(true);
            } else
                response.onFailure("Failed to delete all stocks. Unknown reason");
        } catch (SQLiteException e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }

    private ContentValues getContentValuesForStock(StockItem stock){
        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.STOCK_ID, stock.getId());
        contentValues.put(Constants.STOCK_QUANTITY, stock.getQuantity());
        contentValues.put(Constants.STOCK_MINIMUM, stock.getMinimum_quantity());
        contentValues.put(Constants.STOCK_DESCRIPTION, stock.getDescription());
        contentValues.put(Constants.STOCK_PNO, stock.getPno());
        contentValues.put(Constants.STOCK_SHOPPING, stock.getIs_shopping());
        contentValues.put(Constants.STOCK_TYPE, stock.getType());

        return contentValues;
    }

    private StockItem getStockFromCursor(Cursor cursor){

        int id = cursor.getInt(cursor.getColumnIndex(STOCK_ID));
        String q = cursor.getString(cursor.getColumnIndex(Constants.STOCK_QUANTITY));
        String mq = cursor.getString(cursor.getColumnIndex(Constants.STOCK_MINIMUM));
        String description = cursor.getString(cursor.getColumnIndex(Constants.STOCK_DESCRIPTION));
        String pno = cursor.getString(cursor.getColumnIndex(Constants.STOCK_PNO));
        int is_shopping = cursor.getInt(cursor.getColumnIndex(Constants.STOCK_SHOPPING));
        int type = cursor.getInt(cursor.getColumnIndex(Constants.STOCK_TYPE));

        return new StockItem(id, q, mq, description, pno, is_shopping, type);
    }
}
