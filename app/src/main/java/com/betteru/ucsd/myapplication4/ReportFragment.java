package com.betteru.ucsd.myapplication4;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;


public class ReportFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private ProgressBar spinner;
    private TextView noRecordView;
    ArrayList<HashMap<String,String>> list;
    TextView text_1, text_2, text_3;
    LinearLayout medal_1, medal_2, medal_3;

    private TreeMap<Long, String> timeSpent1 = new TreeMap<>();
    private TreeMap<Long, String> timeSpent2 = new TreeMap<>();
    private TreeMap<Long, String> timeSpent3 = new TreeMap<>();

    FirebaseFirestore db;
    String userId = "user0001";
    LocalDate d = LocalDate.now();

    RelativeLayout chart1, chart2, chart3;
    TextView textView1, textView2, textView3;
    HorizontalBarChart barChart ;
    PieChart pieChart1;
    PieChart pieChart2;

    ExtrasensoryActivities activities = new ExtrasensoryActivities();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        list = new ArrayList<HashMap<String, String>>();

        medal_1 = (LinearLayout) view.findViewById(R.id.medal_1);
        medal_2 = (LinearLayout) view.findViewById(R.id.medal_2);
        medal_3 = (LinearLayout) view.findViewById(R.id.medal_3);

        text_1 = (TextView) view.findViewById(R.id.text_1);
        text_2 = (TextView) view.findViewById(R.id.text_2);
        text_3 = (TextView) view.findViewById(R.id.text_3);

        textView1 = (TextView) view.findViewById(R.id.textView_1);
        textView2 = (TextView) view.findViewById(R.id.textView_2);
        textView3 = (TextView) view.findViewById(R.id.textView_3);

        chart1 = (RelativeLayout) view.findViewById(R.id.chartLayout1);
        chart2 = (RelativeLayout) view.findViewById(R.id.chartLayout2);
        chart3 = (RelativeLayout) view.findViewById(R.id.chartLayout3);

        barChart = (HorizontalBarChart) view.findViewById(R.id.bar_chart);
        pieChart1 = (PieChart) view.findViewById(R.id.pie_chart1);
        pieChart1.setUsePercentValues(true);
        pieChart2 = (PieChart) view.findViewById(R.id.pie_chart2);
        pieChart2.setUsePercentValues(true);

        noRecordView = (TextView) getView().findViewById(R.id.textView_noRecord);
        spinner = (ProgressBar) getView().findViewById(R.id.spinner);

        load(userId, d);

        FloatingActionButton prevButton = (FloatingActionButton) view.findViewById(R.id.button_prevDate);
        prevButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                d = d.minusDays(1);
                load(userId, d);
            }
        });

        FloatingActionButton nextButton = (FloatingActionButton) view.findViewById(R.id.button_nextDate);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                d = d.plusDays(1);
                load(userId, d);
            }
        });

        FloatingActionButton todayButton = (FloatingActionButton) view.findViewById(R.id.button_today);
        todayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                d = LocalDate.now();
                load(userId, d);
            }
        });
    }

    private void load(String userId, LocalDate d) {
        String monthS = String.format("%02d", d.getMonthValue());
        String dayS = String.format("%02d", d.getDayOfMonth());
        String yearS = String.format("%04d", d.getYear());
        load(userId, yearS, monthS, dayS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd");
        String text = d.format(formatter);
        loadButton(text);
    }

    private void load(String userId, String year, String month, String day)
    {
        medal_1.setVisibility(View.GONE);
        medal_2.setVisibility(View.GONE);
        medal_3.setVisibility(View.GONE);
        chart1.setVisibility(View.GONE);
        chart2.setVisibility(View.GONE);
        chart3.setVisibility(View.GONE);
        textView1.setVisibility(View.GONE);
        textView2.setVisibility(View.GONE);
        textView3.setVisibility(View.GONE);
        noRecordView.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);

        DocumentReference dbUser;
        DocumentReference dbData;
        try {
            dbUser = db.collection("extrasensory").document(userId);
            dbData = dbUser.collection(year+month).document(day);
        }catch (Exception e)
        {
            Log.d("Exception", e.toString());
            return;
        }
        timeSpent1.clear();
        timeSpent2.clear();
        timeSpent3.clear();
        dbData.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document != null){
                        if(!document.exists()){
                            loadNoRecordView(true);
                        } else {
                            Log.d("DATA IN CLOUD", document.getId() + " -> " + document.getData());
                            Map<String, Object> obj = document.getData();
                            for (String key : obj.keySet()) {
                                if (!activities.map.containsKey(key)) {
                                    continue;
                                }
                                int type = activities.map.get(key).getType();
                                switch (type) {
                                    case 1:
                                        timeSpent1.put(document.getLong(key), key);
                                        break;
                                    case 2:
                                        timeSpent2.put(document.getLong(key), key);
                                        break;
                                    case 3:
                                        timeSpent3.put(document.getLong(key), key);
                                        break;
                                    case 5:
                                        timeSpent1.put(document.getLong(key), key);
                                        timeSpent2.put(document.getLong(key), key);
                                        break;
                                    default:
                                        break;
                                }
                            }
                            Log.d("DATA in LIST1", timeSpent1.toString());
                            Log.d("DATA in LIST2", timeSpent2.toString());
                            Log.d("DATA in LIST3", timeSpent3.toString());

                            ArrayList<BarEntry> BARENTRY = new ArrayList<>();
                            ArrayList<String> BarEntryLabels = new ArrayList<String>();

                            if(!timeSpent2.isEmpty()) {
                                NavigableMap<Long, String> descMap2 = timeSpent2.descendingMap();
                                int k2 = 0;
                                for (NavigableMap.Entry<Long, String> entry : descMap2.entrySet()) {
                                    if (k2 > 2) break;
                                    String time = Long.toString(entry.getKey());
                                    String text = activities.map.get(entry.getValue()).getText();
                                    if (k2 == 0) {
                                        textView1.setVisibility(View.VISIBLE);
                                        medal_1.setVisibility(View.VISIBLE);
                                        text_1.setText("You spent " + time + " min " + text);
                                    } else if (k2 == 1) {
                                        medal_2.setVisibility(View.VISIBLE);
                                        text_2.setText("You spent " + time + " min " + text);
                                    } else if (k2 == 2) {
                                        medal_3.setVisibility(View.VISIBLE);
                                        text_3.setText("You spent " + time + " min " + text);
                                    }
                                    k2++;
                                }
                                k2 = 0;
                                for (NavigableMap.Entry<Long, String> entry : timeSpent2.entrySet()) {
                                    BARENTRY.add(new BarEntry(entry.getKey(), k2));
                                    BarEntryLabels.add(entry.getValue());
                                    k2++;
                                }
                                BarDataSet barDataSet = new BarDataSet(BARENTRY, "Activities");
                                BarData barData = new BarData(BarEntryLabels, barDataSet);
                                barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                                barChart.setData(barData);
                                barChart.setDescription("");
                                chart1.getLayoutParams().height = k2 * 160 + 100;
                                spinner.setVisibility(View.GONE);
                                chart1.setVisibility(View.VISIBLE);
                                barChart.animateY(2000);
                            }

                            if(!timeSpent1.isEmpty()) {
                                NavigableMap<Long, String> descMap1 = timeSpent1.descendingMap();
                                int k1 = 0;
                                ArrayList<Entry> pieY1 = new ArrayList<Entry>();
                                ArrayList<String> pieX1 = new ArrayList<String>();
                                for (NavigableMap.Entry<Long, String> entry : descMap1.entrySet()) {
                                    pieY1.add(new Entry(entry.getKey(), k1));
                                    String label = activities.map.get(entry.getValue()).getLabel();
                                    pieX1.add(label);
                                    k1++;
                                }
                                PieDataSet pieDataSet1 = new PieDataSet(pieY1, "Status");
                                pieDataSet1.setColors(ColorTemplate.VORDIPLOM_COLORS);
                                PieData pieData1 = new PieData(pieX1, pieDataSet1);
                                pieData1.setValueFormatter(new PercentFormatter());
                                pieChart1.setData(pieData1);
                                pieChart1.setDescription("");
                                pieChart1.setHoleColor(Color.parseColor("#f5f5f5"));
                                spinner.setVisibility(View.GONE);
                                textView2.setVisibility(View.VISIBLE);
                                chart2.setVisibility(View.VISIBLE);
                            }

                            if(!timeSpent3.isEmpty()) {
                                NavigableMap<Long, String> descMap3 = timeSpent3.descendingMap();
                                int k3 = 0;
                                ArrayList<Entry> pieY2 = new ArrayList<Entry>();
                                ArrayList<String> pieX2 = new ArrayList<String>();
                                for (NavigableMap.Entry<Long, String> entry : descMap3.entrySet()) {
                                    pieY2.add(new Entry(entry.getKey(), k3));
                                    String label = activities.map.get(entry.getValue()).getLabel();
                                    pieX2.add(label);
                                    k3++;
                                }
                                PieDataSet pieDataSet2 = new PieDataSet(pieY2, "Locations");
                                pieDataSet2.setColors(ColorTemplate.PASTEL_COLORS);
                                PieData pieData2 = new PieData(pieX2, pieDataSet2);
                                pieData2.setValueFormatter(new PercentFormatter());
                                pieChart2.setData(pieData2);
                                pieChart2.setDescription("");
                                pieChart2.setHoleColor(Color.parseColor("#f5f5f5"));
                                spinner.setVisibility(View.GONE);
                                textView3.setVisibility(View.VISIBLE);
                                chart3.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }else{
                    Log.d("Data in Cloud", "Error getting documents" , task.getException());
                }
            }
        });
    }

    private void loadButton(String date){
        Button button = (Button) getView().findViewById(R.id.button_date);
        button.setText(date);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //showDialog();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ReportFragment.this,
                        d.getYear(),
                        d.getMonthValue()-1,
                        d.getDayOfMonth()
                );
                dpd.setThemeDark(false);
                dpd.vibrate(true);
                dpd.dismissOnPause(false);
                dpd.showYearPickerFirst(false);
                dpd.setVersion(DatePickerDialog.Version.VERSION_1);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }
    private void loadNoRecordView(Boolean flag){
        spinner.setVisibility(View.GONE);
        if(flag == true) noRecordView.setVisibility(View.VISIBLE);
        else noRecordView.setVisibility(View.GONE);
    }
    private void loadListView(){
        ListView listView=(ListView) getView().findViewById(R.id.rankingListView);
        RankingAdapter adapter=new RankingAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                int pos=position+1;
                Toast.makeText(getActivity(), Integer.toString(pos)+" Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static ReportFragment newInstance() {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        d = LocalDate.of(year, monthOfYear+1, dayOfMonth);
        load(userId, d);
    }
}