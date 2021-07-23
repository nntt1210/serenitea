package com.example.serenitea;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends Fragment {

    BarChart chart;
    ArrayList<Bitmap> imageList = new ArrayList<>();
    int val[] = {3, 2, 7, 3, 4, 8};
    ArrayList<String> labels = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_statistics, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        chart = view.findViewById(R.id.barchart);

        labels.add("Sad");
        labels.add("Worried");
        labels.add("Angry");
        labels.add("Neutral");
        labels.add("Happy");

//        Bitmap starBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sad);
//        chart.setRenderer(new ImageBarChartRenderer(chart, chart.getAnimator(), chart.getViewPortHandler(), starBitmap));

//        chart.getXAxis().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);

        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawGridLines(false);

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 20f, "Sad"));
        entries.add(new BarEntry(1f, 15f, "Worried"));
        entries.add(new BarEntry(2f, 20f, "Angry"));
        entries.add(new BarEntry(3f, 25f, "Neutral"));
        entries.add(new BarEntry(4f, 40f, "Happy"));

        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.emotion_sad));
        colors.add(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.emotion_worried));
        colors.add(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.emotion_angry));
        colors.add(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.emotion_neutral));
        colors.add(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.emotion_happy));

        BarDataSet set = new BarDataSet(entries, "emotion");
        set.setColors(colors);
        set.setDrawIcons(true);



        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh

//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sad);
//        imageList.add(bitmap);
//        imageList.add(bitmap);
//        imageList.add(bitmap);
//        imageList.add(bitmap);
//        imageList.add(bitmap);
//        imageList.add(bitmap);
//
//
//        chart.setDrawBarShadow(false);
//        chart.setDrawValueAboveBar(true);
//        chart.getDescription().setEnabled(false);
//        chart.setPinchZoom(false);
//        chart.setDrawGridBackground(false);
//
//
//        XAxis xAxis = chart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);
//        xAxis.setGranularity(1f);
//        xAxis.setLabelCount(7);
//        xAxis.setDrawLabels(false);
//
//
//        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setAxisLineColor(Color.WHITE);
//        leftAxis.setDrawGridLines(false);
//        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//
//        YAxis rightAxis = chart.getAxisRight();
//        rightAxis.setEnabled(false);
//        Legend l = chart.getLegend();
//        l.setEnabled(false);
//        setData();

    }

//    private void setData() {
//
//        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
//        for (int i = 0; i < val.length; i++) {
//            yVals1.add(new BarEntry(i, val[i]));
//        }
//
//        BarDataSet set1;
//
//        set1 = new BarDataSet(yVals1, "");
//
//        set1.setColors(Color.BLUE);
//        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//        dataSets.add(set1);
//
//        BarData data = new BarData(dataSets);
//        data.setDrawValues(false);
//        chart.setData(data);
//        chart.setScaleEnabled(false);
//        chart.setRenderer(new BarChartCustomRenderer(chart, chart.getAnimator(), chart.getViewPortHandler(), imageList, getActivity().getApplicationContext()));
//        chart.setExtraOffsets(0, 0, 0, 20);
//
//    }

}