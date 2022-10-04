package com.example.customers.Presenter;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.widget.Toast;

import com.example.customers.Models.Customer;
import com.example.customers.Models.Database.DatabaseHelper;
import com.example.customers.Views.MainActivity;
import com.example.customers.Views.iMainActivity;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;


public class MainActivityPresenter implements iMainActivity.Presenter {

    private DatabaseHelper db;
    private Geocoder geocoder;

    private iMainActivity.view view = null;


    @Inject
    public MainActivityPresenter(DatabaseHelper db,Geocoder geocoder){
        this.db = db;
        this.geocoder = geocoder;
    }

    @Override
    public void addCustomer(Customer customer) {
        if(db.addNewCustomer(customer))
            view.onSuccess("Customer Added");
        else
            view.onError("An error has been occured");
        refreshList();
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> allCustomers = db.getAllCustomers();
        return allCustomers;
    }

    @Override
    public void refreshList() {
        view.onRefresh(getAllCustomers());
    }

    @Override
    public Location updateCoordinates(String address) {
        List<Address> coordinates;
        Location new_customer_location = new Location(address);
        try{
            coordinates = geocoder.getFromLocationName(address,1);
            if(coordinates != null){
                new_customer_location.setLatitude(coordinates.get(0).getLatitude());
                new_customer_location.setLongitude(coordinates.get(0).getLongitude());
            }
        }catch (IOException e){
            view.onError("An error has been occured");
            e.printStackTrace();
            return null;
        }
        view.onCoordinatesUpdate(new_customer_location.getLatitude(), new_customer_location.getLongitude());
        return new_customer_location;
    }

    @Override
    public void showCustomerData(Customer customer) {
        view.showCustomDialogCustomerDetails(customer);
    }

    @Override
    public void showOnMaps(String uriString) {
        Uri uri =  Uri.parse(uriString);
        view.onShowingMaps(uri);
    }

    @Override
    public void deleteCustomer(Customer customer) {
        boolean success = db.deleteCustomer(customer);
        if(success)
            view.onSuccess("Customer deleted");
        else
            view.onError("Error has been occured");
        refreshList();
    }
    public void setView(iMainActivity.view view) {
        this.view = view;
    }

}
