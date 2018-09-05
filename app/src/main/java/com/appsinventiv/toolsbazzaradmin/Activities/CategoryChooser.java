package com.appsinventiv.toolsbazzaradmin.Activities;

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
//
//    private void prepareListData() {
//
//        // Adding child data
//
//
//        // Adding child data
//        List<String> oil = new ArrayList<String>();
//        oil.add("Copper Wires");
//        oil.add("Sockets");
//        oil.add("Switches");
//
//
//        List<String> spices = new ArrayList<String>();
//        spices.add("Herbs & Spices");
//        spices.add("Salt");
//        spices.add("Sugar");
//        spices.add("National Masala");
//        spices.add("Shan Masala");
//        spices.add("Seasoning Cubes");
//        spices.add("Vinegar");
//
//        List<String> sauces = new ArrayList<String>();
//        sauces.add("Ketchup");
//        sauces.add("Chilli Sauce");
//        sauces.add("Mayonnaise");
//        sauces.add("American Garden");
//        sauces.add("Nandos");
//        sauces.add("Olives");
//        sauces.add("Pickles");
//        sauces.add("Other Sauces");
//
//        List<String> menCare = new ArrayList<String>();
//        menCare.add("M Roll on");
//        menCare.add("Body Spray");
//        menCare.add("Rozors");
//        menCare.add("Shaving Foams");
//        menCare.add("After Shave");
//
//        List<String> womenCare = new ArrayList<String>();
//        womenCare.add("W Body Spray");
//        womenCare.add("W Roll on");
//        womenCare.add("Pads");
//        womenCare.add("Hair Remover");
//        womenCare.add("Nail polish Remover");
//
//
//        List<String> hairCare = new ArrayList<String>();
//        hairCare.add("Hair Color");
//        hairCare.add("Shampoo");
//        hairCare.add("Conditioner");
//        menCare.add("Gel");
//        hairCare.add("Hair Cream");
//
//        List<String> skinCare = new ArrayList<String>();
//        skinCare.add("Scrubs");
//        skinCare.add("Lotion & Cream");
//        skinCare.add("Face Wash");
//        skinCare.add("Sun Block");
//
//        List<String> dentalCare = new ArrayList<String>();
//        dentalCare.add("Tooth Brush");
//        dentalCare.add("Tooth Paste");
//        dentalCare.add("Mouth Wash");
//
//        List<String> soap = new ArrayList<String>();
//        soap.add("Soap");
//        soap.add("Hand Wash");
//        soap.add("Shower Gel");
//
//        List<String> fruitsandveges = new ArrayList<String>();
//        fruitsandveges.add("Fruits");
//        fruitsandveges.add("Vegetables");
//
//
//        List<String> homeCare = new ArrayList<String>();
//        homeCare.add("Floor & Bath Cleaning");
//        homeCare.add("Laundry");
//        homeCare.add("Kitchen Cleaning");
//        homeCare.add("Repellents");
//        homeCare.add("Air Refreshners");
//        homeCare.add("Cleaning Accessories");
//
//        List<String> beverages = new ArrayList<String>();
//        beverages.add("Cold Drinks");
//        beverages.add("Juices");
//        beverages.add("Tea");
//        beverages.add("Mineral Water");
//        beverages.add("Sharbat");
//        beverages.add("Coffee");
//
//        listDataHeader.add("Electrical");
//        listDataHeader.add("Spices, Salt & Sugar");
//        listDataHeader.add("Sauces, Olives & Pickles");
//        listDataHeader.add("Women Care");
//        listDataHeader.add("Men Care");
//        listDataHeader.add("Hair Care");
//        listDataHeader.add("Skin Care");
//        listDataHeader.add("Dental Care");
//        listDataHeader.add("Soap, Hand Wash & Sanitizer");
//        listDataHeader.add("Fruits & Vegetables");
//        listDataHeader.add("Home Care");
//        listDataHeader.add("Beverages");
//
//        listDataChild.put(listDataHeader.get(0), oil); // Header, Child data
//        listDataChild.put(listDataHeader.get(1), spices);
//        listDataChild.put(listDataHeader.get(2), sauces);
//        listDataChild.put(listDataHeader.get(3), womenCare);
//
//        listDataChild.put(listDataHeader.get(4), menCare);
//        listDataChild.put(listDataHeader.get(5), hairCare);
//        listDataChild.put(listDataHeader.get(6), skinCare);
//        listDataChild.put(listDataHeader.get(7), dentalCare);
//        listDataChild.put(listDataHeader.get(8), soap);
//        listDataChild.put(listDataHeader.get(9), fruitsandveges);
//        listDataChild.put(listDataHeader.get(10), homeCare);
//        listDataChild.put(listDataHeader.get(11), beverages);
//
//
//    }

    @Override
    public void onBackPressed() {
        CommonUtils.showToast("Please choose category");
    }
}
