package com.kuba.carcost.fragment;

/**
 * Created by Kuba_HP on 05.02.2017.
 */
public interface EditView {
    String getMileage();


    String getExpense();

    String getFuelUnitAmount();

    String getFuelUnitPrice();

    void showMileageError(int empty_view_error);

    void showExpenseError(int empty_view_error);

    void showFuelUnitAmountError(int empty_view_error);

    void showFuelUnitPriceError(int empty_view_error);
}
