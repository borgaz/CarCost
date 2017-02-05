package com.kuba.carcost.fragment;

import com.kuba.carcost.R;

public class FuelValidator {
    private FuelView view;

    public FuelValidator(FuelView view) { this.view = view; }

    public boolean validate() {
        String mileage = view.getMileage();
        if(mileage.isEmpty()) {
            view.showMileageError(R.string.empty_view_error);
            return false;
        }
        int mileageValue = Integer.parseInt(mileage);
        if(mileageValue<0) {
            view.showMileageError(R.string.negative_value_error);
            return false;
        }
        String expense = view.getExpense();
        if(expense.isEmpty()) {
            view.showExpenseError(R.string.empty_view_error);
            return false;
        }
        double expenseValue = Double.parseDouble(expense);
        if(expenseValue<0) {
            view.showExpenseError(R.string.negative_value_error);
            return false;
        }
        String fuelUnitAmount = view.getFuelUnitAmount();
        if(fuelUnitAmount.isEmpty()) {
            view.showFuelUnitAmountError(R.string.empty_view_error);
            return false;
        }
        double fuelUnitAmountValue = Double.parseDouble(fuelUnitAmount);
        if(fuelUnitAmountValue<0) {
            view.showFuelUnitAmountError(R.string.negative_value_error);
            return false;
        }
        String fuelUnitPrice = view.getFuelUnitPrice();
        if(fuelUnitPrice.isEmpty()) {
            view.showFuelUnitPriceError(R.string.empty_view_error);
            return false;
        }
        double fuelUnitPriceValue = Double.parseDouble(fuelUnitPrice);
        if(fuelUnitPriceValue<0) {
            view.showFuelUnitPriceError(R.string.negative_value_error);
            return false;
        }
        return true;
    }
}
