package com.appsinventiv.toolsbazzaradmin.Activities.Purchases;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.appsinventiv.toolsbazzaradmin.R;

public class Purchases extends AppCompatActivity {
    Button pendingPurchases,purchaseOrders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);

        purchaseOrders=findViewById(R.id.purchaseOrders);
        pendingPurchases=findViewById(R.id.pendingPurchases);

        pendingPurchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Purchases.this, PendingPurchases.class);
                startActivity(i);
            }
        });

        purchaseOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Purchases.this, ViewPurchaseOrder.class);
                i.putExtra("po",10002l);
                startActivity(i);
            }
        });

    }
}
