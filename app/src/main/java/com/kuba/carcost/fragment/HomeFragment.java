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
import com.kuba.carcost.ChartEntry;
import com.kuba.carcost.DatabaseHelper;
import com.kuba.carcost.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private View view;
    private DatabaseHelper myDb;
    private List<BarDataSet> barDataSet;

    private BarData barData;
    private HorizontalBarChart barChart;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        myDb = new DatabaseHelper(getActivity());
        double avgAllValue = myDb.getCostDataForAvgAll();
        String string = new DecimalFormat("####.##zł/km").format(avgAllValue);
        ((TextView) view.findViewById(R.id.avgAllTextView)).setText(string);
        double avgFuelValue = myDb.getCostDataForAvgFuel();
        string = new DecimalFormat("####.##l/km").format(avgFuelValue);
        ((TextView) view.findViewById(R.id.avgFuelTextView)).setText(string);
        double avg30Value = myDb.getCostDataForAvg30();
        string = new DecimalFormat("####.##zł/km").format(avg30Value);
        ((TextView) view.findViewById(R.id.avg30TextView)).setText(string);
        myDb.close();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setHomeChart();
        setUserName();
    }

    private void setHomeChart() {
        myDb = new DatabaseHelper(getContext());
        ChartEntry chartEntry = myDb.getCostDataForChart30();
        barDataSet = new ArrayList<>();
        barDataSet.add(new BarDataSet(chartEntry.getEntries(),
                getString(R.string.fragment_home_fuel_label)));
        barData = new BarData(chartEntry.getLabels(), barDataSet);
        barChart = (HorizontalBarChart) view.findViewById(R.id.homeChart);
        barChart.setData(barData);
        barChart.setDescription("Koszty z minionego miesiąca");
        barChart.invalidate();
        myDb.close();
    }

    private void setUserName() {
        try {
            myDb = new DatabaseHelper(getActivity());
            Cursor res = myDb.getUser();
            while (res.moveToNext()) {
                String name = res.getString(1);
                ((TextView) getActivity().findViewById(R.id.userNameTextView)).setText(name);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        } finally {
            myDb.close();
        }
    }
}



