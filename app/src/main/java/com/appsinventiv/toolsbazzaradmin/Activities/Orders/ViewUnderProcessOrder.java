package com.appsinventiv.toolsbazzaradmin.Activities.Orders;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Invoicing.ViewInvoice;
import com.appsinventiv.toolsbazzaradmin.Adapters.OrderedProductsAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.Customer;
import com.appsinventiv.toolsbazzaradmin.Models.Employee;
import com.appsinventiv.toolsbazzaradmin.Models.InvoiceModel;
import com.appsinventiv.toolsbazzaradmin.Models.OrderModel;
import com.appsinventiv.toolsbazzaradmin.Models.ProductCountModel;
import com.appsinventiv.toolsbazzaradmin.Models.VendorModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.NotificationAsync;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewUnderProcessOrder extends AppCompatActivity {
    Button markAsCourier, markAsShipped, markAsOutOfStock;
    Spinner chooseDeliveryBoy;
    EditText carrier, trackingNumber;

    ArrayList<Employee> employeeArrayList = new ArrayList<>();
    Employee employee;
    TextView orderId, orderTime, quantity, price, username, phone, address, city, ship_country, instructions;
    String orderIdFromIntent;
    DatabaseReference mDatabase;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    OrderedProductsAdapter adapter;
    ArrayList<ProductCountModel> list = new ArrayList<>();
    Button button, button_refused;

    String s_orderId, s_quantity, s_price, s_username;
    String userFcmKey;
    FloatingActionButton invoice;
    long invoiceNumber = 10001;
    Customer customer;
    OrderModel model;

//    RelativeLayout wholeLayout;

    ArrayList<ProductCountModel> newList = new ArrayList<>();
    long totalPrice;
    private Float deliveryCharges = 0.0f;
    private Float shippingCharges = 0.0f;
    long grandTotal = 0;
    CardView shipping_card, order_card, shipping_info_card, delivered_card;
    Button markAsCOD, markAsDeliveredCredit, markAsRefused;
    EditText dueDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_under_process_order);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        orderIdFromIntent = intent.getStringExtra("orderId");

        this.setTitle("Order # " + orderIdFromIntent);


        carrier = findViewById(R.id.carrier);
        trackingNumber = findViewById(R.id.trackingNumber);
        orderId = findViewById(R.id.order_id);
        markAsCourier = findViewById(R.id.markAsCourier);
        markAsShipped = findViewById(R.id.markAsShipped);
        markAsOutOfStock = findViewById(R.id.markAsOutOfStock);
        chooseDeliveryBoy = findViewById(R.id.chooseDeliveryBoy);
        orderTime = findViewById(R.id.order_time);
        quantity = findViewById(R.id.order_quantity);
        ship_country = findViewById(R.id.ship_country);
        price = findViewById(R.id.order_price);
        instructions = findViewById(R.id.instructions);
//        wholeLayout = findViewById(R.id.wholeLayout);
        invoice = findViewById(R.id.invoice);

        username = findViewById(R.id.ship_username);
        phone = findViewById(R.id.ship_phone);
        address = findViewById(R.id.ship_address);
        city = findViewById(R.id.ship_city);
        delivered_card = findViewById(R.id.delivered_card);
        shipping_card = findViewById(R.id.shipping_card);
        order_card = findViewById(R.id.order_card);
        shipping_info_card = findViewById(R.id.shipping_info_card);
//        markAsCOD = findViewById(R.id.markAsCOD);
//        markAsDeliveredCredit = findViewById(R.id.markAsDeliveredCredit);
//        markAsRefused = findViewById(R.id.markAsRefused);
//        dueDate = findViewById(R.id.dueDate);


        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mDatabase.child("Orders").child(orderIdFromIntent).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    model = dataSnapshot.getValue(OrderModel.class);
                    if (model != null) {
                        orderId.setText("" + model.getOrderId());
                        orderTime.setText("" + CommonUtils.getFormattedDate(model.getTime()));
                        quantity.setText("" + model.getCountModelArrayList().size());
                        price.setText("Rs " + model.getTotalPrice());
                        username.setText("" + model.getCustomer().getName());
                        phone.setText("" + model.getCustomer().getPhone());
                        instructions.setText("Instructions: " + model.getInstructions());
                        address.setText(model.getCustomer().getAddress());
                        ship_country.setText(model.getCustomer().getCountry());
                        if (model.getCountModelArrayList().size() > 1) {
                            recyclerView.setMinimumHeight(model.getCountModelArrayList().size() * 550);
                        }

                        city.setText(model.getCustomer().getCity());
                        list = model.getCountModelArrayList();
                        customer = model.getCustomer();
                        adapter = new OrderedProductsAdapter(ViewUnderProcessOrder.this, list, model.getCustomer().getCustomerType(), 1, new OrderedProductsAdapter.OnProductSelected() {
                            @Override
                            public void onChecked(ProductCountModel product, int position) {
                                if (!newList.contains(product)) {
                                    newList.add(product);
                                }
                            }

                            @Override
                            public void onUnChecked(ProductCountModel product, int position) {
                                if (newList.contains(product)) {
                                    newList.remove(newList.indexOf(newList.get(position)));
                                }

                            }
                        });
                        recyclerView.setAdapter(adapter);


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
        markAsShipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markOrderAsShipped();
            }
        });

        markAsOutOfStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markOrderAsOutOfStock();
            }
        });
        markAsCourier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carrier.getText().length() == 0) {
                    carrier.setError("Please enter carrier");
                } else if (trackingNumber.getText().length() == 0) {
                    trackingNumber.setError("Please enter tracking number");
                } else {
                    markOrderAsCourier();
                }
            }
        });

        invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPurchase();
