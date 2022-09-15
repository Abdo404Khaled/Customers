package com.example.customers.Models.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.customers.Models.Customer;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper implements iDatabaseHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, "customers.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlString = "CREATE TABLE CUSTOMERS (NAME TEXT, CODE INTEGER, MOBILE INTEGER, ADDRESS TEXT, LATITUDE REAL, LONGITUDE REAL)";
        sqLiteDatabase.execSQL(sqlString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public boolean addNewCustomer(Customer customer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv =  new ContentValues();

        cv.put("NAME", customer.getName());
        cv.put("CODE", customer.getCode());
        cv.put("MOBILE", customer.getMobile());
        cv.put("ADDRESS", customer.getAddress());
        cv.put("LATITUDE", customer.getLocation().getLatitude());
        cv.put("LONGITUDE", customer.getLocation().getLongitude());

        return db.insert("CUSTOMERS",null,cv) == -1 ? false : true;
    }

    @Override
    public List<Customer> getAllCustomers() {
        ArrayList<Customer> customersList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM CUSTOMERS";
        Cursor dummy = db.rawQuery(queryString,null);
        // *********************************************************************
        if(dummy.moveToFirst()){
            do{
                String name = dummy.getString(0);
                int code = dummy.getInt(1);
                int mobile = dummy.getInt(2);
                String address = dummy.getString(3);
                double latitude = dummy.getDouble(4);
                double longitude = dummy.getDouble(5);

                Location location = new Location(address);
                location.setLatitude(latitude);
                location.setLongitude(longitude);

                Customer newCustomer = new Customer(
                        name,
                        code,
                        mobile,
                        address,
                        location
                );
                customersList.add(newCustomer);
            }while(dummy.moveToNext());
        }else{}
        dummy.close();
        db.close();
        return customersList;
    }
}
