package com.kuba.carcost.fragment;

/**
 * Created by Kuba_HP on 05.02.2017.
 */
public interface FuelView {
    String getMileage();
    String getExpense();
    String getFuelUnitAmount();
    String getFuelUnitPrice();
    void showMileageError(int mileage_error);
    void showExpenseError(int expense_error);
    void showFuelUnitAmountError(int empty_view_error);
    void showFuelUnitPriceError(int empty_view_error);
}
