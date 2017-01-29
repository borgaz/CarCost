package com.kuba.carcost.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.kuba.carcost.Data;
import com.kuba.carcost.DatabaseHelper;
import com.kuba.carcost.MainActivity;
import com.kuba.carcost.R;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private List<BarDataSet> barDataSet;
    private BarData barData;
    private HorizontalBarChart barChart;
    private View view;
    private DatabaseHelper myDb;
    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        myDb = new DatabaseHelper(getActivity());
        Data data = new Data();
        data.pullLast30();
        barDataSet = new ArrayList<>();
        barDataSet.add(new BarDataSet(data.getEntries(),
                getString(R.string.fragment_home_fuel_label)));
        barData = new BarData(data.getLabels(), barDataSet);
        barChart = (HorizontalBarChart) view.findViewById(R.id.homeChart);
        barChart.setData(barData);
        barChart.invalidate();
        setUserName();

        return view;
    }

    private void setUserName() {
        try {
            Cursor res = myDb.getUser();
            while (res.moveToNext()) {
                String name = res.getString(1);
                ((TextView) getActivity().findViewById(R.id.userNameTextView)).setText(name);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}



