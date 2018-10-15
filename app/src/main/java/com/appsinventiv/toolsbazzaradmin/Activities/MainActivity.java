package com.appsinventiv.toolsbazzaradmin.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.appsinventiv.toolsbazzaradmin.Activities.Accounts.Accounts;
import com.appsinventiv.toolsbazzaradmin.Activities.AppSettings.Settings;
import com.appsinventiv.toolsbazzaradmin.Activities.Chat.Chats;
import com.appsinventiv.toolsbazzaradmin.Activities.Employees.ListOfEmployees;
import com.appsinventiv.toolsbazzaradmin.Activities.Invoicing.ListOfInvoices;
import com.appsinventiv.toolsbazzaradmin.Activities.Invoicing.ViewInvoice;
import com.appsinventiv.toolsbazzaradmin.Activities.Orders.Orders;
import com.appsinventiv.toolsbazzaradmin.Activities.Orders.OrdersCourier;
import com.appsinventiv.toolsbazzaradmin.Activities.Orders.OrdersDelivery;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.ListOfProducts;
import com.appsinventiv.toolsbazzaradmin.Activities.Purchases.Purchases;
import com.appsinventiv.toolsbazzaradmin.Activities.Vendors.Vendors;
import com.appsinventiv.toolsbazzaradmin.Models.AdminModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.SharedPrefs;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference mDatabase;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    LinearLayout chats, customers, delivery, orders, products,
            notifications, signout, vendors, settings, purchases,
             employees, accounts, courier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        chats = findViewById(R.id.chats);
        customers = findViewById(R.id.customers);
        signout = findViewById(R.id.signout);
        delivery = findViewById(R.id.delivery);
        orders = findViewById(R.id.orders);
        products = findViewById(R.id.products);
        notifications = findViewById(R.id.notifications);
        vendors = findViewById(R.id.vendors);
        notifications = findViewById(R.id.notifications);
        settings = findViewById(R.id.settings);
        purchases = findViewById(R.id.purchases);
        employees = findViewById(R.id.employees);
        accounts = findViewById(R.id.accounts);
        courier = findViewById(R.id.courier);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // clearing app data
                    String packageName = getApplicationContext().getPackageName();
                    Runtime runtime = Runtime.getRuntime();
                    runtime.exec("pm clear " + packageName);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        employees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ListOfEmployees.class);
                startActivity(i);
            }
        });


        chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Chats.class);
                startActivity(i);
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Orders.class);
                startActivity(i);
            }
        });

        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ListOfProducts.class);
                startActivity(i);
            }
        });
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, OrdersDelivery.class);
                startActivity(i);
            }
        });

        vendors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Vendors.class);
                startActivity(i);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Settings.class);
                startActivity(i);
            }
        });
        purchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Purchases.class);
                startActivity(i);
            }
        });


        accounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Accounts.class);
                startActivity(i);
            }
        });

        courier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, OrdersCourier.class);
                startActivity(i);
            }
        });




        if (SharedPrefs.getIsLoggedIn().equals("yes")) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("Admin").child("admin").child("fcmKey").setValue(SharedPrefs.getFcmKey());
        } else {
            SharedPrefs.setIsLoggedIn("yes");
            mDatabase = FirebaseDatabase.getInstance().getReference();
            SharedPrefs.setUsername("admin");
            mDatabase.child("Admin").child("admin")
                    .setValue(new AdminModel("admin", "Name", "admin", "admin", "", "online", SharedPrefs.getFcmKey()));

        }


//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent i = new Intent(MainActivity.this, Chats.class);
            startActivity(i);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            Intent i = new Intent(MainActivity.this, Orders.class);
            startActivity(i);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
