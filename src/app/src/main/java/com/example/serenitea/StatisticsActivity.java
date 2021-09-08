package com.example.serenitea;

import android.graphics.Bitmap;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatisticsActivity extends Fragment {
    int[] days = {0, 0, 0, 0, 0};
    ArrayList<Diary> diaryList = new ArrayList<>();
    DatabaseReference emoRef;
    BarChart chart;
    ArrayList<Bitmap> imageList = new ArrayList<>();
    int val[] = {3, 2, 7, 3, 4, 8};
    ArrayList<String> labels = new ArrayList<>();
    List<BarEntry> entries = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_statistics, container, false);
    }

    public class Diary {
        String happy;
        String sad;
        String nervous;
        String neutral;
        String angry;

        public Diary() {
        }

        public Diary(String s, String h, String neu, String a, String ner) {
            sad = s;
            happy = h;
            neutral = neu;
            angry = a;
            nervous = ner;
        }

        public String getAngry() {
            return angry;
        }

        public String getHappy() {
            return happy;
        }

        public String getNeutral() {
            return neutral;
        }

        public String getNervous() {
            return nervous;
        }

        public String getSad() {
            return sad;
        }
    }

    public int maximum(int[] a) {
        int index = 0;
        int max = a[0];
        for (int i = 0; i < 5; i++) {
            if (a[i] > max) {
                max = a[i];
                index = i;
            }
        }
        return index;
    }

    public Boolean checkAllEqual(int[] a) {
        for (int i = 0; i < 4; i++) {
            if (a[i] != a[i + 1])
                return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        takeStatistics(view);


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
        chart.getAxisLeft().setAxisMinimum(0f);
        chart.getAxisRight().setAxisMinimum(0f);
//        chart.getXAxis().setCenterAxisLabels(false);
//        chart.getXAxis().setGranularity(1f);


        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.emotion_sad));
        colors.add(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.emotion_worried));
        colors.add(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.emotion_angry));
        colors.add(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.emotion_neutral));
        colors.add(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.emotion_happy));

        entries.add(new BarEntry(0f, 0, "Sad"));
        entries.add(new BarEntry(1f, 0, "Worried"));
        entries.add(new BarEntry(2f, 0, "Angry"));
        entries.add(new BarEntry(3f, 0, "Neutral"));
        entries.add(new BarEntry(4f, 0, "Happy"));

        BarDataSet set = new BarDataSet(entries, "emotion");
        set.setColors(colors);
        set.setDrawIcons(true);

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh*/
        //takeStatistics(view);
    }

    /*public interface CallBack
{
    void onCallBack(ArrayList<Diary> diaryList);
}
    public void readStatistics (CallBack cb)
    {
        emoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ss : snapshot.getChildren())
                {
                    String s = ss.child("1").getValue().toString();
                    String h = ss.child("2").getValue().toString();
                    String neu = ss.child("3").getValue().toString();
                    String a = ss.child("4").getValue().toString();
                    String ner = ss.child("5").getValue().toString();
                    Diary temp = new Diary(s,h,neu,a,ner);
                    diaryList.add(temp);
                    Log.i("array value", String.valueOf(diaryList.size()));
                }
                cb.onCallBack(diaryList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }*/
    private void takeStatistics(View view) {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        String m = String.valueOf(month);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String curUser = mAuth.getCurrentUser().getUid();
        emoRef = FirebaseDatabase.getInstance().getReference("diary/" + curUser);
        emoRef.child(m).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ss : task.getResult().getChildren()) {
                        String s = ss.child("1").getValue().toString();
                        String h = ss.child("2").getValue().toString();
                        String neu = ss.child("3").getValue().toString();
                        String a = ss.child("4").getValue().toString();
                        String ner = ss.child("5").getValue().toString();
                        Diary temp = new Diary(s, h, neu, a, ner);
                        diaryList.add(temp);
                        Log.i("real run", String.valueOf(diaryList.size()));
                    }
                    for (int i = 0; i < diaryList.size(); i++) {
                        int[] temp = new int[5];
                        temp[0] = Integer.parseInt(diaryList.get(i).getSad());
                        temp[1] = Integer.parseInt(diaryList.get(i).getHappy());
                        temp[2] = Integer.parseInt(diaryList.get(i).getNeutral());
                        temp[3] = Integer.parseInt(diaryList.get(i).getAngry());
                        temp[4] = Integer.parseInt(diaryList.get(i).getNervous());
                        if (checkAllEqual(temp))
                            days[2] += 1;
                        else {
                            int emo = maximum(temp);
                            days[emo] += 1;
                        }
                    }
                    if (!(checkAllEqual(days) && (days[0] == 0))) {
                        chart = view.findViewById(R.id.barchart);
                        /*labels.add("Sad");
                        labels.add("Worried");
                        labels.add("Angry");
                        labels.add("Neutral");
                        labels.add("Happy");*/

                        /*chart.getAxisLeft().setEnabled(false);
                        chart.getAxisRight().setEnabled(false);
                        chart.getDescription().setEnabled(false);
                        chart.getLegend().setEnabled(false);

                        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                        chart.getXAxis().setDrawGridLines(false);*/


                        List<BarEntry> entries = new ArrayList<>();
                        entries.add(new BarEntry(0f, days[0], "Sad"));
                        entries.add(new BarEntry(1f, days[4], "Worried"));
                        entries.add(new BarEntry(2f, days[3], "Angry"));
                        entries.add(new BarEntry(3f, days[2], "Neutral"));
                        entries.add(new BarEntry(4f, days[1], "Happy"));

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
                        data.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {

                                if (value > 0) {
                                    return super.getFormattedValue(value);
                                } else {
                                    return "";
                                }
                            }

                        });
                        data.setBarWidth(0.9f); // set custom bar width
                        chart.setData(data);
                        chart.setFitBars(true); // make the x-axis fit exactly all bars
                        chart.invalidate(); // refresh
                    }
                }
            }
        });

        /*readStatistics(new CallBack() {
            @Override
            public void onCallBack(ArrayList<Diary> diaryList) {
                for (int i = 0; i < diaryList.size(); i++) {
                    int[] temp = new int[5];
                    temp[0] = Integer.parseInt(diaryList.get(i).getSad());
                    temp[1] = Integer.parseInt(diaryList.get(i).getHappy());
                    temp[2] = Integer.parseInt(diaryList.get(i).getNeutral());
                    temp[3] = Integer.parseInt(diaryList.get(i).getAngry());
                    temp[4] = Integer.parseInt(diaryList.get(i).getNervous());
                    if (checkAllEqual(temp))
                        days[2] += 1;
                    else {
                        int emo = maximum(temp);
                        days[emo] += 1;
                    }
                }
            }
        });*/
    }
}