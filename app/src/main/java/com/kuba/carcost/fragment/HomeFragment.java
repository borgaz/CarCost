package com.kuba.carcost.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.kuba.carcost.Data;
import com.kuba.carcost.R;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private List<BarDataSet> barDataSet;
    private BarData barData;
    private HorizontalBarChart barChart;
    private View view;
    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        Data data = new Data();
        data.pullLast30();
        barDataSet = new ArrayList<>();
        barDataSet.add(new BarDataSet(data.getEntries(),
                getString(R.string.fragment_home_fuel_label)));
        barData = new BarData(data.getLabels(), barDataSet);
        barChart = (HorizontalBarChart) view.findViewById(R.id.homeChart);
        barChart.setData(barData);
        barChart.invalidate();

        return view;
    }
}



