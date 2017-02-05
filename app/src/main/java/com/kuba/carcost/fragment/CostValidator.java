package com.kuba.carcost.fragment;

import com.kuba.carcost.R;

public class CostValidator {
    private CostView view;

    public CostValidator(CostView view) {
        this.view = view;
    }

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
        int expenseValue = Integer.parseInt(mileage);
        if(expenseValue<0) {
            view.showExpenseError(R.string.negative_value_error);
            return false;
        }
        return true;
    }
}
