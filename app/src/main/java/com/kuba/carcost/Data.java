package com.kuba.carcost;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kuba_HP on 15.12.2016.
 */

public class Data {
    private List<BarEntry> entries;
    private List<String> labels;
    private String date;
    private int xVal;
    private double value;

    /**
     *
     * @param date
     * @param xVal
     * @param value
     */
    public Data(String date, int xVal, double value) {
        this.date = date;
        this.xVal = xVal;
        this.value = value;


    }

    public Data() {
        entries = new ArrayList<>();
        entries.add(new BarEntry(54.1f, 0));
        entries.add(new BarEntry(124.51f, 1));
        entries.add(new BarEntry(78.13f, 2));
        entries.add(new BarEntry(104.24f, 3));
        entries.add(new BarEntry(78.13f, 4));
        entries.add(new BarEntry(104.24f, 5));
        entries.add(new BarEntry(78.13f, 6));
        entries.add(new BarEntry(104.24f, 7));

        labels = new ArrayList<>();
        labels.add("01/11");
        labels.add("04/11");
        labels.add("10/11");
        labels.add("14/11");
        labels.add("19/11");
        labels.add("23/11");
        labels.add("27/11");
        labels.add("31/11");
    }

    public void pullLast30 () {

    }

    public List<BarEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<BarEntry> entries) {
        this.entries = entries;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
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
