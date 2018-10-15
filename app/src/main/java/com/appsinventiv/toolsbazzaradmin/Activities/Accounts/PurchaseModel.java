package com.appsinventiv.toolsbazzaradmin.Activities.Accounts;

import com.appsinventiv.toolsbazzaradmin.Models.PurchaseOrderModel;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by AliAh on 11/10/2018.
 */

public class PurchaseModel {
    Date  date;
    ArrayList<PurchaseOrderModel> purchaseModels;

    public PurchaseModel(Date date, ArrayList<PurchaseOrderModel> purchaseModels) {
        this.date = date;
        this.purchaseModels = purchaseModels;
    }

    public Date getDate() {

        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<PurchaseOrderModel> getPurchaseModels() {
        return purchaseModels;
    }

    public void setPurchaseModels(ArrayList<PurchaseOrderModel> purchaseModels) {
        this.purchaseModels = purchaseModels;
    }
}
