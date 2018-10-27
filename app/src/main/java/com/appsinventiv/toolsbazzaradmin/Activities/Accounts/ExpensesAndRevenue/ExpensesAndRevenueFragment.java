package com.appsinventiv.toolsbazzaradmin.Activities.Accounts.ExpensesAndRevenue;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.appsinventiv.toolsbazzaradmin.Activities.Callbacks.WhichKey;
import com.appsinventiv.toolsbazzaradmin.Adapters.FinalInvoiceListAdapter;
import com.appsinventiv.toolsbazzaradmin.Adapters.YearViewInvoiceAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.InvoiceModel;
import com.appsinventiv.toolsbazzaradmin.Models.Temporarymodel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ExpensesAndRevenueFragment extends Fragment {

    Context context;
    RelativeLayout wholeLayout;
    ArrayList<InvoiceModel> itemList = new ArrayList<>();
    ArrayList<Temporarymodel> newList = new ArrayList<>();
    YearViewInvoiceAdapter adapter;
    ProgressBar progress;
    DatabaseReference mDatabase;
    String by;
    private RecyclerView recyclerView;

    String what = "year";
    String year = "";
    String month = "";
    String day = "";
    FinalInvoiceListAdapter finalInvoiceAdapter;
    public static String path = "";
    WhichKey whichKey;

    Button button, transportation, rent, bills, stationaries, miscellaneous;

    public ExpensesAndRevenueFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        whichKey = (WhichKey) context;
        whichKey.which("Year");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_expenses_and_revenue, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        wholeLayout = rootView.findViewById(R.id.wholeLayout);
        button = rootView.findViewById(R.id.button);
        transportation = rootView.findViewById(R.id.transportation);
        rent = rootView.findViewById(R.id.rent);
        bills = rootView.findViewById(R.id.bills);
        stationaries = rootView.findViewById(R.id.stationaries);
        miscellaneous = rootView.findViewById(R.id.miscellaneous);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Salaries.class);
                startActivity(i);
            }
        });

        transportation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Transportation.class);
                startActivity(i);
            }
        });
        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Rent.class);
                startActivity(i);
            }
        });
        bills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, UtilityBills.class);
                startActivity(i);
            }
        });
        stationaries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Stationaries.class);
                startActivity(i);
            }
        });
        miscellaneous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Miscellaneous.class);
                startActivity(i);
            }
        });


        return rootView;

    }


    private void getDayDataFromServer(String year, String month, String day) {
        wholeLayout.setVisibility(View.VISIBLE);
        String path = year + "/" + month + "/" + day;
        finalInvoiceAdapter = new FinalInvoiceListAdapter(context, itemList, path);
        recyclerView.setAdapter(finalInvoiceAdapter);
        mDatabase.child("Accounts").child("InvoicesFinalized").child(year).child(month).child(day).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    newList.clear();
                    for (DataSnapshot allDays : dataSnapshot.getChildren()) {
                        InvoiceModel model = allDays.getValue(InvoiceModel.class);
                        itemList.add(model);
                        wholeLayout.setVisibility(View.GONE);
                    }
                }
                finalInvoiceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getMonthDataFromServer(String year, String month) {
        wholeLayout.setVisibility(View.VISIBLE);
        mDatabase.child("Accounts").child("InvoicesFinalized").child(year).child(month).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {


                    newList.clear();
                    long count = 0;

                    for (DataSnapshot allMonthSnapshot : dataSnapshot.getChildren()) {
                        float sale = 0;
                        float profit = 0;
                        count = allMonthSnapshot.getChildrenCount();

                        for (DataSnapshot allDays : allMonthSnapshot.getChildren()) {

                            InvoiceModel model = allDays.getValue(InvoiceModel.class);

                            if (model != null) {
                                for (int i = 0; i < model.getCountModelArrayList().size(); i++) {
                                    sale = sale + model.getCountModelArrayList().get(i).getProduct().getRetailPrice();
                                    profit = profit + ((model.getCountModelArrayList().get(i).getProduct().getRetailPrice() * model.getCountModelArrayList().get(i).getQuantity()) -
                                            (model.getCountModelArrayList().get(i).getProduct().getCostPrice() * model.getCountModelArrayList().get(i).getQuantity())
                                    );
                                }
                            }
                        }
                        newList.add(new Temporarymodel(allMonthSnapshot.getKey(), count, sale, profit));
                        wholeLayout.setVisibility(View.GONE);
                    }
                }
                adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getYearDataFromServer(String year) {
        wholeLayout.setVisibility(View.VISIBLE);
        mDatabase.child("Accounts").child("InvoicesFinalized").child(year).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    newList.clear();

                    for (DataSnapshot yearSnapshot : dataSnapshot.getChildren()) {
                        float sale = 0;
                        long count = 0;
                        float profit = 0;

                        for (DataSnapshot allMonthSnapshot : yearSnapshot.getChildren()) {
                            count = count + allMonthSnapshot.getChildrenCount();
                            for (DataSnapshot allDays : allMonthSnapshot.getChildren()) {
                                InvoiceModel model = allDays.getValue(InvoiceModel.class);
                                if (model != null) {
                                    for (int i = 0; i < model.getCountModelArrayList().size(); i++) {
                                        sale = sale + model.getCountModelArrayList().get(i).getProduct().getRetailPrice();
                                        profit = profit + ((model.getCountModelArrayList().get(i).getProduct().getRetailPrice() * model.getCountModelArrayList().get(i).getQuantity()) -
                                                (model.getCountModelArrayList().get(i).getProduct().getCostPrice() * model.getCountModelArrayList().get(i).getQuantity())
                                        );
                                    }
                                }
                            }
                        }

                        newList.add(new Temporarymodel(yearSnapshot.getKey(), count, sale, profit));
                    }
                    wholeLayout.setVisibility(View.GONE);


                }
                adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getAllDataFromServer() {
        wholeLayout.setVisibility(View.VISIBLE);
        mDatabase.child("Accounts").child("InvoicesFinalized").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    newList.clear();
                    for (DataSnapshot allYearsSnapshot : dataSnapshot.getChildren()) {
                        float sale = 0;
                        long count = 0;
                        float profit = 0;
                        for (DataSnapshot yearSnapshot : allYearsSnapshot.getChildren()) {

                            for (DataSnapshot allMonthSnapshot : yearSnapshot.getChildren()) {
                                count = count + allMonthSnapshot.getChildrenCount();
                                for (DataSnapshot allDays : allMonthSnapshot.getChildren()) {
                                    InvoiceModel model = allDays.getValue(InvoiceModel.class);
                                    if (model != null) {
                                        for (int i = 0; i < model.getCountModelArrayList().size(); i++) {
                                            sale = sale + model.getCountModelArrayList().get(i).getProduct().getRetailPrice();
                                            profit = profit + ((model.getCountModelArrayList().get(i).getProduct().getRetailPrice() * model.getCountModelArrayList().get(i).getQuantity()) -
                                                    (model.getCountModelArrayList().get(i).getProduct().getCostPrice() * model.getCountModelArrayList().get(i).getQuantity())
                                            );
                                        }
                                    }
                                }
                            }
                        }
                        newList.add(new Temporarymodel(allYearsSnapshot.getKey(), count, sale, profit));
                    }

                    wholeLayout.setVisibility(View.GONE);
                }

                adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        what = "year";
        year = "";
        month = "";
        day = "";
        path = "";
//        getAllDataFromServer();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        what = "year";
        year = "";
        month = "";
        day = "";
        path = "";
        whichKey.which("");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
