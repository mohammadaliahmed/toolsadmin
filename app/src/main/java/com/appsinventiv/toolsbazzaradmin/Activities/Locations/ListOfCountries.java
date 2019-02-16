package com.appsinventiv.toolsbazzaradmin.Activities.Locations;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.appsinventiv.toolsbazzaradmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListOfCountries extends AppCompatActivity {
    ListOfCountriesAdapter adapter;
    RecyclerView recyclerView;
    DatabaseReference mDatabase;
    ArrayList<String> countriesList = new ArrayList<>();
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_countries);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("List of countries");
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        adapter = new ListOfCountriesAdapter(this, countriesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ListOfCountries.this,AddCountry.class);
                startActivity(i);
            }
        });


        getCountriesFromDB();

    }

    private void getCountriesFromDB() {
        mDatabase.child("Settings").child("Locations").child("Countries").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                        String country = snapshot.getKey();
                        countriesList.add(country);
                        adapter.notifyDataSetChanged();
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
