package com.kuba.carcost.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.kuba.carcost.DatabaseHelper;
import com.kuba.carcost.MainActivity;
import com.kuba.carcost.R;
import com.kuba.carcost.interfaces.ChangeFragment;

public class AddFuelFragment extends Fragment {

    private View view;
    private DatabaseHelper myDb;
    private EditText mileageFAEditText;
    private EditText fuelUnitAmountFAEditText;
    private EditText dateFAEditText;
    private EditText fuelUnitPriceFAEditText;
    private CheckBox fuelFullFACheckBox;
    private Button addFuelButton;

    private ChangeFragment mListener;

    public AddFuelFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_fuel, container, false);
        myDb = new DatabaseHelper(view.getContext());

        return view;
    }

    public void onButtonPressed() {
        if (mListener != null) {
            addFuelButton = (Button) view.findViewById(R.id.addFuelButton);
            addFuelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //myDb.insertCostData()
                    mListener.openHomeFragment();
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChangeFragment) {
            mListener = (ChangeFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
