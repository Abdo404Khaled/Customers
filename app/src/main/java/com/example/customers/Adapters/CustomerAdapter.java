package com.example.customers.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.customers.Models.Customer;
import com.example.customers.Presenter.MainActivityPresenter;
import com.example.customers.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder>{
    private List<Customer> customers = new ArrayList<>();
    private MainActivityPresenter presenter;

    public CustomerAdapter(MainActivityPresenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item, parent, false);
        return new ViewHolder(view) ;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.CustomerItemName.setText(customers.get(position).getName());
        holder.CustomerItemCode.setText(String.valueOf(customers.get(position).getCode()));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.showCustomerData(customers.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CardView parent;
        private TextView CustomerItemName;
        private TextView CustomerItemCode;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.CustomerItem);
            CustomerItemName = itemView.findViewById(R.id.CustomerItemName);
            CustomerItemCode = itemView.findViewById(R.id.CustomerItemCode);
        }
    }
}

