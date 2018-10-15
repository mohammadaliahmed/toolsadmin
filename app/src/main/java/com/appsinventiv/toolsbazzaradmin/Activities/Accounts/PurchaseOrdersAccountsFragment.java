package com.appsinventiv.toolsbazzaradmin.Activities.Accounts;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.appsinventiv.toolsbazzaradmin.Adapters.FinalizedPOAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.PurchaseOrderModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;


public class PurchaseOrdersAccountsFragment extends Fragment {

    Context context;
    RecyclerView recycler_orders;
    ArrayList<PurchaseOrderModel> itemList = new ArrayList<>();
    FinalizedPOAdapter adapter;
    ProgressBar progress;
    DatabaseReference mDatabase;
    String by;
    private RecyclerView recyclerView;

    ArrayList<String> monthsList = new ArrayList<>();
    ArrayList<String> daysList = new ArrayList<>();
    ArrayList<String> yearsList = new ArrayList<>();

    public PurchaseOrdersAccountsFragment() {
        // Required empty public constructor
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
        View rootView = inflater.inflate(R.layout.fragment_purchased_order_accounts, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FinalizedPOAdapter(context, itemList, 1, new FinalizedPOAdapter.ChangeLayout() {
            @Override
            public void onClick(PurchaseOrderModel model, int position, String type) {
                CommonUtils.showToast(type);
                if (type.equalsIgnoreCase("year")) {
                    getYearDataFromServer("2018");
                }
            }
        });
        recyclerView.setAdapter(adapter);


        return rootView;

    }

    private void getYearDataFromServer(String year) {

        mDatabase.child("Accounts").child("PurchaseFinalized").child(year).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot monthSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot daysSnapshot : monthSnapshot.getChildren()) {
                            for(DataSnapshot oneDaySnapshot:daysSnapshot.getChildren()){
                                PurchaseOrderModel model=oneDaySnapshot.getValue(PurchaseOrderModel.class);
                                if(model!=null){
                                    itemList.add(model);
                                }
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mDatabase.child("Accounts").child("PurchaseFinalized/2018/Oct/14").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PurchaseOrderModel model = snapshot.getValue(PurchaseOrderModel.class);
                        if (model != null) {

                            itemList.add(model);
                            Collections.sort(itemList, new Comparator<PurchaseOrderModel>() {
                                @Override
                                public int compare(PurchaseOrderModel listData, PurchaseOrderModel t1) {
                                    Long ob1 = listData.getTime();
                                    Long ob2 = t1.getTime();

                                    return ob2.compareTo(ob1);

                                }
                            });


                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        getDayDataFromServer();
    }

    private void getDayDataFromServer() {

        mDatabase.child("Accounts").child("PurchaseFinalized/2018/Oct/14").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PurchaseOrderModel model = snapshot.getValue(PurchaseOrderModel.class);
                        if (model != null) {

                            itemList.add(model);
                            Collections.sort(itemList, new Comparator<PurchaseOrderModel>() {
                                @Override
                                public int compare(PurchaseOrderModel listData, PurchaseOrderModel t1) {
                                    Long ob1 = listData.getTime();
                                    Long ob2 = t1.getTime();

                                    return ob2.compareTo(ob1);

                                }
                            });


                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getDate() {
        HashMap<Date, ArrayList<PurchaseOrderModel>> dateArrayListHashMap = new HashMap<>();

        for (int i = 0; i < itemList.size(); i++) {
            ArrayList<ArrayList<PurchaseOrderModel>> temppurchaselist = new ArrayList<>();

            long time = itemList.get(i).getTime();
            Calendar calendar = Calendar.getInstance();
//            String date = CommonUtils.getFormattedDateOnly(time);
            calendar.setTimeInMillis(time);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = calendar.getTime();
                String date1 = sdf.format(date);
                Date date2 = sdf.parse(date1);
                if (dateArrayListHashMap.get(date2) != null) {
                    ArrayList<PurchaseOrderModel> templist = dateArrayListHashMap.get(date2);
                    templist.add(itemList.get(i));
                    dateArrayListHashMap.put(date2, templist);
                } else {
                    ArrayList<PurchaseOrderModel> templist = new ArrayList<>();
                    templist.add(itemList.get(i));
//                    temppurchaselist.add(i,templist);
                    dateArrayListHashMap.put(date2, templist);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ArrayList<PurchaseModel> purchaseModels = new ArrayList<>();
        for (Date date : dateArrayListHashMap.keySet()) {
            PurchaseModel purchaseModel = new PurchaseModel(date, dateArrayListHashMap.get(date));
            purchaseModels.add(purchaseModel);
        }
//        adapter = new FinalizedPOAdapter(context, purchaseModels);
//        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();


//        dateArrayListHashMap.size();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
