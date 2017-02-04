package com.kuba.carcost.fragment;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kuba.carcost.Cost;
import com.kuba.carcost.DatabaseHelper;
import com.kuba.carcost.R;
import com.kuba.carcost.interfaces.ChangeFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditCostFragment extends Fragment {

    private View view;
    private DatabaseHelper myDb;
    private Cost cost;
    private int costId;
    private ChangeFragment mListener;

    private EditText mileageCEEditText;
    private TextView dateCETextView;
    private EditText fuelUnitPriceCEEditText;
    private EditText expenseCEEditText;
    private EditText fuelUnitAmountCEEditText;
    private RadioGroup radioCETankNumber;
    private CheckBox fuelFullCECheckBox;
    private CheckBox tankMissedCECheckBox;
    private Spinner categoriesCESpinner;
    private EditText descriptionCEEditText;
    private EditText placeCEEditText;
    private EditText insurerCEEditText;
    private Spinner insuranceCESpinner;
    private TextView insurerCETextView;
    private TextView insuranceCETextView;

    public EditCostFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_cost, container, false);
        mileageCEEditText = (EditText) view.findViewById(R.id.mileageCEEditText);
        dateCETextView = (TextView) view.findViewById(R.id.dateCETextView);
        expenseCEEditText = (EditText) view.findViewById(R.id.expenseCEEditText);
        fuelUnitAmountCEEditText = (EditText) view.findViewById(R.id.fuelUnitAmountCEEditText);
        fuelUnitPriceCEEditText = (EditText) view.findViewById(R.id.fuelUnitPriceCEEditText);
        radioCETankNumber = (RadioGroup) view.findViewById(R.id.radioCETankNumber);
        fuelFullCECheckBox = (CheckBox) view.findViewById(R.id.fuelFullCECheckBox);
        tankMissedCECheckBox = (CheckBox) view.findViewById(R.id.tankMissedCECheckBox);
        categoriesCESpinner = (Spinner) view.findViewById(R.id.categoriesCESpinner);
        descriptionCEEditText = (EditText) view.findViewById(R.id.descriptionCEEditText);
        placeCEEditText = (EditText) view.findViewById(R.id.placeCEEditText);
        insurerCETextView = (TextView) view.findViewById(R.id.insurerCETextView);
        insurerCEEditText = (EditText) view.findViewById(R.id.insurerCEEditText);
        insuranceCETextView = (TextView) view.findViewById(R.id.insuranceCETextView);
        insuranceCESpinner = (Spinner) view.findViewById(R.id.insuranceCESpinner);

        Bundle bundle = getArguments();
        costId = bundle.getInt("EDITCOST");
        myDb = new DatabaseHelper(view.getContext());
        Cursor res = myDb.getCostById(costId);
        if (res.getCount() != 0) {
            while (res.moveToNext()) {
                cost = new Cost(res.getInt(0), res.getInt(1), res.getDouble(2), res.getString(3), res.getInt(4),
                        res.getInt(5), res.getString(6), res.getDouble(7), res.getDouble(8), res.getInt(9),
                        res.getInt(10), res.getString(11), res.getString(12), res.getInt(13), res.getInt(14));
            }
        }
        myDb.close();
        ((EditText) view.findViewById(R.id.insurerCEEditText)).setText(cost.getInsurer());
        ((Spinner) view.findViewById(R.id.insuranceCESpinner)).setSelection(cost.getInsurance());

        setEditLayout();

        categoriesCESpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(id == 2) {
                    insurerCETextView.setVisibility(View.VISIBLE);
                    insurerCEEditText.setVisibility(View.VISIBLE);
                    insuranceCETextView.setVisibility(View.VISIBLE);
                    insuranceCESpinner.setVisibility(View.VISIBLE);
                } else {
                    insurerCETextView.setVisibility(View.GONE);
                    insurerCEEditText.setVisibility(View.GONE);
                    insuranceCETextView.setVisibility(View.GONE);
                    insuranceCESpinner.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        view.findViewById(R.id.saveCEButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCostData();
                mListener.backToHistoryFragment();
            }
        });

        view.findViewById(R.id.deleteCEbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb = new DatabaseHelper(view.getContext());
                myDb.deleteCostById(costId);
                myDb.close();
                mListener.backToHistoryFragment();
            }
        });

        return view;
    }

    public void setEditLayout() {
        mileageCEEditText.setText(Integer.toString(cost.getMileage()));
        dateCETextView.setText(cost.getCostDate());
        expenseCEEditText.setText(Double.toString(cost.getExpense()));
        if (cost.getCategory() == 0) {
            view.findViewById(R.id.editCostLayout).setVisibility(View.GONE);
            view.findViewById(R.id.editFuelLayout).setVisibility(View.VISIBLE);
            fuelUnitAmountCEEditText.setText(Double.toString(cost.getFuelUnitAmount()));
            fuelUnitPriceCEEditText.setText(Double.toString(cost.getFuelUnitPrice()));
            if (cost.getFuelTankNum() != -1) {
                view.findViewById(R.id.tankNumberCETextView).setVisibility(View.VISIBLE);
                radioCETankNumber.setVisibility(View.VISIBLE);
                if (cost.getFuelTankNum() == 0) {
                    ((RadioButton) view.findViewById(R.id.radioCETank1)).setChecked(true);
                } else {
                    ((RadioButton) view.findViewById(R.id.radioCETank2)).setChecked(true);
                }
            } else {
                view.findViewById(R.id.tankNumberCETextView).setVisibility(View.GONE);
                radioCETankNumber.setVisibility(View.GONE);
            }
            if (cost.getFuelFull() == 0) {
                fuelFullCECheckBox.setChecked(false);
            } else {
                fuelFullCECheckBox.setChecked(true);
            }
            if (cost.getTankMissed() == 0) {
                tankMissedCECheckBox.setChecked(false);
            } else {
                tankMissedCECheckBox.setChecked(true);
            }
        } else {
            view.findViewById(R.id.editFuelLayout).setVisibility(View.GONE);
            view.findViewById(R.id.editCostLayout).setVisibility(View.VISIBLE);
            int i = cost.getCategory()-1;
            categoriesCESpinner.setSelection(i);
            descriptionCEEditText.setText(cost.getDescription());
            placeCEEditText.setText(cost.getPlace());
            if(cost.getCategory() == 3) {
                insurerCETextView.setVisibility(View.VISIBLE);
                insurerCEEditText.setVisibility(View.VISIBLE);
                insuranceCETextView.setVisibility(View.VISIBLE);
                insuranceCESpinner.setVisibility(View.VISIBLE);

                insurerCEEditText.setText(cost.getInsurer());
                insuranceCESpinner.setSelection(cost.getInsurance());
            } else {
                insurerCETextView.setVisibility(View.GONE);
                insurerCEEditText.setVisibility(View.GONE);
                insuranceCETextView.setVisibility(View.GONE);
                insuranceCESpinner.setVisibility(View.GONE);
            }
        }
    }

    public void saveCostData() {

        cost.setMileage(Integer.parseInt(mileageCEEditText.getText().toString()));
        cost.setCostDate(dateCETextView.getText().toString());
        cost.setExpense(Double.parseDouble(expenseCEEditText.getText().toString()));

        // Dla tankowania
        if (cost.getCategory() == 0) {
            cost.setFuelUnitAmount(Double.parseDouble(fuelUnitAmountCEEditText.getText().toString()));
            cost.setFuelUnitPrice(Double.parseDouble(fuelUnitPriceCEEditText.getText().toString()));
            if (cost.getFuelTankNum() != -1) {
                if (cost.getFuelTankNum() == 0) {
                    if (radioCETankNumber.getCheckedRadioButtonId() == R.id.radioTank1) {
                        cost.setFuelTankNum(0);
                    } else {
                        cost.setFuelTankNum(1);
                    }
                }
            }
            if (fuelFullCECheckBox.isChecked()) {
                cost.setFuelFull(1);
            } else {
                cost.setFuelFull(0);
            }
            if (tankMissedCECheckBox.isChecked()) {
                cost.setTankMissed(1);
            } else {
                cost.setTankMissed(0);
            }
        // Dla pozostałych kategorii
        } else {
            int i = ((int) categoriesCESpinner.getSelectedItemId()) + 1;
            cost.setCategory(i);
            cost.setDescription(descriptionCEEditText.getText().toString());
            cost.setPlace(placeCEEditText.getText().toString());
            cost.setInsurer(insurerCEEditText.getText().toString());
            cost.setInsurance((int) insuranceCESpinner.getSelectedItemId());
        }


        myDb = new DatabaseHelper(view.getContext());
        if(myDb.updateCostData(costId, cost.getVehicleId(), cost.getExpense(),
                cost.getCostDate(), cost.getMileage(), cost.getCategory(), cost.getDescription(),
                cost.getFuelUnitAmount(), cost.getFuelUnitPrice(), cost.getFuelFull(),
                cost.getFuelTankNum(), cost.getPlace(), cost.getInsurer(), cost.getInsurance(),
                cost.getTankMissed())){
            Toast.makeText(getContext(), "Zaktualizowano", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Nie zaktualizowano", Toast.LENGTH_LONG).show();
        }
        myDb.close();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChangeFragment) {
            mListener = (ChangeFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChangeFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
