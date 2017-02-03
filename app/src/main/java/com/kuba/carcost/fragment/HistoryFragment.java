package com.kuba.carcost.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kuba.carcost.DatabaseHelper;
import com.kuba.carcost.Cost;
import com.kuba.carcost.HistoryAdapter;
import com.kuba.carcost.R;
import com.kuba.carcost.interfaces.ChangeFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private static View view;
    private ChangeFragment mListener;
    private DatabaseHelper myDb;
    private ArrayList<Cost> costList;
    private static ArrayList<Integer> categoryList;
    static SimpleDateFormat simpleDateFormat;
    private static GregorianCalendar sinceDate;
    private static GregorianCalendar toDate;
    private static GregorianCalendar todayDate;
    private static boolean isSinceDate;

    private static TextView sinceDateHistoryTextView;
    private static TextView toDateHistoryTextView;
    private Button categoriesHistoryButton;
    private Button searchHistoryButton;
    private ListView listView;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);
        sinceDateHistoryTextView = (TextView) view.findViewById(R.id.sinceDateHistoryTextView);
        toDateHistoryTextView = (TextView) view.findViewById(R.id.toDateHistoryTextView);
        categoriesHistoryButton = (Button) view.findViewById(R.id.categoriesHistoryButton);
        searchHistoryButton = (Button) view.findViewById(R.id.searchHistoryButton);
        listView = (ListView) view.findViewById(R.id.historyListView);

        categoryList = new ArrayList<Integer>();
        categoryList.add(0);
        categoryList.add(1);
        categoryList.add(2);
        categoryList.add(3);
        categoryList.add(4);
        myDb = new DatabaseHelper(view.getContext());

        // Initialize toDate
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        toDate = new GregorianCalendar();
        todayDate = new GregorianCalendar();
        simpleDateFormat.setCalendar(toDate);
        String date = simpleDateFormat.format(toDate.getTime());
        toDateHistoryTextView.setText(date);

        // Initialize sinceDate
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.add(GregorianCalendar.MONTH, -1);
        sinceDate = gregorianCalendar;
        simpleDateFormat.setCalendar(gregorianCalendar);
        date = simpleDateFormat.format(gregorianCalendar.getTime());
        sinceDateHistoryTextView.setText(date);

        // set Listeners on DateViews
        sinceDateHistoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSinceDate = true;
                showDatePickerDialog(v);
            }
        });
        toDateHistoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSinceDate = false;
                showDatePickerDialog(v);
            }
        });

        // set Listener on categoryButton
        categoriesHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryMultiPickerFragment();
            }
        });

        // set Listener on searchButton
        searchHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHistoryList();
            }
        });

        // Set ListView adapter
        setHistoryList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setHistoryList();
    }

    private void setHistoryList() {
        myDb = new DatabaseHelper(view.getContext());
        Cursor res = myDb.getCostByCategoryAndDate(sinceDateHistoryTextView.getText().toString(),
                toDateHistoryTextView.getText().toString(), categoryList);
        myDb.close();
        costList = new ArrayList<>();
        if (res.getCount() != 0) {
            while (res.moveToNext()) {
                Cost cost;
                cost = new Cost(res.getInt(0), res.getInt(1), res.getDouble(2), res.getString(3), res.getInt(4),
                        res.getInt(5), res.getString(6), res.getDouble(7), res.getDouble(8), res.getInt(9),
                        res.getInt(10), res.getString(11), res.getString(12), res.getInt(13), res.getInt(14));
                costList.add(cost);
            }


            ListAdapter listAdapter = new HistoryAdapter(getContext(), costList);
            listView.setAdapter(listAdapter);

            if (mListener != null) {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mListener.openEditFragment(costList.get(position).getId());
                    }
                });
            }
        }
    }

    public void showDatePickerDialog(View view) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "DatePickerDialog");
    }

    private static void setDate(final GregorianCalendar gregorianCalendar) {
        simpleDateFormat.setCalendar(gregorianCalendar);
        String date = simpleDateFormat.format(gregorianCalendar.getTime());
        if(isSinceDate) { // check if sinceDate is ok
            if(gregorianCalendar.compareTo(toDate) <= 0) {
                sinceDate = gregorianCalendar;
                sinceDateHistoryTextView.setText(date);
            } else {
                Toast.makeText(view.getContext(), "Data początkowa nie może być późniejsza niż końcowa.", Toast.LENGTH_LONG).show();
            }
        } else { // check if toDate is ok
            if(gregorianCalendar.compareTo(todayDate) > 0) {
                Toast.makeText(view.getContext(), "Data końcowa nie może być późniejsza niż dzisiaj.", Toast.LENGTH_LONG).show();
            } else if(gregorianCalendar.compareTo(sinceDate) < 0) {
                Toast.makeText(view.getContext(), "Data końcowa nie może być wcześniejsza niż początkowa.", Toast.LENGTH_LONG).show();
            } else {
                toDate = gregorianCalendar;
                toDateHistoryTextView.setText(date);
            }
        }
    }

    public void showCategoryMultiPickerFragment() {
        DialogFragment dialogFragment = new CategoryMultiPickerFragment();
        dialogFragment.show(getFragmentManager(), "CategoryMultiPickerFragment");
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

    public static class CategoryMultiPickerFragment extends DialogFragment {

        ArrayList<Integer> categories;
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            categories = new ArrayList<>();  // Where we track the selected items
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Set the dialog title
            builder.setTitle(R.string.category_select_title)
                    // Specify the list array, the items to be selected by default (null for none),
                    // and the listener through which to receive callbacks when items are selected
                    .setMultiChoiceItems(R.array.all_categories, null,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which,
                                                    boolean isChecked) {
                                    if (isChecked) {
                                        // If the user checked the item, add it to the selected items
                                        categories.add(which);
                                    } else if (categories.contains(which)) {
                                        // Else, if the item is already in the array, remove it
                                        categories.remove(Integer.valueOf(which));
                                    }
                                }
                            })
                    // Set the action buttons
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            categoryList = categories;
                        }
                    })
                    .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

            return builder.create();
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final GregorianCalendar gregorianCalendar;
            if(isSinceDate) {
                gregorianCalendar = sinceDate;
            } else {
                gregorianCalendar = toDate;
            }
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
