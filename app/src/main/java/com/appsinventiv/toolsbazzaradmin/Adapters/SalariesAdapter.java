package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Models.Employee;
import com.appsinventiv.toolsbazzaradmin.Models.SalaryModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by AliAh on 26/10/2018.
 */

public class SalariesAdapter extends RecyclerView.Adapter<SalariesAdapter.ViewHolder> {
    Context context;
    ArrayList<Employee> itemList;
//    ViewHolder holder;
//    int postition;
    SalaryInterface salaryInterface;
    public SalariesAdapter(Context context, ArrayList<Employee> itemList,SalaryInterface salaryInterface) {
        this.context = context;
        this.itemList = itemList;
        this.salaryInterface=salaryInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.salaries_item_layout, parent, false);
        SalariesAdapter.ViewHolder viewHolder = new SalariesAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Employee employee = itemList.get(position);
        holder.name.setText((position + 1) + ". " + employee.getName());
        holder.role.setText("(Role: " + CommonUtils.rolesList[employee.getRole()] + ")");
        holder.printSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtils.showToast("Print salary");
            }
        });

        holder.salary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                CommonUtils.showToast(editable+"\n"+position);
            }
        });



//        this.holder = holder;
//        this.postition=position;
    }

    public void setValues() {
        ArrayList<SalaryModel> salaryList = new ArrayList<>();


//        CommonUtils.showToast(postition+" \n"+ holder.salary.getText().toString());
        salaryInterface.values(salaryList);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, role;
        Button printSalary;
        EditText salary, overTime, bonus, deduction, reason;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            role = itemView.findViewById(R.id.role);
            printSalary = itemView.findViewById(R.id.printSalary);
            salary = itemView.findViewById(R.id.salary);
            overTime = itemView.findViewById(R.id.overTime);
            bonus = itemView.findViewById(R.id.bonus);
            deduction = itemView.findViewById(R.id.deduction);
            reason = itemView.findViewById(R.id.reason);
        }
    }

    public interface SalaryInterface {
        public void values(ArrayList<SalaryModel> salaryModelList);
    }

}
