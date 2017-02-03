package com.kuba.carcost.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kuba.carcost.DatabaseHelper;
import com.kuba.carcost.R;
import com.kuba.carcost.interfaces.ChangeFragment;

import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

public class AddFuelFragment extends Fragment {

    private View view;
    private DatabaseHelper myDb;
    private ChangeFragment mListener;

    private RadioGroup radioTankNumber;
    private EditText mileageFAEditText;
    private EditText fuelUnitAmountFAEditText;
    private static TextView dateFAEditText;
    private TextView tankNumberTextView;
    private EditText fuelUnitPriceFAEditText;
    private EditText expenseFAEditText;
    private CheckBox fuelFullFACheckBox;
    private CheckBox tankMissedCheckBox;
    private Button addFuelButton;

    private int mileage;
    private double fuelUnitAmount;
    private double fuelUnitPrice;
    private double expense;
    private int fuelFull;
    private int fuelTankNum;
    private int currentVehicle;
    private int tankMissed;
    private int hasTwoTanks;

    public AddFuelFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_fuel, container, false);
        myDb = new DatabaseHelper(view.getContext());
        mileageFAEditText = (EditText) view.findViewById(R.id.mileageFAEditText);
        dateFAEditText = (TextView) view.findViewById(R.id.dateFATextView);
        expenseFAEditText = (EditText) view.findViewById(R.id.expenseFAEditText);
        fuelUnitAmountFAEditText = (EditText) view.findViewById(R.id.fuelUnitAmountFAEditText);
        fuelUnitPriceFAEditText = (EditText) view.findViewById(R.id.fuelUnitPriceFAEditText);
        fuelFullFACheckBox = (CheckBox) view.findViewById(R.id.fuelFullFACheckBox);
        tankMissedCheckBox = (CheckBox) view.findViewById(R.id.tankMissedCheckBox);
        tankNumberTextView = (TextView) view.findViewById(R.id.tankNumberTextView);
        radioTankNumber = (RadioGroup) view.findViewById(R.id.radioTankNumber);
        setDate(new GregorianCalendar());
        Cursor res = myDb.getUser();
        while (res.moveToNext()) {
            currentVehicle = res.getInt(5);
        }
        res = myDb.getVehicle(currentVehicle);
        while (res.moveToNext()) {
            hasTwoTanks = res.getInt(5);
        }
        if(hasTwoTanks != -1) {
            tankNumberTextView.setVisibility(View.VISIBLE);
            radioTankNumber.setVisibility(View.VISIBLE);
        }

        dateFAEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        onButtonPressed();

        return view;
    }

    public void onButtonPressed() {
        if (mListener != null) {
            addFuelButton = (Button) view.findViewById(R.id.addFuelButton);
            addFuelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveCostData();
                    if (mileage < 0) {
                        mileageFAEditText.setError("Przebieg musi być dodatni.");
                    } else if (fuelUnitAmount <= 0) {
                        fuelUnitAmountFAEditText.setError("Podaj liczbę dodatnią.");
                    } else if (fuelUnitPrice <= 0) {
                        fuelUnitPriceFAEditText.setError("Podaj liczbę dodatnią.");
                    } else if (expense <= 0) {
                        expenseFAEditText.setError("Podaj liczbę dodatnią.");
                    } else {
                        if(myDb.insertCostData(currentVehicle, expense,
                                dateFAEditText.getText().toString(), mileage, 0, " ", fuelUnitAmount,
                                fuelUnitPrice, fuelFull, fuelTankNum, " ", " ", -1, tankMissed))
                            Toast.makeText(getContext(), "Dodano koszt.", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getContext(), "Nie dodano kosztu.", Toast.LENGTH_SHORT).show();

                        mListener.openHomeFragment();
                    }
                }
            });
        }
    }

    private void saveCostData() {
        mileage = Integer.parseInt(mileageFAEditText.getText().toString());
        fuelUnitAmount = Double.parseDouble(fuelUnitAmountFAEditText.getText().toString());
        fuelUnitPrice = Double.parseDouble(fuelUnitPriceFAEditText.getText().toString());
        expense = Double.parseDouble(expenseFAEditText.getText().toString());
        if (fuelFullFACheckBox.isChecked()) {
            fuelFull = 1; //Full fueled
        } else {
            fuelFull = 0; //Not full fueled
        }
        if(hasTwoTanks != -1) {
            if (radioTankNumber.getCheckedRadioButtonId() == R.id.radioTank1) {
                fuelTankNum = 0;
            } else {
                fuelTankNum = 1;
            }
        } else {
            fuelTankNum = hasTwoTanks;
        }
        if(tankMissedCheckBox.isChecked()) {
            tankMissed = 1; // Last fuel missed
        } else {
            tankMissed = 0; // Last fuel not missed
        }
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

    public void showDatePickerDialog(View view) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "datePicker");
    }

    private static void setDate(final GregorianCalendar gregorianCalendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setCalendar(gregorianCalendar);
        String date = simpleDateFormat.format(gregorianCalendar.getTime());
        dateFAEditText.setText(date);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final GregorianCalendar gregorianCalendar = new GregorianCalendar();
            int year = gregorianCalendar.get(GregorianCalendar.YEAR);
            int month = gregorianCalendar.get(GregorianCalendar.MONTH);
            int day = gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, dayOfMonth);
            setDate(gregorianCalendar);
        }
    }
}
