package com.appsinventiv.toolsbazzaradmin.Activities.Products;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.appsinventiv.toolsbazzaradmin.Activities.MainActivity;
import com.appsinventiv.toolsbazzaradmin.R;

public class ProductUploaded extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_uploaded);
        Button done=findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ProductUploaded.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(ProductUploaded.this,MainActivity.class);
        startActivity(i);
        finish();
    }
}
