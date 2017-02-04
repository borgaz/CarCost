package com.kuba.carcost;

import android.widget.Toast;

public class Cost {
    private int id;
    private int vehicleId;
    private double expense;
    private String costDate;
    private int mileage;
    private int category;
    private String description;
    private double fuelUnitAmount;
    private double fuelUnitPrice;
    private int fuelFull;
    private int fuelTankNum;
    private String place;
    private String insurer;
    private int insurance;
    private int tankMissed;

    public Cost () {}
    public Cost(int id, int vehicleId, double expense, String costDate, int mileage, int category,
                String description, double fuelUnitAmount, double fuelUnitPrice, int fuelFull,
                int fuelTankNum, String place, String insurer, int insurance, int tankMissed) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.expense = expense;
        this.costDate = costDate;
        this.mileage = mileage;
        this.category = category;
        this.description = description;
        this.fuelUnitAmount = fuelUnitAmount;
        this.fuelUnitPrice = fuelUnitPrice;
        this.fuelFull = fuelFull;
        this.fuelTankNum = fuelTankNum;
        this.place = place;
        this.insurer = insurer;
        this.insurance = insurance;
        this.tankMissed = tankMissed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public void setCostDate(String costDate) {
        this.costDate = costDate;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFuelUnitAmount(double fuelUnitAmount) {
        this.fuelUnitAmount = fuelUnitAmount;
    }

    public void setFuelUnitPrice(double fuelUnitPrice) {
        this.fuelUnitPrice = fuelUnitPrice;
    }

    public void setFuelFull(int fuelFull) {
        this.fuelFull = fuelFull;
    }

    public void setFuelTankNum(int fuelTankNum) {
        this.fuelTankNum = fuelTankNum;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setInsurer(String insurer) {
        this.insurer = insurer;
    }

    public void setInsurance(int insurance) {
        this.insurance = insurance;
    }

    public void setTankMissed(int tankMissed) {
        this.tankMissed = tankMissed;
    }

    public int getId() {
        return id;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public double getExpense() {
        return expense;
    }

    public String getCostDate() {
        return costDate;
    }

    public int getMileage() {
        return mileage;
    }

    public int getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public double getFuelUnitAmount() {
        return fuelUnitAmount;
    }

    public double getFuelUnitPrice() {
        return fuelUnitPrice;
    }

    public int getFuelFull() {
        return fuelFull;
    }

    public int getFuelTankNum() {
        return fuelTankNum;
    }

    public String getPlace() {
        return place;
    }

    public String getInsurer() {
        return insurer;
    }

    public int getInsurance() {
        return insurance;
    }

    public int getTankMissed() {
        return tankMissed;
    }
}
