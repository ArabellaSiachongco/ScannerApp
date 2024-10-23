package com.example.scanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "transactions.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "transaction_details";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_QUANTITY + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to check if an item already exists in the table
    public boolean itemExists(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = ?", new String[]{name});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Method to get the current quantity of an item
    public int getItemQuantity(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_QUANTITY + " FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = ?", new String[]{name});
        int quantity = 0;
        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(0);
        }
        cursor.close();
        return quantity;
    }

    // Method to update the quantity of an existing item
    public void updateQuantity(String name, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_QUANTITY, newQuantity);
        db.update(TABLE_NAME, contentValues, COLUMN_NAME + " = ?", new String[]{name});
    }

    // Method to insert a new item
    public boolean insertTransaction(String name, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_QUANTITY, quantity);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;  // returns true if insert was successful
    }

    // Method to get all transactions
    public Cursor getAllTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public void deleteTransaction(int idToDelete) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("transaction_details", "id = ?", new String[]{String.valueOf(idToDelete)});
        db.close();
    }

}
