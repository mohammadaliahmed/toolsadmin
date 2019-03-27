package com.appsinventiv.toolsbazzaradmin.Activities.Customers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsinventiv.toolsbazzaradmin.Adapters.ChatListAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.ChatModel;
import com.appsinventiv.toolsbazzaradmin.Models.Customer;
import com.appsinventiv.toolsbazzaradmin.Models.VendorModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SellerListFragment extends Fragment {

    Context context;
    RecyclerView recyclerview;
    ArrayList<SellerModel> itemList = new ArrayList<>();
    SellerListAdapter adapter;
    DatabaseReference mDatabase;
    String type;

    public SellerListFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public SellerListFragment(String type) {
        // Required empty public constructor
        this.type = type;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootView = inflater.inflate(R.layout.chat_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_orders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SellerListAdapter(context, itemList, new SellerListAdapter.SellerListCallbacks() {
            @Override
            public void onStatusChanged(SellerModel sellerModel, boolean status) {
                changeSellerStatus(sellerModel, status);
            }
        });

        recyclerView.setAdapter(adapter);


        return rootView;

    }


    private void changeSellerStatus(SellerModel sellerModel, final boolean status) {
        mDatabase.child("Sellers").child(sellerModel.getUsername()).child("status").setValue(status).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast(status ? "Seller is enabled" : "Seller is disabled");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(chatWith.equalsIgnoreCase("wholesale")){
//        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        mDatabase.child("Sellers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SellerModel model = snapshot.getValue(SellerModel.class);
                        if (model != null) {
                            itemList.add(model);
                        }

                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
