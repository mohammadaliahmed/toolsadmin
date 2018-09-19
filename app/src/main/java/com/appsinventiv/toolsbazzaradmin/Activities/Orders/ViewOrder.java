package com.appsinventiv.toolsbazzaradmin.Activities.Orders;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Invoicing.ViewInvoice;
import com.appsinventiv.toolsbazzaradmin.Adapters.OrderedProductsAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.Customer;
import com.appsinventiv.toolsbazzaradmin.Models.InvoiceModel;
import com.appsinventiv.toolsbazzaradmin.Models.OrderModel;
import com.appsinventiv.toolsbazzaradmin.Models.ProductCountModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.NotificationAsync;
import com.appsinventiv.toolsbazzaradmin.Utils.NotificationObserver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewOrder extends AppCompatActivity implements NotificationObserver {
    TextView orderId, orderTime, quantity, price, username, phone, address, city, instructions;
    String orderIdFromIntent;
    DatabaseReference mDatabase;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    OrderedProductsAdapter adapter;
    ArrayList<ProductCountModel> list = new ArrayList<>();
    Button orderCompleted, orderShipped;

    String s_orderId, s_orderTime, s_quantity, s_price, s_username, s_phone, s_address, s_city;
    String userFcmKey;
    FloatingActionButton invoice;
    long invoiceNumber = 10001;
    ArrayList<Integer> invoiceArrayList = new ArrayList<>();
    Customer customer;
    OrderModel model;
    RelativeLayout wholeLayout;

    ArrayList<ProductCountModel> newList = new ArrayList<>();
    long totalPrice;
    private long deliveryCharges = 40;
    long grandTotal = 0;
    String pendingId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Order View");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        orderIdFromIntent = intent.getStringExtra("orderId");
        orderId = findViewById(R.id.order_id);
        orderTime = findViewById(R.id.order_time);
        quantity = findViewById(R.id.order_quantity);
        price = findViewById(R.id.order_price);
        instructions = findViewById(R.id.instructions);
        wholeLayout = findViewById(R.id.wholeLayout);

        username = findViewById(R.id.ship_username);
        phone = findViewById(R.id.ship_phone);
        address = findViewById(R.id.ship_address);
        city = findViewById(R.id.ship_city);


        orderCompleted = findViewById(R.id.completed);
        orderShipped = findViewById(R.id.shipped);
        invoice = findViewById(R.id.invoice);

        getInvoicesFromDb();

        orderShipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                markOrderAsShipped();
            }
        });


        invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (model.isInvoiced()) {
                    Intent i = new Intent(ViewOrder.this, ViewInvoice.class);
                    i.putExtra("invoiceNumber", model.getInvoiceNumber());
                    startActivity(i);
                    finish();
                } else {

                    wholeLayout.setVisibility(View.VISIBLE);
                    mDatabase.child("Invoices").child("" + invoiceNumber)
                            .setValue(new InvoiceModel(

                                    invoiceNumber,
                                    list,
                                    newList,
                                    customer,
                                    getTotalPrice(),
                                    System.currentTimeMillis(),
                                    orderIdFromIntent, deliveryCharges,
                                    (grandTotal + deliveryCharges + totalPrice)

                            ))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    updateInvoiceStatus();
                                    Intent i = new Intent(ViewOrder.this, ViewInvoice.class);
                                    i.putExtra("invoiceNumber", invoiceNumber);
                                    startActivity(i);
                                    addPurchase();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }
        });

        orderCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markOrderAsComplete();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);


        mDatabase.child("Orders").child(orderIdFromIntent).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    model = dataSnapshot.getValue(OrderModel.class);
                    if (model != null) {
                        if (model.getOrderStatus().equalsIgnoreCase("pending")
                                || model.getOrderStatus().equalsIgnoreCase("under process")
                                ) {
                            invoice.setVisibility(View.VISIBLE);
                        }else{
                            invoice.setVisibility(View.GONE);
                        }
                        orderId.setText("" + model.getOrderId());
                        orderTime.setText("" + CommonUtils.getFormattedDate(model.getTime()));
                        quantity.setText("" + model.getCountModelArrayList().size());
                        price.setText("Rs " + model.getTotalPrice());
                        username.setText("" + model.getCustomer().getName());
                        phone.setText("" + model.getCustomer().getPhone());
                        instructions.setText("Instructions: " + model.getInstructions());
                        address.setText(model.getCustomer().getAddress());
                        city.setText(model.getCustomer().getCity());
                        list = model.getCountModelArrayList();
                        customer = model.getCustomer();
                        adapter = new OrderedProductsAdapter(ViewOrder.this, list, model.getCustomer().getCustomerType(), new OrderedProductsAdapter.OnProductSelected() {
                            @Override
                            public void onChecked(ProductCountModel product, int position) {
                                if (!newList.contains(product)) {
                                    newList.add(product);
                                }
                            }

                            @Override
                            public void onUnChecked(ProductCountModel product, int position) {
                                if (newList.contains(product)) {
                                    newList.remove(product);
                                }

                            }
                        });
                        recyclerView.setAdapter(adapter);

                        if (model.getOrderStatus().equalsIgnoreCase("Pending")) {
                            orderCompleted.setVisibility(View.GONE);
                            orderShipped.setVisibility(View.VISIBLE);

                        } else if (model.getOrderStatus().equalsIgnoreCase("Shipped")) {
                            orderShipped.setVisibility(View.GONE);
                            orderCompleted.setVisibility(View.VISIBLE);
                        } else {
                            orderShipped.setVisibility(View.GONE);
                            orderCompleted.setVisibility(View.GONE);
                        }
//                        Toast.makeText(ViewOrder.this, ""+list, Toast.LENGTH_SHORT).show();
//                        adapter.notifyDataSetChanged();

                        s_orderId = model.getOrderId();
                        s_quantity = "" + model.getCountModelArrayList().size();
                        s_price = "" + model.getTotalPrice();
                        s_username = model.getCustomer().getUsername();

                        userFcmKey = model.getCustomer().getFcmKey();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void addPurchase() {
        final ArrayList<String> keys = new ArrayList<>();

        mDatabase.child("Purchases").child("PendingPurchases").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        keys.add(snapshot.getKey());
                    }
                    for (final ProductCountModel model : newList) {
                        if (!keys.contains(model.getProduct().getId())) {
                            mDatabase.child("Purchases").child("PendingPurchases").child(model.getProduct().getId()).setValue(model);
                            mDatabase.child("Purchases").child("PendingPurchases").child(model.getProduct().getId()).child("OrderId").child(orderIdFromIntent).setValue(orderIdFromIntent);


                        } else {
                            setQuantity(model);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setQuantity(final ProductCountModel model) {
        final int[] quantity = new int[1];
        mDatabase.child("Purchases").child("PendingPurchases").child(model.getProduct().getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    ProductCountModel model1 = dataSnapshot.getValue(ProductCountModel.class);
                    if (model1 != null) {
                        quantity[0] = model.getQuantity() + model1.getQuantity();
                        mDatabase.child("Purchases").child("PendingPurchases").child(model.getProduct().getId()).child("quantity").setValue(quantity[0]);
                        mDatabase.child("Purchases").child("PendingPurchases").child(model.getProduct().getId()).child("OrderId").child(orderIdFromIntent).setValue(orderIdFromIntent);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private long getTotalPrice() {
        totalPrice = 0;
        for (ProductCountModel abc : newList) {
            if (model.getCustomer().getCustomerType().equalsIgnoreCase("wholesale")) {
                totalPrice += abc.getQuantity() * abc.getProduct().getWholeSalePrice();
            } else if (model.getCustomer().getCustomerType().equalsIgnoreCase("retail")) {
                totalPrice += abc.getQuantity() * abc.getProduct().getRetailPrice();
            }
        }
        return totalPrice;
    }

    private void updateInvoiceStatus() {
        mDatabase.child("Orders").child(orderIdFromIntent).child("isInvoiced").setValue(true);
        mDatabase.child("Orders").child(orderIdFromIntent).child("invoiceNumber").setValue(invoiceNumber);


    }

    private void getInvoicesFromDb() {
        mDatabase.child("Invoices").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    long invoiceNum = dataSnapshot.getChildrenCount();
                    invoiceNumber = invoiceNum + invoiceNumber;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void markOrderAsComplete() {
        showAlertDialogButtonClicked("Your order no: " + s_orderId + " ");
    }

    private void markOrderAsShipped() {
        showAlertDialogButtonClicked("Shipped");
    }

    public void showAlertDialogButtonClicked(final String message) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewOrder.this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to mark this order as " + message + "?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("Orders").child(orderIdFromIntent).child("orderStatus").setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Order marked as " + message);
                        NotificationAsync notificationAsync = new NotificationAsync(ViewOrder.this);
                        String notification_title = "" + message;
                        String notification_message = "Click to view";
                        notificationAsync.execute("ali", userFcmKey, notification_title, notification_message, "Order", "abc");
                        Intent i = new Intent(ViewOrder.this, Orders.class);
                        startActivity(i);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onSuccess(String chatId) {
        CommonUtils.showToast("Notification sent to user");
    }

    @Override
    public void onFailure() {

    }
}