//                if (model.isInvoiced()) {
//                    Intent i = new Intent(ViewUnderProcessOrder.this, ViewInvoice.class);
//                    i.putExtra("invoiceNumber", model.getInvoiceNumber());
//                    startActivity(i);
//                    finish();
//                } else {
//
////                    wholeLayout.setVisibility(View.VISIBLE);
//                    mDatabase.child("Invoices").child("" + invoiceNumber)
//                            .setValue(new InvoiceModel(
//
//                                    invoiceNumber,
//                                    list,
//                                    newList,
//                                    customer,
//                                    getTotalPrice(),
//                                    System.currentTimeMillis(),
//                                    orderIdFromIntent,
//                                    deliveryCharges,
//                                    shippingCharges,
//                                    (grandTotal + deliveryCharges + shippingCharges + totalPrice)
//
//                            ))
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    updateInvoiceStatus();
//                                    Intent i = new Intent(ViewUnderProcessOrder.this, ViewInvoice.class);
//                                    i.putExtra("invoiceNumber", invoiceNumber);
//                                    startActivity(i);
//                                    addPurchase();
//
//
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//
//                        }
//                    });
//                }
            }
        });


        getDeliveryBoysFromDb();
        getInvoiceCountFromDb();

    }


    private void getInvoiceCountFromDb() {
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
                            mDatabase.child("Purchases").child("PendingPurchases").child(model.getProduct().getId()).child("orderId").child(orderIdFromIntent).setValue(orderIdFromIntent);


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
                        mDatabase.child("Purchases").child("PendingPurchases").child(model.getProduct().getId()).child("orderId").child(orderIdFromIntent).setValue(orderIdFromIntent);

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


    private void markOrderAsShipped() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewUnderProcessOrder.this);
        builder.setMessage("Mark order " + orderIdFromIntent + " as shipped");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mDatabase.child("Orders").child(orderIdFromIntent).child("orderStatus").setValue("Shipped");
                mDatabase.child("Orders").child(orderIdFromIntent).child("deliveryBy").setValue(employee.getName()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Order Marked as shipped");
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

    private void getDeliveryBoysFromDb() {
        mDatabase.child("Admin").child("Employees").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    employeeArrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Employee employee = snapshot.getValue(Employee.class);
                        if (employee != null) {
                            if (employee.getRole() == 6)
                                employeeArrayList.add(employee);
                        }
                    }
                    setUpDeliveryBoySpinner();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpDeliveryBoySpinner() {
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < employeeArrayList.size(); i++) {
            items.add("" + employeeArrayList.get(i).getName());

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseDeliveryBoy.setAdapter(adapter);

        chooseDeliveryBoy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

                employee = employeeArrayList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void markOrderAsOutOfStock() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewUnderProcessOrder.this);
        builder.setMessage("Mark order " + orderIdFromIntent + " as out of stock?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mDatabase.child("Orders").child(orderIdFromIntent).child("orderStatus").setValue("Out Of Stock").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Order marked as out of stock");
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

    private void markOrderAsCourier() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewUnderProcessOrder.this);
        builder.setMessage("Mark order " + orderIdFromIntent + " as courier");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mDatabase.child("Orders").child(orderIdFromIntent).child("orderStatus").setValue("Shipped By Courier").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        insertOtherValues();
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

    private void insertOtherValues() {
        mDatabase.child("Orders").child(orderIdFromIntent).child("carrier").setValue(carrier.getText().toString());
        mDatabase.child("Orders").child(orderIdFromIntent).child("trackingNumber").setValue(trackingNumber.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Order marked as courier");
                finish();
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
