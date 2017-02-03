package com.kuba.carcost;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<Cost> {

    public HistoryAdapter(Context context, ArrayList<Cost> arrayList) {
        super(context, R.layout.history_row, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.history_row, parent, false);
        String[] categories = view.getResources().getStringArray(R.array.all_categories);

        Cost cost = getItem(position);
        TextView categoryRowTextView = (TextView) view.findViewById(R.id.categoryRowTextView);
        TextView dateRowTextView = (TextView) view.findViewById(R.id.dateRowTextView);
        TextView expenseRowTextView = (TextView) view.findViewById(R.id.expenseRowTextView);
        TextView mileageRowTextView = (TextView) view.findViewById(R.id.mileageRowTextView);

        categoryRowTextView.setText(categories[getItem(position).getCategory()]);
        dateRowTextView.setText(getItem(position).getCostDate());
        expenseRowTextView.setText(Double.toString(getItem(position).getExpense()));
        mileageRowTextView.setText(Integer.toString(cost.getMileage()));

        return view;
    }
}
