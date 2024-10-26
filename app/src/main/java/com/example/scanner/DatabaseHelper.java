package com.example.scanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "scanner.db";
    private static final int DATABASE_VERSION = 1;

    // Transaction details table
    private static final String TABLE_TRANSACTIONS = "transaction_details";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_SELL_BY_DATE = "sellByDate";

    // Products table
    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_PRODUCT_NAME = "product_name";
    private static final String COLUMN_PRODUCT_QUANTITY = "product_quantity";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create transaction_details table
        String createTransactionTable = "CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_QUANTITY + " INTEGER, " +
                COLUMN_SELL_BY_DATE + " TEXT)";
        db.execSQL(createTransactionTable);

        // Create products table
        String createProductsTable = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCT_NAME + " TEXT, " +
                COLUMN_PRODUCT_QUANTITY + " INTEGER)";
        db.execSQL(createProductsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    // Insert a new transaction into transaction_details table
    public boolean insertTransaction(String name, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_QUANTITY, quantity);

        long result = db.insert(TABLE_TRANSACTIONS, null, contentValues);
        db.close();
        return result != -1; // Returns true if insert was successful
    }

    // Insert a new product into products table
    public boolean insertProduct(String productName, int productQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRODUCT_NAME, productName);
        contentValues.put(COLUMN_PRODUCT_QUANTITY, productQuantity);

        long result = db.insert(TABLE_PRODUCTS, null, contentValues);
        db.close();
        return result != -1; // Returns true if insert was successful
    }

    // Check if a transaction item exists
    public boolean itemExists(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_NAME + " = ?", new String[]{name});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Get quantity of a specific item in transaction_details
    public int getItemQuantity(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_QUANTITY + " FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_NAME + " = ?", new String[]{name});
        int quantity = 0;
        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(0);
        }
        cursor.close();
        return quantity;
    }

    // Update quantity of an item in transaction_details
    public void updateQuantity(String name, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_QUANTITY, newQuantity);
        db.update(TABLE_TRANSACTIONS, contentValues, COLUMN_NAME + " = ?", new String[]{name});
        db.close();
    }

    // Get all transactions
    public Cursor getAllTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_TRANSACTIONS, null, null, null, null, null, null); // Use the correct table name here
    }


    // Delete a transaction by ID
    public void deleteTransaction(int idToDelete) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTIONS, COLUMN_ID + " = ?", new String[]{String.valueOf(idToDelete)});
        db.close();
    }

    // Get all products
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
    }

    public boolean insertOrUpdateProduct(String productName, int productQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if product already exists
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCT_NAME + " = ?", new String[]{productName});
        if (cursor.moveToFirst()) {
            // Product exists, update quantity
            int currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_QUANTITY));
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_PRODUCT_QUANTITY, currentQuantity + productQuantity);
            db.update(TABLE_PRODUCTS, contentValues, COLUMN_PRODUCT_NAME + " = ?", new String[]{productName});
            cursor.close();
            db.close();
            return true;
        } else {
            // Product does not exist, insert new entry
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_PRODUCT_NAME, productName);
            contentValues.put(COLUMN_PRODUCT_QUANTITY, productQuantity);
            long result = db.insert(TABLE_PRODUCTS, null, contentValues);
            cursor.close();
            db.close();
            return result != -1;
        }
    }
}