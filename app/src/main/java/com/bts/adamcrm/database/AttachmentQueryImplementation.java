package com.bts.adamcrm.database;

import static com.bts.adamcrm.MyApplication.context;
import static com.bts.adamcrm.database.Constants.ATTACHMENT_ID;
import static com.bts.adamcrm.database.Constants.TABLE_ATTACHMENT;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.bts.adamcrm.model.Attachment;

import java.util.ArrayList;
import java.util.List;

public class AttachmentQueryImplementation implements QueryContract.AttachmentQuery {

    // Attachment Table Operation
    public void insertAttachment(Attachment attachment, QueryResponse<Boolean> response) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = getContentValuesForAttachment(attachment);

        try {
            long id = sqLiteDatabase.insertOrThrow(TABLE_ATTACHMENT, null, contentValues);
            if(id > 0) {
                response.onSuccess(true);
                attachment.setId((int) id);
            }
            else
                response.onFailure("Failed to create attachment. Unknown Reason!");
        } catch (SQLiteException e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }

    public void getAllAttachments(QueryResponse<List<Attachment>> response){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.query(TABLE_ATTACHMENT, null, null, null, null, null, null, null);
            if(cursor != null && cursor.moveToFirst()){
                List<Attachment> AttachmentList = new ArrayList<>();
                do {
                    Attachment attachment = getAttachmentFromCursor(cursor);
                    AttachmentList.add(attachment);

                }   while (cursor.moveToNext());
                response.onSuccess(AttachmentList);
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

    public void getAttachmentById(int attachmentId, QueryResponse<Attachment> response){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        Attachment attachment = null;
        try {
            cursor = sqLiteDatabase.query(TABLE_ATTACHMENT, null,
                    Constants.CUSTOMER_ID+ " = ? ", new String[]{String.valueOf(attachmentId)},
                    null, null, null);

            if(cursor!=null && cursor.moveToFirst()){
                attachment = getAttachmentFromCursor(cursor);
                response.onSuccess(attachment);
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

    public void updateAttachmentInfo(Attachment attachment, QueryResponse<Boolean> response){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = getContentValuesForAttachment(attachment);


        try {
            rowCount = sqLiteDatabase.update(TABLE_ATTACHMENT, contentValues,
                    Constants.CUSTOMER_ID + " = ? ", new String[] {String.valueOf(attachment.getId())});
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

    public void deleteAttachmentById(int AttachmentId, QueryResponse<Boolean> response) {
        long deletedRowCount = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            deletedRowCount = sqLiteDatabase.delete(TABLE_ATTACHMENT,
                    Constants.CUSTOMER_ID + " = ? ",
                    new String[]{ String.valueOf(AttachmentId)});
            if (deletedRowCount > 0)
                response.onSuccess(true);
            else
                response.onFailure("Failed to delete attachment. Unknown reason");
        } catch (SQLiteException e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }

    public void deleteAllAttachments(QueryResponse<Boolean> response){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            //for "1" delete() method returns number of deleted rows
            //if you don't want row count just use delete(TABLE_NAME, null, null)
            sqLiteDatabase.delete(TABLE_ATTACHMENT, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_ATTACHMENT);

            if(count==0) {
                response.onSuccess(true);
            } else
                response.onFailure("Failed to delete all attachments. Unknown reason");
        } catch (SQLiteException e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }

    private ContentValues getContentValuesForAttachment(Attachment attachment){
        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.ATTACHMENT_ID, attachment.getId());
        contentValues.put(Constants.ATTACHMENT_FILE, attachment.getFile_path());

        return contentValues;
    }

    private Attachment getAttachmentFromCursor(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex(ATTACHMENT_ID));
        String file_path = cursor.getString(cursor.getColumnIndex(Constants.ATTACHMENT_FILE));

        return new Attachment(id, file_path);
    }
}
