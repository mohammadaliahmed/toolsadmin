package com.appsinventiv.toolsbazzaradmin.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Adapters.InvoiceAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.InvoiceModel;
import com.appsinventiv.toolsbazzaradmin.Models.ProductCountModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ViewInvoice extends AppCompatActivity {
    long invoiceNumber;
    DatabaseReference mDatabase;
    ArrayList<ProductCountModel> allProductsInOneOrderList = new ArrayList<>();
    ArrayList<ProductCountModel> availableProductsInOneOrderList = new ArrayList<>();
    TextView invoiceNumberText, date, address, delivery, total, storeAddress, orderNumber,grandTotal;
    RecyclerView recyclerView;
    InvoiceAdapter adapter;
    RelativeLayout wholeLayout;
    InvoiceModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invoice);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        invoiceNumberText = findViewById(R.id.invoiceNumber);
        date = findViewById(R.id.date);
        address = findViewById(R.id.address);
        delivery = findViewById(R.id.delivery);
        total = findViewById(R.id.total);
        recyclerView = findViewById(R.id.recyclerview);
        wholeLayout = findViewById(R.id.wholeLayout);
        storeAddress = findViewById(R.id.storeAddress);
        orderNumber = findViewById(R.id.orderNumber);
        grandTotal = findViewById(R.id.grandTotal);


        Intent i = getIntent();
        invoiceNumber = i.getLongExtra("invoiceNumber", 0);
        this.setTitle("Invoice # " + invoiceNumber);


        setUpRecycler();

        getInvoiceFromDb();
        getAddressFromDb();


    }

    private void getAddressFromDb() {
        mDatabase.child("Settings").child("AboutUs").child("contact").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    String contact = dataSnapshot.getValue(String.class);
                    storeAddress.setText(contact);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void getInvoiceFromDb() {
        mDatabase.child("Invoices").child("" + invoiceNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    model = dataSnapshot.getValue(InvoiceModel.class);
                    if (model != null) {

                        allProductsInOneOrderList = model.getCountModelArrayList();
                        availableProductsInOneOrderList = model.getNewCountModelArrayList();
                        invoiceNumberText.setText("Invoice # " + model.getId());
                        date.setText("" + CommonUtils.getFormattedDatee(model.getTime()));
                        delivery.setText("Delivery:        " + "Rs 40");
                        total.setText("Total:        Rs: " + model.getTotalPrice());
                        orderNumber.setText("Order number: " + model.getOrderId());
                        grandTotal.setText("Grand Total:        Rs: "+model.getGrandTotal());
                        address.setText("Name:  " + model.getCustomer().getName() + "\nPhone: " + model.getCustomer().getPhone() + "\nAddress:  " + model.getCustomer().getAddress() + ", " + model.getCustomer().getCity());
                        adapter = new InvoiceAdapter(ViewInvoice.this,
                                allProductsInOneOrderList,
                                availableProductsInOneOrderList,
                                model.getCustomer().getCustomerType());


                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        wholeLayout.setVisibility(View.GONE);

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void CalculateTotal() {

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ViewInvoice.this, Orders.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(ViewInvoice.this, Orders.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
