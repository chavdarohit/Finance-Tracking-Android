package com.example.finance.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.finance.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GraphFragment extends Fragment {

    PieChart piechart;
    PieDataSet pieDataSet;
    PieData pieData;
    ArrayList barEntries;
    BarChart barChart;
    BarDataSet barDataSet;
    BarData barData;
    DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user= auth.getCurrentUser();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        piechart = view.findViewById(R.id.piechart);
        barChart=view.findViewById(R.id.barchart);





        ArrayList<PieEntry> finance = new ArrayList<>();
        finance.add(new PieEntry(11603, "Expense"));
        finance.add(new PieEntry(10720,"Income "));

        pieDataSet = new PieDataSet(finance, "              Finance Tracker");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        pieData = new PieData(pieDataSet);

        piechart.setData(pieData);
        piechart.getDescription().setEnabled(false);
        piechart.setCenterText("Finance");
        piechart.animate();



        getEntries();
        barDataSet = new BarDataSet(barEntries, "");
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(18f);
        return view;


    }

    private void getEntries() {
        barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0,11603 ));
        barEntries.add(new BarEntry(0,10720 ));

    }
}