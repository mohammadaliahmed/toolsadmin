package com.appsinventiv.toolsbazzaradmin.Activities.Orders;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.appsinventiv.toolsbazzaradmin.Activities.MainActivity;
import com.appsinventiv.toolsbazzaradmin.Adapters.SimpleFragmentPagerAdapter;
import com.appsinventiv.toolsbazzaradmin.R;

import java.util.ArrayList;

public class OrdersDelivery extends AppCompatActivity {
    ArrayList<String> orderStatusList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_delivery);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Delivery");
        ViewPager viewPager = findViewById(R.id.viewpager);
        orderStatusList.add("Shipped");
        orderStatusList.add("Delivered");
        orderStatusList.add("Credit");
        orderStatusList.add("Refused");
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this, orderStatusList, getSupportFragmentManager(),"delivery");
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorRed));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
