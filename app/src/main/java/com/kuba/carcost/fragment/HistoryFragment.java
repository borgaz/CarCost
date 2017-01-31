package com.kuba.carcost.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.kuba.carcost.DatabaseHelper;
import com.kuba.carcost.R;
import com.kuba.carcost.interfaces.ChangeFragment;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private View view;
    private ChangeFragment mListener;
    private DatabaseHelper myDb;

    private static TextView startDateHistoryTextView;
    private static TextView stopDateHistoryTextView;
    private Button categoriesHistoryButton;
    private Button searchHistoryButton;
    private static boolean isStartDate;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);
        startDateHistoryTextView = (TextView) view.findViewById(R.id.startDateHistoryTextView);
        stopDateHistoryTextView = (TextView) view.findViewById(R.id.stopDateHistoryTextView);
        categoriesHistoryButton = (Button) view.findViewById(R.id.categoriesHistoryButton);
        searchHistoryButton = (Button) view.findViewById(R.id.searchHistoryButton);

        startDateHistoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDate = true;
                showDatePickerDialog(v);
            }
        });
        stopDateHistoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDate = false;
                showDatePickerDialog(v);
            }
        });



        return view;
    }

    public void showDatePickerDialog(View view) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "datePicker");
    }

    private static void setDate(final GregorianCalendar gregorianCalendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setCalendar(gregorianCalendar);
        String date = simpleDateFormat.format(gregorianCalendar.getTime());
        if(isStartDate) {
            startDateHistoryTextView.setText(date);
        } else {
            stopDateHistoryTextView.setText(date);
        }
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
