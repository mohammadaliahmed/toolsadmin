package com.appsinventiv.toolsbazzaradmin.Activities.Accounts.ExpensesAndRevenue;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Callbacks.WhichKey;
import com.appsinventiv.toolsbazzaradmin.Adapters.FinalInvoiceListAdapter;
import com.appsinventiv.toolsbazzaradmin.Adapters.YearViewInvoiceAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.ExpensesAndRevenueModelMap;
import com.appsinventiv.toolsbazzaradmin.Models.InvoiceModel;
import com.appsinventiv.toolsbazzaradmin.Models.PurchaseOrderModel;
import com.appsinventiv.toolsbazzaradmin.Models.Temporarymodel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ExpensesAndRevenueFragment extends Fragment {

    Context context;
    DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    String what = "year";
    String year = "";
    String month = "";
    String day = "";
    public String path = "";
    //    WhichKey whichKey;
    ArrayList<ExpensesModel> itemList = new ArrayList<>();
    ExpensesAndRevenueAdapter adapter;

    public ExpensesAndRevenueFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expenses_and_revenue, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        adapter = new ExpensesAndRevenueAdapter(context, itemList, new ExpensesAndRevenueAdapter.ChangeLayout() {
            @Override
            public void onClick(ExpensesModel model, int position, String type, String key) {
            }
        });
        recyclerView.setAdapter(adapter);
        return rootView;

    }


    @Override
    public void onResume() {
        super.onResume();
        what = "year";
        year = "";
        month = "";
        day = "";

        getDataFromServer();


    }

    private void getDataFromServer() {
        itemList.clear();
        mDatabase.child("Accounts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot allYears : dataSnapshot.child("ExpensesAndRevenue").getChildren()) {
                        float total = 0;
                        for (DataSnapshot allMonth : allYears.getChildren()) {

                            ExpensesAndRevenueModelMap modelMap = allMonth.getValue(ExpensesAndRevenueModelMap.class);
                            float salary = 0;
                            if (allMonth.child("Salaries").getValue() != null) {
                                salary = allMonth.child("Salaries").child("total").getValue(Float.class);
                                total = total + salary;
                            }
                            if (modelMap.getRent() != null) {
                                total = total + modelMap.getRent().getTotal();

                            }
                            if (modelMap.getStationaries() != null) {
                                total = total + modelMap.getStationaries().getTotal();

                            }
                            if (modelMap.getTransportation() != null) {
                                total = total + modelMap.getTransportation().getTotal();

                            }
                            if (modelMap.getUtilityBills() != null) {
                                total = total + modelMap.getUtilityBills().getTotal();

                            }

                            if (modelMap.getMiscellaneous() != null) {
                                total = total + modelMap.getMiscellaneous().getTotal();

                            }

                        }
//                        itemList.add(new ExpensesModel("left text", "Expenses: Rs " + total, "aaa", ""));
                    }
                    for (DataSnapshot allYearsSnapshot : dataSnapshot.child("InvoicesFinalized").getChildren()) {
                        float sale = 0;
                        long count = 0;
                        float profit = 0;
                        for (DataSnapshot yearSnapshot : allYearsSnapshot.getChildren()) {

                            for (DataSnapshot allMonthSnapshot : yearSnapshot.getChildren()) {
                                count = count + allMonthSnapshot.getChildrenCount();
                                for (DataSnapshot allDays : allMonthSnapshot.getChildren()) {
                                    InvoiceModel model = allDays.getValue(InvoiceModel.class);
                                    if (model != null) {
                                        for (int i = 0; i < model.getNewCountModelArrayList().size(); i++) {
                                            sale = sale + (model.getNewCountModelArrayList().get(i).getProduct().getRetailPrice() * model.getNewCountModelArrayList().get(i).getQuantity()) + (model.getShippingCharges() + model.getDeliveryCharges());
                                            profit = profit + ((model.getShippingCharges() + model.getDeliveryCharges()) + (model.getNewCountModelArrayList().get(i).getProduct().getRetailPrice() * model.getNewCountModelArrayList().get(i).getQuantity()) -
                                                    (model.getNewCountModelArrayList().get(i).getProduct().getCostPrice() * model.getNewCountModelArrayList().get(i).getQuantity())
                                            );
                                        }
                                    }
                                }
                            }
                        }
//                        String leftTotal = "Sale: Rs " + sale + "\nPurchase: Rs" + "" + "\nProfit: Rs " + profit + "\nCost: Rs " + "";
//
//                        itemList.add(new ExpensesModel(leftTotal, "Expenses: Rs " + count, "aaa" + sale, "" + profit));
                    }
                    for (DataSnapshot allYearsSnapshot : dataSnapshot.child("PurchaseFinalized").getChildren()) {
                        float cost = 0;
                        long count = 0;
                        float purchaseCost=0;
                        for (DataSnapshot yearSnapshot : allYearsSnapshot.getChildren()) {

                            for (DataSnapshot allMonthSnapshot : yearSnapshot.getChildren()) {
                                count = count + allMonthSnapshot.getChildrenCount();
                                for (DataSnapshot allDays : allMonthSnapshot.getChildren()) {
                                    PurchaseOrderModel model = allDays.getValue(PurchaseOrderModel.class);
                                    if (model != null) {
                                        for (int i = 0; i < model.getProductsList().size(); i++) {
                                            cost = cost + model.getProductsList().get(i).getProduct().getCostPrice();
                                            if (model.getProductsList().get(i).getNewCostPrice() != -1 ) {
                                                purchaseCost = cost + model.getProductsList().get(i).getNewCostPrice();
                                                purchaseCost=purchaseCost-model.getProductsList().get(i).getProduct().getCostPrice();

                                            } else {
                                                purchaseCost = cost;
                                            }

                                        }
                                    }
                                }
                            }
                        }
                        String leftTotal = "Sale: Rs " + cost + "\nPurchase: Rs" + "" + "\nProfit: Rs " + "" + "\nCost: Rs " + "";

                        itemList.add(new ExpensesModel(leftTotal, "Expenses: Rs " + count, "aaa" + "", "" + ""));
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
    public void onDestroyView() {
        super.onDestroyView();
        what = "year";
        year = "";
        month = "";
        day = "";
        path = "";
//        whichKey.which("");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
