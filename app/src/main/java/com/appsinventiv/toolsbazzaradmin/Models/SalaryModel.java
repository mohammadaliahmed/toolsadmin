package com.appsinventiv.toolsbazzaradmin.Models;

/**
 * Created by AliAh on 26/10/2018.
 */

public class SalaryModel {
    String id;
    float basicSalary=0,overTime=0,bonus=0,deduction=0;
    String reason;
    float time;
    String year,month;
    String userId;


    public SalaryModel(String id, float basicSalary, float overTime, float bonus, float deduction, String reason, float time, String year, String month, String userId) {
        this.id = id;
        this.basicSalary = basicSalary;
        this.overTime = overTime;
        this.bonus = bonus;
        this.deduction = deduction;
        this.reason = reason;
        this.time = time;
        this.year = year;
        this.month = month;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public SalaryModel() {
    }

    public float getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(float basicSalary) {
        this.basicSalary = basicSalary;
    }

    public float getOverTime() {
        return overTime;
    }

    public void setOverTime(float overTime) {
        this.overTime = overTime;
    }

    public float getBonus() {
        return bonus;
    }

    public void setBonus(float bonus) {
        this.bonus = bonus;
    }

    public float getDeduction() {
        return deduction;
    }

    public void setDeduction(float deduction) {
        this.deduction = deduction;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
