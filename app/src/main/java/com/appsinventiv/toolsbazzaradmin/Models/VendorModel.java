package com.appsinventiv.toolsbazzaradmin.Models;

/**
 * Created by AliAh on 23/08/2018.
 */

public class VendorModel {
    String vendorId,vendorName,vendorPhone,vendorAddress,vendorEmail,isActive;
    long time;
    public VendorModel(String vendorId, String vendorName, String vendorPhone,
                       String vendorAddress, long time,
                        String vendorEmail,
                        String isActive
    ) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.vendorPhone = vendorPhone;
        this.vendorAddress = vendorAddress;
        this.time = time;
        this.vendorEmail = vendorEmail;
        this.isActive = isActive;
    }

    public VendorModel() {
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getVendorEmail() {
        return vendorEmail;
    }

    public void setVendorEmail(String vendorEmail) {
        this.vendorEmail = vendorEmail;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorPhone() {
        return vendorPhone;
    }

    public void setVendorPhone(String vendorPhone) {
        this.vendorPhone = vendorPhone;
    }

    public String getVendorAddress() {
        return vendorAddress;
    }

    public void setVendorAddress(String vendorAddress) {
        this.vendorAddress = vendorAddress;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
