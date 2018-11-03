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


public class ExpensesDetailFragment extends Fragment {

    Context context;
    RelativeLayout wholeLayout;
    ArrayList<InvoiceModel> itemList = new ArrayList<>();
    ArrayList<Temporarymodel> newList = new ArrayList<>();
    YearViewInvoiceAdapter adapter;
    ProgressBar progress;
    DatabaseReference mDatabase;
    String by;
    private RecyclerView recyclerView;
    float total = 0;
    String what = "year";
    String year = "";
    String month = "";
    String day = "";
    FinalInvoiceListAdapter finalInvoiceAdapter;
    public String path = "";
    WhichKey whichKey;

    Button button, transportation, rent, bills, stationaries, miscellaneous;
    TextView salaryTotal, transportationTotal, rentTotal, billsTotal,
            stationariesTotal, miscellaneousTotal;
    TextView expenseTotal, shopAddress;
    Button print;
    TextView which, leftText, rightText;
    float purchaseBanking = 0;
    float netProfitbanking = 0;
    float lost = 0;
    float sale = 0;
    float purchase = 0;
    float profit = 0;
    float cost = 0;
    private String leftTotal;
    private String rightTotal;


    public ExpensesDetailFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_expenses_detail, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        wholeLayout = rootView.findViewById(R.id.wholeLayout);
        button = rootView.findViewById(R.id.button);
        transportation = rootView.findViewById(R.id.transportation);
        rent = rootView.findViewById(R.id.rent);
        bills = rootView.findViewById(R.id.bills);
        stationaries = rootView.findViewById(R.id.stationaries);
        miscellaneous = rootView.findViewById(R.id.miscellaneous);
        salaryTotal = rootView.findViewById(R.id.salaryTotal);
        transportationTotal = rootView.findViewById(R.id.transportationTotal);
        rentTotal = rootView.findViewById(R.id.rentTotal);
        billsTotal = rootView.findViewById(R.id.billsTotal);
        stationariesTotal = rootView.findViewById(R.id.stationariesTotal);
        print = rootView.findViewById(R.id.print);
        miscellaneousTotal = rootView.findViewById(R.id.miscellaneousTotal);
        expenseTotal = rootView.findViewById(R.id.expenseTotal);
        which = rootView.findViewById(R.id.which);
        rightText = rootView.findViewById(R.id.rightText);
        leftText = rootView.findViewById(R.id.leftText);
        shopAddress = rootView.findViewById(R.id.shopAddress);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ViewExpensesReport.class);
                i.putExtra("path", path);
                startActivity(i);
            }
        });
        path = "2018/Nov";
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Salaries.class);
                i.putExtra("path", path);
                startActivity(i);
            }
        });

        transportation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Transportation.class);
                i.putExtra("path", path);

                startActivity(i);
            }
        });
        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Rent.class);
                i.putExtra("path", path);

                startActivity(i);
            }
        });
        bills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, UtilityBills.class);
                i.putExtra("path", path);

                startActivity(i);
            }
        });
        stationaries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Stationaries.class);
                i.putExtra("path", path);

                startActivity(i);
            }
        });
        miscellaneous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Miscellaneous.class);
                i.putExtra("path", path);

                startActivity(i);
            }
        });


        return rootView;

    }

    private void getSalesFromDb() {
        mDatabase.child("Accounts").child("InvoicesFinalized").child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot month : dataSnapshot.getChildren()) {
                        for (DataSnapshot daysSnapshot : month.getChildren()) {
                            InvoiceModel model = daysSnapshot.getValue(InvoiceModel.class);
                            if (model != null) {
                                sale = sale + model.getTotalPrice();
                                for (int i = 0; i < model.getCountModelArrayList().size(); i++) {
                                    profit = profit + (model.getShippingCharges() + model.getDeliveryCharges() + (model.getCountModelArrayList().get(i).getProduct().getRetailPrice() * model.getCountModelArrayList().get(i).getQuantity()) -
                                            (model.getCountModelArrayList().get(i).getProduct().getCostPrice() * model.getCountModelArrayList().get(i).getQuantity())
                                    );
                                }
                            }
                        }
                    }
                    leftTotal = "Sale: Rs " + sale + "\nPurchase: Rs" + purchase + "\nProfit: Rs " + profit + "\nCost: Rs " + cost;
                    leftText.setText(leftTotal);
                    rightTotal = "Expense: Rs " + total + "\nPurchase Banking: Rs" + purchase + "\nNet Profit: Rs " + (profit - total) + "\nLoss: Rs " + ((profit - total) < 0 ? (profit - total) : 0);
                    rightText.setText(rightTotal);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPurchasesFromDb() {
        mDatabase.child("Accounts").child("PurchaseFinalized").child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot month : dataSnapshot.getChildren()) {
                        for (DataSnapshot daysSnapshot : month.getChildren()) {
                            PurchaseOrderModel model = daysSnapshot.getValue(PurchaseOrderModel.class);
                            if (model != null) {
                                cost = cost + model.getTotal();
                                purchase = purchase + model.getTotal();
                            }
                        }
                    }
                    leftTotal = "Sale: Rs " + sale + "\nPurchase: Rs" + purchase + "\nProfit: Rs " + profit + "\nCost: Rs " + cost;
                    leftText.setText(leftTotal);
                    float loss = ((profit - total) < 0 ? (profit - total) : 0);

                    rightTotal = "Expense: Rs " + total + "\nPurchase Banking: Rs" + purchase + "\nNet Profit: Rs " + (profit - total) + "\nLoss: Rs " + loss;
                    rightText.setText(rightTotal);

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
        what = "year";
        year = "";
        month = "";
        day = "";
        which.setText("Nov");
        sale = 0;
        purchase = 0;
        profit = 0;
        cost = 0;
        total = 0;
        getDataFromServer();
        getAddressFromDb();
        getSalesFromDb();
        getPurchasesFromDb();


    }

    private void getDataFromServer() {

        mDatabase.child("Accounts").child("ExpensesAndRevenue").child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    ExpensesAndRevenueModelMap modelMap = dataSnapshot.getValue(ExpensesAndRevenueModelMap.class);
                    float salary = 0;
                    if (dataSnapshot.child("Salaries").getValue() != null) {
                        salary = dataSnapshot.child("Salaries").child("total").getValue(Float.class);
                        salaryTotal.setText("Rs " + salary);
                        total = total + salary;
                    }
                    if (modelMap.getRent() != null) {
                        rentTotal.setText("Rs " + modelMap.getRent().getTotal());
                        total = total + modelMap.getRent().getTotal();

                    }
                    if (modelMap.getStationaries() != null) {
                        stationariesTotal.setText("Rs " + modelMap.getStationaries().getTotal());
                        total = total + modelMap.getStationaries().getTotal();

                    }
                    if (modelMap.getTransportation() != null) {
                        transportationTotal.setText("Rs " + modelMap.getTransportation().getTotal());
                        total = total + modelMap.getTransportation().getTotal();

                    }
                    if (modelMap.getUtilityBills() != null) {
                        billsTotal.setText("Rs " + modelMap.getUtilityBills().getTotal());
                        total = total + modelMap.getUtilityBills().getTotal();

                    }

                    if (modelMap.getMiscellaneous() != null) {
                        miscellaneousTotal.setText("Rs " + modelMap.getMiscellaneous().getTotal());
                        total = total + modelMap.getMiscellaneous().getTotal();

                    }


                    expenseTotal.setText("Expenses Total: Rs " + CommonUtils.getFormattedPrice(total));
//                    leftText.setText("Sale: Rs 14,800\nPurchase: Rs 6,600\nProfit: Rs 4,000\nCost: Rs 5,500");
//                    rightText.setText("Expense: Rs " + leftTotal + "\nPurchase Banking: Rs 2,900\nNet Profit Banking: Rs 4,000\nLost: Rs 0");
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
        whichKey.which("");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    private void getAddressFromDb() {
        mDatabase.child("Settings").child("AboutUs").child("contact").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    String contact = dataSnapshot.getValue(String.class);
                    shopAddress.setText(contact);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
