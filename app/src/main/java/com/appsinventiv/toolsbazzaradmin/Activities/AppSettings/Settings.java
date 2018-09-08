package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.appsinventiv.toolsbazzaradmin.Activities.Products.AddCategories;
import com.appsinventiv.toolsbazzaradmin.R;

public class Settings extends AppCompatActivity {
    Button aboutUs, categories, terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        aboutUs = findViewById(R.id.aboutUs);
        terms = findViewById(R.id.terms);
        categories = findViewById(R.id.categories);

        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Settings.this, AddCategories.class);
                startActivity(i);
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Settings.this, AboutUs.class);
                startActivity(i);
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Settings.this, Terms.class);
                startActivity(i);
            }
        });
    }
}
