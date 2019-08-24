package com.example.demoproduct.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.demoproduct.model.Customers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lithin on 7/02/2019.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MilkProduct.db";

    // User table name
    private static final String TABLE_USER = "customers";

    // User table name
    private static final String TABLE_USER_LOAN_REQUEST = "user_loan_request";

    // User Table Columns names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_COMMISSION = "commission";
    private static final String COLUMN_CUSTOMERID= "customerId";
    private static final String COLUMN_CUSTOMERNAME ="customername";
    private static final String COLUMN_TMCommission ="tmcommission";

    //User Request Loan
    private static final String COLUMN_USER_LOAN_ID = "user_id";
    private static final String COLUMN_USER_LOAN_AMOUNT = "user_loan_amount";
    private static final String COLUMN_USER_LOAN_MOBILE_NO ="user_loan_mobileno";
    private static final String COLUMN_USER_LOAN_REPAYMENT ="user_loan_repayment";
    private static final String COLUMN_USER_LOAN_NATIONAL_ID = "user_national_id";


    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_COMMISSION + " TEXT,"
            + COLUMN_CUSTOMERID + " TEXT," + COLUMN_CUSTOMERNAME + " TEXT," + COLUMN_TMCommission + " TEXT" + ")";

    //create Loan Request for table

    // create table sql query
    private String CREATE_USER_LOAN_TABLE = "CREATE TABLE " + TABLE_USER_LOAN_REQUEST + "("
            + COLUMN_USER_LOAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_LOAN_AMOUNT + " TEXT,"
            + COLUMN_USER_LOAN_MOBILE_NO + " TEXT,"
            + COLUMN_USER_LOAN_REPAYMENT + " TEXT," + COLUMN_USER_LOAN_NATIONAL_ID + " TEXT" + ")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    // drop table sql query
    private String DROP_USER_LOAN_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER_LOAN_REQUEST;

    /**
     * Constructor
     * 
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_USER_LOAN_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);

        //Drop User Table if exist
        db.execSQL(DROP_USER_LOAN_TABLE);

        // Create tables again
        onCreate(db);

    }

    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(Customers user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_COMMISSION, user.getCommission());
        values.put(COLUMN_CUSTOMERID,user.getCustomerId());
        values.put(COLUMN_CUSTOMERNAME,user.getCustomerName());
        values.put(COLUMN_TMCommission, user.getTMCommission());
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }


    /**
     * This method is to create user record
     *
     * @param user
     */
//    public void addUserLoanRequest(ApplyLoan user) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_USER_LOAN_AMOUNT, user.getLoanamount());
//        values.put(COLUMN_USER_LOAN_MOBILE_NO,user.getMobilenumber());
//        values.put(COLUMN_USER_LOAN_REPAYMENT,user.getRepaymentperiod());
//        values.put(COLUMN_USER_LOAN_NATIONAL_ID, user.getNationalid());
//
//        // Inserting Row
//        db.insert(TABLE_USER_LOAN_REQUEST, null, values);
//        db.close();
//    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public ArrayList<Customers> getAllCustomers() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_ID,
                COLUMN_COMMISSION,
                COLUMN_CUSTOMERID,
                COLUMN_CUSTOMERNAME,
                COLUMN_TMCommission
        };
//        // sorting orders
//        String sortOrder =
//                COLUMN_CUSTOMERNAME + " ASC";
        ArrayList<Customers> userList = new ArrayList<Customers>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customers user = new Customers();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
                user.setCommission(cursor.getString(cursor.getColumnIndex(COLUMN_COMMISSION)));
                user.setCustomerId(cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMERID)));
                user.setCustomerName(cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMERNAME)));
                user.setTMCommission(cursor.getString(cursor.getColumnIndex(COLUMN_TMCommission)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    /**
     * This method to update user record
     *
     * @param user
     */
//    public void updateUser(User user) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_USER_NAME, user.getName());
//        values.put(COLUMN_USER_EMAIL, user.getEmail());
//        values.put(COLUMN_USER_PASSWORD, user.getPassword());
//
//        // updating row
//        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
//                new String[]{String.valueOf(user.getId())});
//        db.close();
//    }

    /**
     * This method is to delete user record
     *
     * @param user
     */
//    public void deleteUser(User user) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        // delete user record by id
//        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
//                new String[]{String.valueOf(user.getId())});
//        db.close();
//    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
//    public boolean checkUser(String email) {
//
//        // array of columns to fetch
//        String[] columns = {
//                COLUMN_USER_ID
//        };
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        // selection criteria
//        String selection = COLUMN_USER_EMAIL + " = ?";
//
//        // selection argument
//        String[] selectionArgs = {email};
//
//        // query user table with condition
//        /**
//         * Here query function is used to fetch records from user table this function works like we use sql query.
//         * SQL query equivalent to this query function is
//         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
//         */
//        Cursor cursor = db.query(TABLE_USER, //Table to query
//                columns,                    //columns to return
//                selection,                  //columns for the WHERE clause
//                selectionArgs,              //The values for the WHERE clause
//                null,                       //group the rows
//                null,                      //filter by row groups
//                null);                      //The sort order
//        int cursorCount = cursor.getCount();
//        cursor.close();
//        db.close();
//
//        if (cursorCount > 0) {
//            return true;
//        }
//
//        return false;
//    }

    public List<Customers> getCustomerid(int questionid) {
        String sql = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(questionid)};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        List<Customers> childList = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    Customers user = new Customers();
                    user.setCommission(cursor.getString(cursor.getColumnIndex(COLUMN_COMMISSION)));
                    user.setCustomerId(cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMERID)));
                    user.setCustomerName(cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMERNAME)));
                    user.setTMCommission(cursor.getString(cursor.getColumnIndex(COLUMN_TMCommission)));
                    childList.add(user);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return childList;
    }


    public ArrayList<Customers> getAllCustomersNamesList() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_CUSTOMERNAME
        };
        ArrayList<Customers> userList = new ArrayList<Customers>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customers user = new Customers();
                user.setCustomerName(cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMERNAME)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }




    public void deleteCustomers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.enableWriteAheadLogging(); // this will allow transactions from multiple threads.
        db.delete(TABLE_USER, null, null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='"+TABLE_USER+"';");
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param mobileno
     * @return true/false
     */
    public boolean checkUserLoanMobileNo(String mobileno) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_LOAN_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_LOAN_MOBILE_NO + " = ?";

        // selection argument
        String[] selectionArgs = {mobileno};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER_LOAN_REQUEST, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
//    public boolean checkUser(String email, String password) {
//
//        // array of columns to fetch
//        String[] columns = {
//                COLUMN_USER_ID
//        };
//        SQLiteDatabase db = this.getReadableDatabase();
//        // selection criteria
//        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
//
//        // selection arguments
//        String[] selectionArgs = {email, password};
//
//        // query user table with conditions
//        /**
//         * Here query function is used to fetch records from user table this function works like we use sql query.
//         * SQL query equivalent to this query function is
//         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
//         */
//        Cursor cursor = db.query(TABLE_USER, //Table to query
//                columns,                    //columns to return
//                selection,                  //columns for the WHERE clause
//                selectionArgs,              //The values for the WHERE clause
//                null,                       //group the rows
//                null,                       //filter by row groups
//                null);                      //The sort order
//
//        int cursorCount = cursor.getCount();
//
//        cursor.close();
//        db.close();
//        if (cursorCount > 0) {
//            return true;
//        }
//
//        return false;
//    }
}
