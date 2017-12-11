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
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;


public class SummaryFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private ProgressBar spinner;
    private TextView noRecordView;
    ArrayList<HashMap<String,String>> list;
    TextView text_1, text_2, text_3;
    LinearLayout medal_1, medal_2, medal_3;

    private TreeMap<Long, String> timeSpent1 = new TreeMap<>();
    private TreeMap<Long, String> timeSpent2 = new TreeMap<>();
    private TreeMap<Long, String> timeSpent3 = new TreeMap<>();

    FirebaseFirestore db;
    UserModel currentFBUser; //= ((BetterUApplication) getActivity().getApplication()).getCurrentFBUser();
    String userId = "user0001";
    //LocalDate d = LocalDate.now();
    Calendar calendar = Calendar.getInstance();

    RelativeLayout chart1, chart2, chart3;
    TextView textView1, textView2, textView3;
    HorizontalBarChart barChart ;
    PieChart pieChart1;
    PieChart pieChart2;

    ExtrasensoryActivities activities = new ExtrasensoryActivities();
    int nact2 = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        calendar.add(Calendar.DATE, -1);

        currentFBUser = ((BetterUApplication) getActivity().getApplication()).getCurrentFBUser();
        userId = currentFBUser.getUserId();

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

        load(userId, calendar);

        FloatingActionButton prevButton = (FloatingActionButton) view.findViewById(R.id.button_prevDate);
        prevButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -1);
                load(userId, calendar);
            }
        });

        FloatingActionButton nextButton = (FloatingActionButton) view.findViewById(R.id.button_nextDate);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 1);
                load(userId, calendar);
            }
        });

        FloatingActionButton todayButton = (FloatingActionButton) view.findViewById(R.id.button_today);
        todayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE,-1);
                load(userId, calendar);
            }
        });
    }

    private void load(String userId, Calendar calendar) {
        String monthS = String.format("%02d", calendar.get(Calendar.MONTH)+1);
        String dayS = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        String yearS = String.format("%04d", calendar.get(Calendar.YEAR));
        load(userId, yearS, monthS, dayS);
        //DateFormat formatter = DateTimeFormatter.ofPattern("MMM-dd");
        String text = yearS+"-"+monthS+"-"+dayS;
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
        nact2 = 0;

        /*db.collection("extrasensory").document(userId).collection("201711").document("14")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null) {
                    if (!document.exists()) {
                    } else {
                        Log.d("DATA IN CLOUDhaha", document.getId() + " -> " + document.getData());
                        Map<String, Object> obj = document.getData();
                        db.collection("extrasensory").document("user0"+k).collection("201712").document("08").update(obj);
                    }}}}});*/

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
                            if (obj.isEmpty()){
                                loadNoRecordView(true);
                            }
                            for (String key : obj.keySet()) {
                                if (!activities.map.containsKey(key)) {
                                    continue;
                                }
                                int type = activities.map.get(key).getType();
                                Long time = document.getLong(key);
                                switch (type) {
                                    case 1:
                                        if (timeSpent1.containsKey(time)) {
                                            timeSpent1.put(time, timeSpent1.get(time)+";"+key);
                                        } else {
                                            timeSpent1.put(time, key);
                                        }
                                        break;
                                    case 2:
                                        if (timeSpent2.containsKey(time)) {
                                            timeSpent2.put(time, timeSpent2.get(time)+";"+key);
                                        } else {
                                            timeSpent2.put(time, key);
                                        }
                                        nact2++;
                                        break;
                                    case 3:
                                        if (timeSpent3.containsKey(time)) {
                                            timeSpent3.put(time, timeSpent3.get(time)+";"+key);
                                        } else {
                                            timeSpent3.put(time, key);
                                        }
                                        break;
                                    case 5:
                                        if (timeSpent1.containsKey(time)) {
                                            timeSpent1.put(time, timeSpent1.get(time)+";"+key);
                                        } else {
                                            timeSpent1.put(time, key);
                                        }
                                        if (timeSpent2.containsKey(time)) {
                                            timeSpent2.put(time, timeSpent2.get(time)+";"+key);
                                        } else {
                                            timeSpent2.put(time, key);
                                        }
                                        nact2++;
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
                                int nr2 = -1;
                                ArrayList<String> top3text = new ArrayList<>();
                                ArrayList<Long> top3time = new ArrayList<>();
                                for (NavigableMap.Entry<Long, String> entry : timeSpent2.entrySet()) {
                                    String [] acts = entry.getValue().split(";");
                                    for (String act : acts) {
                                        k2++;
                                        if(nact2-k2>=15){continue;}
                                        nr2++;
                                        String label = activities.map.get(act).getLabel();
                                        String text = activities.map.get(act).getText();
                                        BarEntryLabels.add(label);
                                        BARENTRY.add(new BarEntry(entry.getKey(), nr2));
                                        if((nact2-k2)<3){
                                            top3text.add(text);
                                            top3time.add(entry.getKey());
                                        }
                                    }
                                }
                                int topK = top3text.size();
                                if (topK > 0) {
                                    textView1.setVisibility(View.VISIBLE);
                                    medal_1.setVisibility(View.VISIBLE);
                                    text_1.setText("You spent " + top3time.get(topK-1) + " min " + top3text.get(topK-1));
                                }
                                if (topK > 1) {
                                    textView2.setVisibility(View.VISIBLE);
                                    medal_2.setVisibility(View.VISIBLE);
                                    text_2.setText("You spent " + top3time.get(topK-2) + " min " + top3text.get(topK-2));
                                }
                                if (topK > 2) {
                                    textView2.setVisibility(View.VISIBLE);
                                    medal_3.setVisibility(View.VISIBLE);
                                    text_3.setText("You spent " + top3time.get(topK-3) + " min " + top3text.get(topK-3));
                                }

                                BarDataSet barDataSet = new BarDataSet(BARENTRY, "Top "+(nr2+1)+" Activities");
                                BarData barData = new BarData(BarEntryLabels, barDataSet);
                                barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                                barChart.setData(barData);
                                barChart.setDescription("");
                                chart1.getLayoutParams().height = (nr2+1) * 160 + 100;
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
                                    String [] acts = entry.getValue().split(";");
                                    for (String act : acts) {
                                        String label = activities.map.get(act).getLabel();
                                        pieX1.add(label);
                                        pieY1.add(new Entry(entry.getKey(), k1));
                                        k1++;
                                    }
                                }
                                PieDataSet pieDataSet1 = new PieDataSet(pieY1, "Status");
                                pieDataSet1.setColors(ColorTemplate.VORDIPLOM_COLORS);
                                PieData pieData1 = new PieData(pieX1, pieDataSet1);
                                //pieData1.setValueFormatter(new PercentFormatter());
                                pieData1.setValueFormatter(new PercentFormatter() {
                                    @Override
                                    public String getFormattedValue(float value, Entry entry,int dataSetIndex, ViewPortHandler viewPortHandler) {
                                        return Float.toString(entry.getVal());
                                    }
                                });
                                pieData1.setValueTextSize(10f);
                                pieChart1.setData(pieData1);
                                pieChart1.setDescription("");
                                pieChart1.setHoleColor(Color.parseColor("#f5f5f5"));
                                spinner.setVisibility(View.GONE);
                                textView2.setVisibility(View.VISIBLE);
                                chart2.setVisibility(View.VISIBLE);
                                pieChart1.animateY(2000);
                            }

                            if(!timeSpent3.isEmpty()) {
                                NavigableMap<Long, String> descMap3 = timeSpent3.descendingMap();
                                int k3 = 0;
                                ArrayList<Entry> pieY2 = new ArrayList<Entry>();
                                ArrayList<String> pieX2 = new ArrayList<String>();
                                for (NavigableMap.Entry<Long, String> entry : descMap3.entrySet()) {
                                    String [] acts = entry.getValue().split(";");
                                    for (String act : acts) {
                                        String label = activities.map.get(act).getLabel();
                                        pieX2.add(label);
                                        pieY2.add(new Entry(entry.getKey(), k3));
                                        k3++;
                                    }
                                }
                                PieDataSet pieDataSet2 = new PieDataSet(pieY2, "Locations");
                                pieDataSet2.setColors(ColorTemplate.PASTEL_COLORS);
                                PieData pieData2 = new PieData(pieX2, pieDataSet2);
                                //pieData2.setValueFormatter(new PercentFormatter());
                                pieData2.setValueFormatter(new PercentFormatter() {
                                    @Override
                                    public String getFormattedValue(float value, Entry entry,int dataSetIndex, ViewPortHandler viewPortHandler) {
                                        return Float.toString(entry.getVal());
                                    }
                                });
                                pieData2.setValueTextSize(10f);
                                pieChart2.setData(pieData2);
                                pieChart2.setDescription("");
                                pieChart2.setHoleColor(Color.parseColor("#f5f5f5"));
                                spinner.setVisibility(View.GONE);
                                textView3.setVisibility(View.VISIBLE);
                                chart3.setVisibility(View.VISIBLE);
                                pieChart2.animateY(2000);
                            }
                        }
                    }
                }else{
                    Log.d("Data in Cloud", "Error getting documents" , task.getException());
                }
            }
        });
        Log.d("User ID in Report", userId);
    }

    private void loadButton(String date){
        Button button = (Button) getView().findViewById(R.id.button_date);
        button.setText(date);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //showDialog();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        SummaryFragment.this,
                        /*d.getYear(),
                        d.getMonthValue()-1,
                        d.getDayOfMonth()*/
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
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

    public static SummaryFragment newInstance() {
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        /*d = LocalDate.of(year, monthOfYear+1, dayOfMonth);
        load(userId, d);*/
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        load(userId, calendar);
    }
}