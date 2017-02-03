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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kuba.carcost.DatabaseHelper;
import com.kuba.carcost.R;
import com.kuba.carcost.interfaces.ChangeFragment;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCostFragment extends Fragment {


    private View view;
    private ChangeFragment mListener;
    private DatabaseHelper myDb;

    private EditText mileageCAEditText;
    private static TextView dateCAEditText;
    private EditText expenseCAEditText;
    private Spinner categoriesSpinner;
    private EditText descriptionEditText;
    private EditText placeEditText;
    private TextView insurerTextView;
    private EditText insurerEditText;
    private TextView insuranceTextView;
    private Spinner insuranceSpinner;
    private Button addCostButton;

    private int mileage;
    private double expense;
    private int category;
    private String description;
    private String place;
    private String insurer;
    private int insurance;
    private int currentVehicle;

    public AddCostFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_cost, container, false);
        mileageCAEditText = (EditText) view.findViewById(R.id.mileageCAEditText);
        dateCAEditText = (TextView) view.findViewById(R.id.dateCATextView);
        expenseCAEditText = (EditText) view.findViewById(R.id.expenseCAEditText);
        categoriesSpinner = (Spinner) view.findViewById(R.id.categoriesSpinner);
        descriptionEditText = (EditText) view.findViewById(R.id.descriptionEditText);
        placeEditText = (EditText) view.findViewById(R.id.placeEditText);
        insurerTextView = (TextView) view.findViewById(R.id.insurerTextView);
        insurerEditText = (EditText) view.findViewById(R.id.insurerEditText);
        insuranceTextView = (TextView) view.findViewById(R.id.insuranceTextView);
        insuranceSpinner = (Spinner) view.findViewById(R.id.insuranceSpinner);

        setDate(new GregorianCalendar());
        myDb = new DatabaseHelper(view.getContext());
        Cursor res = myDb.getUser();
        myDb.close();
        while (res.moveToNext()) {
            currentVehicle = res.getInt(5);
        }

        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 2) {
                    insurerTextView.setVisibility(View.VISIBLE);
                    insurerEditText.setVisibility(View.VISIBLE);
                    insuranceTextView.setVisibility(View.VISIBLE);
                    insuranceSpinner.setVisibility(View.VISIBLE);
                } else {
                    insurerTextView.setVisibility(View.GONE);
                    insurerEditText.setVisibility(View.GONE);
                    insuranceTextView.setVisibility(View.GONE);
                    insuranceSpinner.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        dateCAEditText.setOnClickListener(new View.OnClickListener() {
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
            addCostButton = (Button) view.findViewById(R.id.addCostButton);
            addCostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveCostData();
                    if (mileageCAEditText.getText().toString() == "") {
                        mileageCAEditText.setError("Pole wymagane.");
                    } else if (mileage < 0) {
                        mileageCAEditText.setError("Przebieg nie może być ujemny.");
                    } else if (expenseCAEditText.getText().toString() == "") {
                        expenseCAEditText.setError("Pole wymagane.");
                    } else {
                        myDb = new DatabaseHelper(view.getContext());
                        if(myDb.insertCostData(currentVehicle, expense,
                                dateCAEditText.getText().toString(), mileage, category+1, description,
                                -1, -1, -1, -1, place, insurer, insurance+1, -1))
                            Toast.makeText(getContext(), "Dodano koszt.", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getContext(), "Nie dodano kosztu.", Toast.LENGTH_SHORT).show();

                        myDb.close();
                        mListener.openHomeFragment();
                    }
                }
            });
        }
    }

    private void saveCostData() {
        try {
            mileage = Integer.parseInt(mileageCAEditText.getText().toString());
        } catch (NumberFormatException e) {
            mileage = -1;
        }
        try {
        expense = Double.parseDouble(expenseCAEditText.getText().toString());
        } catch (NumberFormatException e) {
            expense = 0;
        }
        category = (int) categoriesSpinner.getSelectedItemId();
        description = descriptionEditText.getText().toString();
        place = placeEditText.getText().toString();
        insurer = insurerEditText.getText().toString();
        insurance = (int) insuranceSpinner.getSelectedItemId();
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
        dateCAEditText.setText(date);
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
