package com.appsinventiv.toolsbazzaradmin.Activities.Purchases;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.appsinventiv.toolsbazzaradmin.Adapters.PendingProductsAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.ProductCountModel;
import com.appsinventiv.toolsbazzaradmin.Models.PurchaseOrderModel;
import com.appsinventiv.toolsbazzaradmin.Models.VendorModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PendingPurchases extends AppCompatActivity {
    ArrayList<VendorModel> vendorModelArrayList = new ArrayList<>();
    Spinner spinner;
    VendorModel vendor;
    DatabaseReference mDatabase;
    RecyclerView recyclerview;
    public static ArrayList<ProductCountModel> itemList = new ArrayList<>();
    PendingProductsAdapter adapter;
    ProgressBar progress;
    long purchaseOrder = 10001;
    FloatingActionButton fab;
    ArrayList<String> productIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_purchases);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Pending Purchases");
        spinner = findViewById(R.id.chooseVendor);
        progress = findViewById(R.id.progress);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPurchaseOrder();


            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerview = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);
        adapter = new PendingProductsAdapter(this, itemList, new PendingProductsAdapter.IsPurchased() {
            @Override
            public void addToArray(String id) {
                if (!productIds.contains(id)) {
                    productIds.add(id);
                }
                removePendingOrderFromDb(id);
            }

            @Override
            public void removeFromArray(String id) {
                if (productIds.contains(id)) {
                    productIds.remove(id);
                }
            }
        });
        recyclerview.setAdapter(adapter);


        getVendorsFromDb();
        getPurchaseOrdersFromDb();
    }

    private void removePendingOrderFromDb(String id) {
        mDatabase.child("Purchases").child("PurchaseOrders").child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Item marked as purchased");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void getPurchaseOrdersFromDb() {
        mDatabase.child("Purchases").child("PurchaseOrders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    long po = dataSnapshot.getChildrenCount();
                    purchaseOrder = po + purchaseOrder;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void createPurchaseOrder() {
        mDatabase.child("Purchases").child("PurchaseOrders")
                .child("" + purchaseOrder)
                .setValue(new PurchaseOrderModel("" + purchaseOrder,
                        itemList,
                        vendor,
                        calculateTotal()
                        , System.currentTimeMillis()
                )).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(PendingPurchases.this, ViewPurchaseOrder.class);
                i.putExtra("po",purchaseOrder);
                startActivity(i);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private long calculateTotal() {
        long total = 0;
        for (ProductCountModel model : itemList) {
            total += model.getQuantity() * model.getProduct().getCostPrice();
        }
        CommonUtils.showToast("" + total);
        return total;
    }

    private void getVendorsFromDb() {
        mDatabase.child("Vendors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot != null) {
                            VendorModel model = snapshot.getValue(VendorModel.class);
                            if (model != null) {
                                vendorModelArrayList.add(model);
                                setUpSpinner();
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpSpinner() {


        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < vendorModelArrayList.size(); i++) {
            items.add("" + vendorModelArrayList.get(i).getVendorName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

                vendor = vendorModelArrayList.get(position);
                getDataFromDb(vendor.getVendorId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void getDataFromDb(final String vendorId) {
        progress.setVisibility(View.VISIBLE);

        mDatabase.child("Purchases").child("PendingPurchases").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ProductCountModel model = snapshot.getValue(ProductCountModel.class);
                        if (model != null) {
                            if (vendorId != null) {
                                if (model.getProduct().getVendor().getVendorId().equalsIgnoreCase(vendorId)) {
                                    itemList.add(model);

                                    adapter.notifyDataSetChanged();
                                    progress.setVisibility(View.GONE);

                                } else {
                                    progress.setVisibility(View.GONE);
                                }
                            }
                        }
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
