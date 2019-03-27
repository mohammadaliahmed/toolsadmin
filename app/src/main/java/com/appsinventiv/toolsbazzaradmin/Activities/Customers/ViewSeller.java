package com.appsinventiv.toolsbazzaradmin.Activities.Customers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Models.Customer;
import com.appsinventiv.toolsbazzaradmin.Models.VendorModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewSeller extends AppCompatActivity {
    DatabaseReference mDatabase;
    String customerId;
    TextView name, username, lastOrder, totalOrders, customerSince, customerName,
            type, storeName, email, shipping, city, country, telephone, mobile,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer);
        this.setTitle("Seller info");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        shipping = findViewById(R.id.shipping);
        city = findViewById(R.id.city);
        country = findViewById(R.id.country);
        telephone = findViewById(R.id.telephone);
        mobile = findViewById(R.id.mobile);
        country = findViewById(R.id.country);
        lastOrder = findViewById(R.id.lastOrder);
        totalOrders = findViewById(R.id.totalOrders);
        customerSince = findViewById(R.id.customerSince);
        customerName = findViewById(R.id.customerName);
        type = findViewById(R.id.type);
        storeName = findViewById(R.id.storeName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        customerId = getIntent().getStringExtra("customerId");
        getDataFromDB(customerId);
    }

    private void getDataFromDB(final String customerId) {
        mDatabase.child("Sellers").child(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    SellerModel customer = dataSnapshot.getValue(SellerModel.class);
                    if (customer != null) {
                        ViewSeller.this.setTitle("Customer: " + customer.getVendorName());
                        name.setText(customer.getVendorName());
                        username.setText(customer.getVendorId());
                        shipping.setText(customer.getCity());
                        city.setText(customer.getCity());
                        country.setText(customer.getCountry());
                        telephone.setText(customer.getTelNumber());
                        mobile.setText(customer.getPhone());
//                        lastOrder.setText(customer.la());
                        customerSince.setText(CommonUtils.getFormattedDateOnly(customer.getTime()));
                        type.setText(customer.getCustomerType());
                        email.setText(customer.getEmail());
                        storeName.setText(customer.getStoreName());
                        customerName.setText(customer.getVendorName());
                        password.setText(customer.getPassword());

//                        totalOrders.setText(""+dataSnapshot.child("Orders").getChildrenCount());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
