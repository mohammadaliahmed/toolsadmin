package com.appsinventiv.toolsbazzaradmin.Activities.Invoicing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appsinventiv.toolsbazzaradmin.Activities.Orders.Orders;
import com.appsinventiv.toolsbazzaradmin.Adapters.InvoiceAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.InvoiceModel;
import com.appsinventiv.toolsbazzaradmin.Models.LocationAndChargesModel;
import com.appsinventiv.toolsbazzaradmin.Models.ProductCountModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ViewInvoice extends AppCompatActivity {
    long invoiceNumber;
    DatabaseReference mDatabase;
    ArrayList<ProductCountModel> allProductsInOneOrderList = new ArrayList<>();
    ArrayList<ProductCountModel> availableProductsInOneOrderList = new ArrayList<>();
    TextView invoiceNumberText, date, address, delivery, total, storeAddress, orderNumber, grandTotal, shipping;
    RecyclerView recyclerView;
    InvoiceAdapter adapter;
    RelativeLayout wholeLayout;
    InvoiceModel model;
    RelativeLayout ll_linear;
    LocationAndChargesModel locationAndChargesModel;
    String locationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invoice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        ll_linear = findViewById(R.id.ll_linear);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        invoiceNumberText = findViewById(R.id.poNumber);
        date = findViewById(R.id.date);
        address = findViewById(R.id.address);
        delivery = findViewById(R.id.delivery);
        total = findViewById(R.id.total);
        recyclerView = findViewById(R.id.recyclerview);
        wholeLayout = findViewById(R.id.wholeLayout);
        storeAddress = findViewById(R.id.storeAddress);
        orderNumber = findViewById(R.id.orderNumber);
        grandTotal = findViewById(R.id.grandTotal);
        shipping = findViewById(R.id.shipping);


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
                        setUpLayout(model.getCustomer().getLocationId());
                        address.setText("Name:  " + model.getCustomer().getName() + "\nPhone: " + model.getCustomer().getPhone() + "\nAddress:  " + model.getCustomer().getAddress()
                                + ", " + model.getCustomer().getCity()
                                + "\nCountry: " + model.getCustomer().getCountry());
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

    private void setUpLayout(String locationId) {
        mDatabase.child("Settings").child("DeliveryCharges").child(locationId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    locationAndChargesModel = dataSnapshot.getValue(LocationAndChargesModel.class);
                    if (locationAndChargesModel != null) {
                        total.setText(locationAndChargesModel.getCurrency() + " " +
                                CommonUtils.getFormattedPrice(model.getTotalPrice() * locationAndChargesModel.getCurrencyRate()));


                        delivery.setText(locationAndChargesModel.getCurrency() + " " +
                                CommonUtils.getFormattedPrice(model.getDeliveryCharges() * locationAndChargesModel.getCurrencyRate()));



                        shipping.setText(locationAndChargesModel.getCurrency() + " " +
                                CommonUtils.getFormattedPrice(model.getShippingCharges() * locationAndChargesModel.getCurrencyRate()));

                        grandTotal.setText(locationAndChargesModel.getCurrency() + " " +
                                CommonUtils.getFormattedPrice(model.getGrandTotal() * locationAndChargesModel.getCurrencyRate()));
                        orderNumber.setText("Order number: " + model.getOrderId());
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

        finish();
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    public void saveBitmap(Bitmap bitmap) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));
        File imagePath = new File("/sdcard/" + "Invoice_Number_" + ".jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            CommonUtils.showToast("Invoice saved in gallery\nKindly view it");
            Log.e("ImageSave", "Saveimage");
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {

            finish();
        }
        if (id == R.id.action_print) {
            Bitmap bitmap1 = loadBitmapFromView(ll_linear, ll_linear.getWidth(), ll_linear.getHeight());
            saveBitmap(bitmap1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.print_menu, menu);
        return true;
    }


}
