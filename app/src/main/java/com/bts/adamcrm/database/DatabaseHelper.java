package com.bts.adamcrm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bts.adamcrm.MyApplication;
import static com.bts.adamcrm.database.Constants.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "customer-db";
    private static final int DATABASE_VERSION = 1;

    private DatabaseHelper() {
        super(MyApplication.context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final class DatabaseHelperHolder {
        static final DatabaseHelper databaseHelper = new DatabaseHelper();
    }

    public static DatabaseHelper getInstance(Context context) {
        return DatabaseHelperHolder.databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_CUSTOMER_TABLE = "CREATE TABLE " + TABLE_CUSTOMER + "("
                + CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CUSTOMER_TITLE + " TEXT NOT NULL, "
                + CUSTOMER_PHONE + " TEXT, "
                + CUSTOMER_EMAIL + " TEXT, " //nullable
                + CUSTOMER_NAME + " TEXT," //nullable
                + CUSTOMER_ADDRESS + " TEXT, " //nullable
                + CUSTOMER_TOWN + " TEXT, " //nullable
                + CUSTOMER_CODE + " TEXT, " //nullable
                + CUSTOMER_NOTE + " TEXT, " //nullable
                + CUSTOMER_STATE + " INTEGER, " //nullable
                + CUSTOMER_REMINDER + " TEXT, " //nullable
                + CUSTOMER_CATEGORY_ID + " INTEGER, " //nullable
                + CUSTOMER_SMS + " INTEGER, " //nullable
                + CUSTOMER_ATTACH + " TEXT, " //nullable
                + CUSTOMER_CREATE + " TEXT NOT NULL, " //nullable
                + CUSTOMER_UPDATE + " TEXT" //nullable
                + ")";

        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "("
                + CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CATEGORY_NAME + " TEXT NOT NULL, "
                + CATEGORY_SORT + " INTEGER NOT NULL, "
                + CATEGORY_CREATE + " TEXT, " //nullable
                + CATEGORY_UPDATED + " TEXT" //nullable
                + ")";

        String CREATE_ATTACHMENT_TABLE = "CREATE TABLE " + TABLE_ATTACHMENT + "("
                + ATTACHMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ATTACHMENT_FILE + " TEXT NOT NULL"
                + ")";

        String CREATE_STOCK_TABLE = "CREATE TABLE " + TABLE_STOCK + "("
                + STOCK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + STOCK_QUANTITY + " TEXT NOT NULL, "
                + STOCK_MINIMUM + " TEXT NOT NULL, "
                + STOCK_DESCRIPTION + " TEXT NOT NULL, "
                + STOCK_PNO + " TEXT NOT NULL, "
                + STOCK_SHOPPING + " INTEGER NOT NULL, "
                + STOCK_TYPE + " INTEGER NOT NULL, "
                + STOCK_CREATE + " TEXT, "
                + STOCK_UPDATE + "TEXT"
                + ")";

        sqLiteDatabase.execSQL(CREATE_CUSTOMER_TABLE);
        sqLiteDatabase.execSQL(CREATE_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(CREATE_ATTACHMENT_TABLE);
        sqLiteDatabase.execSQL(CREATE_STOCK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTACHMENT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        //enable foreign key constraints like ON UPDATE CASCADE, ON DELETE CASCADE
        db.execSQL("PRAGMA foreign_keys=ON;");
    }
}
