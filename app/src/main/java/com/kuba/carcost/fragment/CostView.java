package com.kuba.carcost.fragment;

public interface CostView {
    String getMileage();
    String getExpense();
    void showMileageError(int resId);
    void showExpenseError(int resId);
}
