package com.kuba.carcost;

import android.content.Context;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;

public class Data {
    private DatabaseHelper myDb;
    private ChartEntry chartEntry;
    private String date;
    private int xVal;
    private double value;

    public Data(Context context) {
        myDb = new DatabaseHelper(context);
        chartEntry = myDb.getCostDataForChart30();
    }

    public ArrayList<BarEntry> getEntries() {
        return chartEntry.getEntries();
    }

    public ArrayList<String> getLabels() {
        return chartEntry.getLabels();
    }

    public int getxVal() {
        return xVal;
    }

    public void setxVal(int xVal) {
        this.xVal = xVal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
