package com.appsinventiv.toolsbazzaradmin.Models;

import java.util.ArrayList;

/**
 * Created by AliAh on 03/09/2018.
 */

public class InvoiceModel {
    long id;
    ArrayList<ProductCountModel> countModelArrayList;
    ArrayList<ProductCountModel> newCountModelArrayList;
    Customer customer;
    long totalPrice;
    long time;
    String orderId;
    float deliveryCharges;
    float shippingCharges;
    float grandTotal;


    public InvoiceModel(long id, ArrayList<ProductCountModel> countModelArrayList, ArrayList<ProductCountModel> newCountModelArrayList, Customer customer, long totalPrice,
                        long time, String orderId,
                        float deliveryCharges,
                        float shippingCharges,
                        float grandTotal) {
        this.id = id;
        this.countModelArrayList = countModelArrayList;
        this.newCountModelArrayList = newCountModelArrayList;
        this.customer = customer;
        this.totalPrice = totalPrice;
        this.time = time;
        this.orderId = orderId;
        this.deliveryCharges = deliveryCharges;
        this.shippingCharges = shippingCharges;
        this.grandTotal = grandTotal;
    }

    public float getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(float deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public float getShippingCharges() {
        return shippingCharges;
    }

    public void setShippingCharges(float shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    public float getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(float grandTotal) {
        this.grandTotal = grandTotal;
    }

    public void setGrandTotal(long grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ArrayList<ProductCountModel> getNewCountModelArrayList() {
        return newCountModelArrayList;
    }

    public void setNewCountModelArrayList(ArrayList<ProductCountModel> newCountModelArrayList) {
        this.newCountModelArrayList = newCountModelArrayList;
    }

    public InvoiceModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<ProductCountModel> getCountModelArrayList() {
        return countModelArrayList;
    }

    public void setCountModelArrayList(ArrayList<ProductCountModel> countModelArrayList) {
        this.countModelArrayList = countModelArrayList;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
