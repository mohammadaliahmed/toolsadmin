package com.appsinventiv.toolsbazzaradmin.Activities.Locations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddCountry extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText countryName, provinces, currency, currencyRate;
    Button create, update;
    DatabaseReference mDatabase;
    String country;
    String provincesList = "";
    ArrayList<String> itemList = new ArrayList<>();
    AddCountryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_country);
        this.setTitle("Add Country");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        recyclerView = findViewById(R.id.recyclerView);
        countryName = findViewById(R.id.countryName);
        provinces = findViewById(R.id.provinces);
        currency = findViewById(R.id.currency);
        currencyRate = findViewById(R.id.currencyRate);
        create = findViewById(R.id.create);
        update = findViewById(R.id.update);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        country = getIntent().getStringExtra("country");

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new AddCountryAdapter(this, itemList);
        recyclerView.setAdapter(adapter);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countryName.getText().length() == 0) {
                    countryName.setError("Enter data");
                } else if (provinces.getText().length() == 0) {
                    provinces.setError("Enter data");
                } else if (currency.getText().length() == 0) {
                    currency.setError("Enter data");
                } else if (currencyRate.getText().length() == 0) {
                    currencyRate.setError("Enter data");
                } else {
                    sendDataToDB();
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (country != null) {
            getDataFromDB();
        }

    }

    private void getDataFromDB() {
        mDatabase.child("Settings").child("Locations").child("Countries").child(country).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    provincesList = "";
                    CountryModel model = dataSnapshot.getValue(CountryModel.class);
                    if (model != null) {
                        countryName.setText(model.getCountryName());
                        currencyRate.setText("" + model.getCurrencyRate());
                        currency.setText(model.getCurrencySymbol());
                        for (DataSnapshot snapshot : dataSnapshot.child("provinces").getChildren()) {
                            String value = snapshot.getValue(String.class);
                            itemList.add(value);

                            provincesList = provincesList + value + "\n";
                            provinces.setText(provincesList);
                        }
                        adapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendDataToDB() {
        List<String> provincesList = new ArrayList<String>(Arrays.asList(provinces.getText().toString().split("\n")));

        mDatabase.child("Settings").child("Locations").child("Countries").child(countryName.getText().toString())
                .setValue(new CountryModel(
                        countryName.getText().toString(),
                        currency.getText().toString(),
                        Float.parseFloat(currencyRate.getText().toString()),
                        "",
                        provincesList
                )).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Country added");
//                finish();
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
