package com.kuba.carcost;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class ChartEntry {
    private ArrayList<BarEntry> entries;
    private ArrayList<String> labels;

    public ChartEntry() {
        entries = new ArrayList<>();
        labels = new ArrayList<>();
    }

    public void addBarEntry(BarEntry barEntry) {
        entries.add(barEntry);
    }

    public void addLabel(String label) {
        labels.add(label);
    }
    
    public ArrayList<BarEntry> getEntries() {
        return entries;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }
}
