package com.appsinventiv.toolsbazzaradmin.Activities.Products;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.appsinventiv.toolsbazzaradmin.Adapters.ProductsAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.Product;
import com.appsinventiv.toolsbazzaradmin.Models.ProductCountModel;
import com.appsinventiv.toolsbazzaradmin.R;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListOfProducts extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Context context;
    ArrayList<Product> productArrayList = new ArrayList<>();
    ArrayList<ProductCountModel> userCartProductList = new ArrayList<>();

    ProductsAdapter adapter;
    String category;
    DatabaseReference mDatabase;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_products);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListOfProducts.this, AddProduct.class);
                startActivity(i);
            }
        });
        this.setTitle("List of products");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.recycler_view_products);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProductsAdapter(this, productArrayList, new ProductsAdapter.OnProductStatusChanged() {
            @Override
            public void onStatusChanged( Product product, final boolean status) {
                mDatabase.child("Products").child(product.getId()).child("isActive").setValue("" + status).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (status) {
                            CommonUtils.showToast("Product is now active");
                        } else {
                            CommonUtils.showToast("Product is now inactive");
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        CommonUtils.showToast("There was some error");

                    }
                });

            }

            @Override
            public void onDelete(final Product product) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ListOfProducts.this);
                builder1.setMessage("Delete product?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                deleteProduct(product.getId());
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        recyclerView.setAdapter(adapter);
        getProductsFromDB();

    }

    private void deleteProduct(String id) {

        mDatabase.child("Products").child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Product deleted");
            }
        });
    }

    private void getProductsFromDB() {
        mDatabase.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    productArrayList.clear();
                    ListOfProducts.this.setTitle(dataSnapshot.getChildrenCount()+" Products");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Product product = snapshot.getValue(Product.class);
                        if (product != null) {
                            productArrayList.add(product);
                            Collections.sort(productArrayList, new Comparator<Product>() {
                                @Override
                                public int compare(Product listData, Product t1) {
                                    String ob1 = listData.getTitle();
                                    String ob2 = t1.getTitle();

                                    return ob1.compareTo(ob2);

                                }
                            });
                            adapter.updatelist(productArrayList);

                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        final MenuItem mSearch = menu.findItem(R.id.action_search);
//        mSearch.expandActionView();
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 0) {
                    adapter.filter(newText);
                }
                return false;
            }
        });


        return true;
        // Get SearchView object.

    }

}
