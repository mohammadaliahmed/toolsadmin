package com.appsinventiv.toolsbazzaradmin.Activities.Purchases;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Orders.Orders;
import com.appsinventiv.toolsbazzaradmin.Adapters.PurchaseOrderAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.ProductCountModel;
import com.appsinventiv.toolsbazzaradmin.Models.PurchaseOrderModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewPurchaseOrder extends AppCompatActivity {
    long purchaseOrder;
    DatabaseReference mDatabase;
    ArrayList<ProductCountModel> orderList = new ArrayList<>();
    TextView poNumber, date, address, total, storeAddress;
    RecyclerView recyclerview;
    RelativeLayout wholeLayout;
    PurchaseOrderModel model;
    PurchaseOrderAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_purchase_order);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent i = getIntent();
        purchaseOrder = i.getLongExtra("po", 0);
        this.setTitle("PO #: " + purchaseOrder);

        poNumber = findViewById(R.id.poNumber);
        date = findViewById(R.id.date);
        address = findViewById(R.id.address);
        total = findViewById(R.id.total);
        storeAddress = findViewById(R.id.storeAddress);
        recyclerview = findViewById(R.id.recyclerview);
        wholeLayout = findViewById(R.id.wholeLayout);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);
        getDataFromDb();

        adapter = new PurchaseOrderAdapter(this, orderList);
        recyclerview.setAdapter(adapter);

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


    private void getDataFromDb() {
        mDatabase.child("Purchases").child("PurchaseOrders").child("" + purchaseOrder).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    PurchaseOrderModel model = dataSnapshot.getValue(PurchaseOrderModel.class);
                    if (model != null) {
                        poNumber.setText("PO number: " + model.getId());
                        date.setText("" + CommonUtils.getFormattedDatee(model.getTime()));
                        total.setText("Total        Rs: " + model.getTotal());
                        orderList = model.getProductsList();
                        address.setText("Name: " + model.getVendor().getVendorName()
                                + "\nPhone: " + model.getVendor().getVendorPhone() + "\nAddress: " + model.getVendor().getVendorAddress());
                        adapter = new PurchaseOrderAdapter(ViewPurchaseOrder.this, orderList);
                        recyclerview.setAdapter(adapter);
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ViewPurchaseOrder.this, Orders.class);
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
            Intent i = new Intent(ViewPurchaseOrder.this, Orders.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
