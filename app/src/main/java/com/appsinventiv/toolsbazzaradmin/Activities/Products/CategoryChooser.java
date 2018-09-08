package com.appsinventiv.toolsbazzaradmin.Activities.Products;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.appsinventiv.toolsbazzaradmin.Adapters.ExpandableListAdapter;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryChooser extends AppCompatActivity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_chooser);
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        this.setTitle("Choose category");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Settings").child("Categories");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot header : dataSnapshot.getChildren()) {
                        listDataHeader.add(header.getKey());
                    }
                    setChildList();


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // preparing list data
//        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader,
                listDataChild,
                new ExpandableListAdapter.CategoryChoosen() {
                    @Override
                    public void whichCategory(String parent, String text) {
//                        category.setText("Category: "+text);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("mainCategory", parent);
                        returnIntent.putExtra("subCategory", text);

                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                });

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private void setChildList() {
        int ijk = 0;
        for (String head : listDataHeader) {

            final int finalIjk = ijk;
            mDatabase.child(head).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                        };
                        List<String> abc = dataSnapshot.getValue(t);
                        listDataChild.put(listDataHeader.get(finalIjk), abc);
                        listAdapter.notifyDataSetChanged();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            ijk++;
        }

    }


    @Override
    public void onBackPressed() {
        CommonUtils.showToast("Please choose category");
    }
}
