package com.appsinventiv.toolsbazzaradmin.Models;

/**
 * Created by AliAh on 22/09/2018.
 */

public class CompanyDetailsModel {
    String name,address,telephone,phone;

    public CompanyDetailsModel(String name, String address, String telephone, String phone) {
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.phone = phone;
    }

    public CompanyDetailsModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
