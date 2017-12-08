package com.betteru.ucsd.myapplication4;

import android.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
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
    private ListView mainListView ;
    private TextView noRecordView;
    private ArrayAdapter<String> listAdapter ;
    ArrayList<HashMap<String,String>> list;
    TextView text_1, text_2, text_3;
    LinearLayout medal_1, medal_2, medal_3;

    private TreeMap<Long, String> timeSpent = new TreeMap<>();

    FirebaseFirestore db;
    String userId = "user0001";
    LocalDate d = LocalDate.now();

    BarChart barChart ;
    PieChart pieChart;

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

        barChart = (BarChart) view.findViewById(R.id.bar_chart);
        pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        pieChart.setUsePercentValues(true);

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
        barChart.setVisibility(View.GONE);
        pieChart.setVisibility(View.GONE);

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
        timeSpent.clear();
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
                                timeSpent.put(document.getLong(key), key);
                            }
                            Log.d("DATA in LIST", timeSpent.toString());

                            ArrayList<BarEntry> BARENTRY = new ArrayList<>();
                            ArrayList<String> BarEntryLabels = new ArrayList<String>();

                            NavigableMap<Long, String> descMap = timeSpent.descendingMap();
                            int k2 = 0;
                            for(NavigableMap.Entry<Long, String> entry : descMap.entrySet()){
                                String time = Long.toString(entry.getKey());
                                String activity = entry.getValue();
                                if(k2==0) {
                                    medal_1.setVisibility(View.VISIBLE);
                                    text_1.setText("You spent "+time+" min "+activity);
                                } else if(k2==1) {
                                    medal_2.setVisibility(View.VISIBLE);
                                    text_2.setText("You spent "+time+" min "+activity);
                                } else if(k2==2) {
                                    medal_3.setVisibility(View.VISIBLE);
                                    text_3.setText("You spent "+time+" min "+activity);}
                                BARENTRY.add(new BarEntry(entry.getKey(), k2));
                                BarEntryLabels.add(entry.getValue());
                                k2++;
                            }
                            BarDataSet barDataSet = new BarDataSet(BARENTRY, "Secondary Activities");
                            BarData barData = new BarData(BarEntryLabels, barDataSet);
                            barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                            barChart.setData(barData);
                            barChart.setDescription("");
                            barChart.setVisibility(View.VISIBLE);
                            barChart.animateY(2000);

                            int k1 = 0;
                            ArrayList<Entry> pieY = new ArrayList<Entry>();
                            ArrayList<String> pieX = new ArrayList<String>();
                            for(NavigableMap.Entry<Long, String> entry : descMap.entrySet()){
                                pieY.add(new Entry(entry.getKey(), k1));
                                pieX.add(entry.getValue());
                                k1++;
                            }
                            PieDataSet pieDataSet = new PieDataSet(pieY, "Main Activities");
                            pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                            PieData pieData = new PieData(pieX, pieDataSet);
                            pieData.setValueFormatter(new PercentFormatter());
                            pieChart.setData(pieData);
                            pieChart.setDescription("");
                            pieChart.setVisibility(View.VISIBLE);
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
        noRecordView = (TextView) getView().findViewById(R.id.textView_noRecord);
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