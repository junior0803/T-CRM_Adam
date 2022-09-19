package com.bts.adamcrm.database;

import static com.bts.adamcrm.MyApplication.context;
import static com.bts.adamcrm.database.Constants.*;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.bts.adamcrm.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryQueryImplementation implements QueryContract.CategoryQuery{

    // Category Table Operation
    public void insertCategory(Category category, QueryResponse<Category> response){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = getContentValuesForCategory(category);

        try {
            long id = sqLiteDatabase.insertOrThrow(TABLE_CATEGORY, null, contentValues);
            if (id > 0) {
                response.onSuccess(category);
                category.setId((int) id);
            }
            else
                response.onFailure("Failed to create category. Unknown Reason!");
        } catch (SQLiteException e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }

    public void getAllCategories(QueryResponse<List<Category>> response){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.query(TABLE_CATEGORY, null, null, null, null, null, null, null);
            if(cursor != null && cursor.moveToFirst()){
                List<Category> CategoryList = new ArrayList<>();
                do {
                    Category category = getCategoryFromCursor(cursor);
                    CategoryList.add(category);

                }   while (cursor.moveToNext());
                response.onSuccess(CategoryList);
            } else {
                response.onFailure("There are no student in database");
            }
        } catch (Exception e){
            response.onFailure(e.getMessage());
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }
    }

    public void getCategoryById(int categoryId, QueryResponse<Category> response){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        Category category = null;
        try {
            cursor = sqLiteDatabase.query(TABLE_CATEGORY, null,
                    Constants.CUSTOMER_ID+ " = ? ", new String[]{String.valueOf(categoryId)},
                    null, null, null);

            if(cursor!=null && cursor.moveToFirst()){
                category = getCategoryFromCursor(cursor);
                response.onSuccess(category);
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

    public void updateCategoryInfo(Category category, QueryResponse<Boolean> response){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = getContentValuesForCategory(category);


        try {
            rowCount = sqLiteDatabase.update(TABLE_CATEGORY, contentValues,
                    Constants.CUSTOMER_ID + " = ? ", new String[] {String.valueOf(category.getId())});
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

    public void deleteCategoryById(int CategoryId, QueryResponse<Boolean> response) {
        long deletedRowCount = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            deletedRowCount = sqLiteDatabase.delete(TABLE_CATEGORY,
                    Constants.CUSTOMER_ID + " = ? ",
                    new String[]{ String.valueOf(CategoryId)});
            if (deletedRowCount > 0)
                response.onSuccess(true);
            else
                response.onFailure("Failed to delete category. Unknown reason");
        } catch (SQLiteException e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }

    public void deleteAllCategories(QueryResponse<Boolean> response){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            //for "1" delete() method returns number of deleted rows
            //if you don't want row count just use delete(TABLE_NAME, null, null)
            sqLiteDatabase.delete(TABLE_CATEGORY, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_CATEGORY);

            if(count==0) {
                response.onSuccess(true);
            } else
                response.onFailure("Failed to delete all categorys. Unknown reason");
        } catch (SQLiteException e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }

    private ContentValues getContentValuesForCategory(Category category){
        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.CATEGORY_ID, category.getId());
        contentValues.put(Constants.CATEGORY_NAME, category.getName());
        contentValues.put(Constants.CATEGORY_SORT, category.getSort());

        return contentValues;
    }

    private Category getCategoryFromCursor(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex(CATEGORY_ID));
        String name = cursor.getString(cursor.getColumnIndex(Constants.CATEGORY_NAME));
        int sort = cursor.getInt(cursor.getColumnIndex(Constants.CATEGORY_SORT));

        return new Category(id, name, sort);
    }
}
