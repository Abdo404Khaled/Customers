package com.example.customers.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customers.Adapters.CustomerAdapter;
import com.example.customers.Models.Customer;
import com.example.customers.Models.Database.DatabaseHelper;
import com.example.customers.Presenter.MainActivityPresenter;
import com.example.customers.R;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements iMainActivity.view{

    @Inject
    DatabaseHelper db;

    @Inject
    @Named ("addDiag")
    Dialog dialog;

    @Inject
    @Named ("userDiag")
    Dialog userDialog;

    @Inject
    MainActivityPresenter presenter;
    // ********************************Add Customer Dialog*************************************
    private EditText customerName;
    private EditText customerCode;
    private EditText customerMobile;
    private EditText customerAddress;

    private TextView locationLatitude;
    private TextView locationLongitude;

    private Button updateCoordinates;
    private Button confirmBtn;

    private Location new_customer_location;

    private boolean updated;
    // *********************************************************************

    // ********************************Customer Details Dialog*************************************
    private TextView currentCustomerName;
    private TextView currentCustomerCode;
    private TextView currentCustomerMobile;
    private TextView currentCustomerAddress;

    private Button currentCustomerLocation;
    private Button deleteCurrentCustomer;
    // *********************************************************************

    // ********************************Main Activity*************************************
    private CustomerAdapter cAdp;
    private RecyclerView rv;
    private List<Customer> allCustomers;
    private ActionBar bar;
    private SwipeRefreshLayout refresh;
    // *********************************************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.addUserBtn:
                showCustomDialogAddUser();
                break;
            case R.id.refreshUserBtn:
                presenter.refreshList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ********************************Action Bar*************************************
        bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        bar.setElevation(0.0f);
        setTitle(Html.fromHtml("<font color='#000000'>Customers </font>"));
        // *********************************************************************
        presenter.setView(this);
        // *********************************************************************
        rv = findViewById(R.id.customerList);
        cAdp = new CustomerAdapter(presenter);
        rv.setAdapter(cAdp);
        rv.setLayoutManager(new LinearLayoutManager(this));
        allCustomers = presenter.getAllCustomers();
        cAdp.setCustomers(allCustomers);
        // *********************************************************************
        refresh = findViewById(R.id.pullToRefresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {@Override public void onRefresh() {presenter.refreshList();refresh.setRefreshing(false);}});

    }

    @Override
    public void showCustomDialogAddUser() {
        try{
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.add_customer_dialog);
        }catch (AndroidRuntimeException e){
            onCloseDialog();
        }
        // *********************************************************************
        customerName = dialog.findViewById(R.id.NameText);
        customerCode = dialog.findViewById(R.id.CodeText);
        customerMobile = dialog.findViewById(R.id.MobileText);
        customerAddress = dialog.findViewById(R.id.AddressText);
        // *********************************************************************
        updateCoordinates = dialog.findViewById(R.id.coordinatesBtn);
        confirmBtn = dialog.findViewById(R.id.confirmBtn);

        updateCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(customerAddress.getText().toString().isEmpty())
                    onError("Address is Empty");
                else{
                    new_customer_location = presenter.updateCoordinates(customerAddress.getText().toString());
                    updated = true;
                }
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(
                        customerName.getText().toString().isEmpty() ||
                                customerCode.getText().toString().isEmpty() ||
                                customerMobile.getText().toString().isEmpty() ||
                                customerAddress.getText().toString().isEmpty()
                )
                    onError("Please fill all the information");
                else if (!updated)
                    onError("Please update the coordinates at least once");
                else{
                    Customer new_customer = new Customer(
                            customerName.getText().toString(),
                            Integer.valueOf(customerCode.getText().toString()),
                            Integer.valueOf(customerMobile.getText().toString()),
                            customerAddress.getText().toString(),
                            new_customer_location
                    );
                    presenter.addCustomer(new_customer);
                    dialog.dismiss();
                    onCloseDialog();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void showCustomDialogCustomerDetails(Customer customer) {
        String name = customer.getName();
        int code = customer.getCode();
        int mobile = customer.getMobile();
        String address = customer.getAddress();
        double latitude = customer.getLocation().getLatitude();
        double longitude = customer.getLocation().getLongitude();
        // *********************************************************************
        try{
            userDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            userDialog.setCancelable(true);
            userDialog.setContentView(R.layout.customer_details_dialog);
        }catch (AndroidRuntimeException e){}
        // *********************************************************************
        currentCustomerName = userDialog.findViewById(R.id.textView2);
        currentCustomerCode = userDialog.findViewById(R.id.textView3);
        currentCustomerMobile = userDialog.findViewById(R.id.textView4);
        currentCustomerAddress = userDialog.findViewById(R.id.textView5);
        currentCustomerLocation = userDialog.findViewById(R.id.button);
        deleteCurrentCustomer = userDialog.findViewById(R.id.button2);
        // *********************************************************************
        currentCustomerName.setText("Customer Name: " + name);
        currentCustomerCode.setText("Customer Code: " + code) ;
        currentCustomerMobile.setText("Customer Mobile: " + mobile);
        currentCustomerAddress.setText("Customer Address: " + address);
        // *********************************************************************
        currentCustomerLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.showOnMaps("geo:" + latitude + ", " + longitude + "?q=" + address);
            }
        });
        deleteCurrentCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.deleteCustomer(customer);
                userDialog.cancel();
            }
        });

        userDialog.show();
    }

    @Override
    public void onShowingMaps(Uri uri) {
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh(List<Customer> newList) {
        rv = findViewById(R.id.customerList);
        rv.setAdapter(cAdp);
        rv.setLayoutManager(new LinearLayoutManager(this));
        allCustomers = presenter.getAllCustomers();
        cAdp.setCustomers(allCustomers);
    }

    @Override
    public void onCloseDialog() {
        customerName = dialog.findViewById(R.id.NameText);
        customerCode = dialog.findViewById(R.id.CodeText);
        customerMobile = dialog.findViewById(R.id.MobileText);
        customerAddress = dialog.findViewById(R.id.AddressText);
        locationLatitude = dialog.findViewById(R.id.LatitudeText);
        locationLongitude = dialog.findViewById(R.id.LongitudeText);
        // *********************************************************************
        customerName.setText("");
        customerCode.setText("");
        customerMobile.setText("");
        customerAddress.setText("");
        locationLatitude.setText("Latitude:");
        locationLongitude.setText("Longitude:");
        // *********************************************************************
        new_customer_location = null;

    }


    @Override
    public void onCoordinatesUpdate(double latitude, double longitude) {
        locationLatitude = dialog.findViewById(R.id.LatitudeText);
        locationLongitude = dialog.findViewById(R.id.LongitudeText);
        // *********************************************************************
        locationLatitude.setText("Latitude: " + String.valueOf(latitude));
        locationLongitude.setText("Longitude: " + String.valueOf(longitude));
    }

}