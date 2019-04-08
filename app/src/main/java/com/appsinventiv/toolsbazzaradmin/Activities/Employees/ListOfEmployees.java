package com.appsinventiv.toolsbazzaradmin.Activities.Employees;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.appsinventiv.toolsbazzaradmin.Activities.MainActivity;
import com.appsinventiv.toolsbazzaradmin.Activities.Vendors.AddVendors;
import com.appsinventiv.toolsbazzaradmin.Activities.Vendors.Vendors;
import com.appsinventiv.toolsbazzaradmin.Adapters.EmployeesListAdapter;
import com.appsinventiv.toolsbazzaradmin.Adapters.VendorsListAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.Employee;
import com.appsinventiv.toolsbazzaradmin.Models.VendorModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListOfEmployees extends AppCompatActivity {
    DatabaseReference mDatabase;
    ArrayList<Employee> employeesList = new ArrayList<>();
    RecyclerView recyclerView;
    EmployeesListAdapter adapter;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_employees);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        progress=findViewById(R.id.progress);
        this.setTitle("Employees");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getVendorsFromDb();
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EmployeesListAdapter(this, employeesList);
        recyclerView.setAdapter(adapter);


    }

    private void getVendorsFromDb() {
        employeesList.clear();
        mDatabase.child("Admin").child("Employees").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null) {
                    Employee model = dataSnapshot.getValue(Employee.class);
                    if (model != null) {
                        employeesList.add(model);
                        adapter.notifyDataSetChanged();
                        progress.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
