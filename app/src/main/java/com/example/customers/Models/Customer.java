package com.example.customers.Models;

import android.location.Address;
import android.location.Location;

public class Customer {
    private String name;
    private int code;
    private int mobile;
    private String address;
    private Location location;

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public int getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public Location getLocation() {
        return location;
    }

    public Customer(String name, int code, int mobile, String address, Location location) {
        this.name = name;
        this.code = code;
        this.mobile = mobile;
        this.address = address;
        this.location = location;
    }
}
