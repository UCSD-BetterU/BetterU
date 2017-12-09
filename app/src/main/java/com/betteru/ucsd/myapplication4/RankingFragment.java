package com.betteru.ucsd.myapplication4;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;


public class RankingFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private ListView mainListView ;
    private TextView noRecordView;
    private ArrayAdapter<String> listAdapter ;
    ArrayList<HashMap<String,String>> list;

    private boolean[] preferences = new boolean[51];
    HashMap<String, Long> selfRankingData;
    HashMap<String, Integer> rankingM;
    HashMap<String, Integer> rankingN;
    ArrayList<HashMap<String,ArrayList<Long> > > allRankingDatalist;
    HashMap<String, Integer> rankingMap;

    FirebaseFirestore db;
    String userId = "user0001";
    //LocalDate d = LocalDate.now();
    Calendar calendar = Calendar.getInstance();

    ProgressBar spinner;

    ExtrasensoryActivities activities = new ExtrasensoryActivities();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ranking, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        list = new ArrayList<HashMap<String, String>>();
        selfRankingData = new HashMap<>();
        rankingMap = new HashMap<>();
        rankingN = new HashMap<>();
        rankingM = new HashMap<>();

        spinner = (ProgressBar) view.findViewById(R.id.spinner);
        noRecordView = (TextView) getView().findViewById(R.id.textView_noRecord);

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
                load(userId, calendar);
            }
        });

        FloatingActionButton settingButton = (FloatingActionButton) getView().findViewById(R.id.button_setting);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new RankingSettingsDialogFragment();
                dialog.show(getFragmentManager(), "RankingSettingsDialogFragment");
            }
        });

        BroadcastReceiver dialogReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                load(userId, calendar);
            }
        };
        IntentFilter filter = new IntentFilter("DialogChangeSaved");
        getActivity().registerReceiver(dialogReceiver,filter);
    }

    private void load(String userId, Calendar calendar) {
        String monthS = String.format("%02d", calendar.get(Calendar.MONTH)+1);
        String dayS = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        String yearS = String.format("%04d", calendar.get(Calendar.YEAR));
        loadRankingPreferences(userId, yearS, monthS, dayS);
        //DateFormat formatter = DateTimeFormatter.ofPattern("MMM-dd");
        String text = yearS+"-"+monthS+"-"+dayS;
        loadButton(text);
    }

    private void loadRankingPreferences(String userId, String year, String month, String day) {
        spinner.setVisibility(View.VISIBLE);
        noRecordView.setVisibility(View.GONE);
        DocumentReference ranking_preferences;
        try {
            ranking_preferences = db.collection("ranking_preferences").document(userId);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
            return;
        }
        list.clear();
        final String id = userId;
        final String y = year;
        final String m = month;
        final String d = day;
        ranking_preferences.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (!document.exists()) {
                            Log.d("Data in Cloud", "no preferences");
                        } else {
                            Map<String, Object> obj = document.getData();
                            for (String key : obj.keySet()) {
                                int k = Integer.valueOf(key);
                                preferences[k] = document.getBoolean(key);
                            }
                            loadUserData(id, y, m, d);
                        }
                    }
                } else {
                    Log.d("Data in Cloud", "Error getting documents", task.getException());
                }
            }
        });
    }

    private void loadUserData(String userId, String year, String month, String day){
        DocumentReference dbData;
        Log.d("extrasensory",db.collection("extrasensory").getPath());
        dbData = db.collection("extrasensory").document(userId).collection(year+month).document(day);
        selfRankingData.clear();
        rankingM.clear();
        rankingN.clear();
        final String y = year;
        final String m = month;
        final String d = day;
        dbData.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document != null){
                        if(!document.exists()){
                            loadListView();
                            loadNoRecordView(true);
                        } else {
                            Log.d("DATA IN CLOUD", document.getId() + " -> " + document.getData());
                            Map<String, Object> obj = document.getData();
                            for (String key : obj.keySet()) {
                                if (!activities.map.containsKey(key)) {
                                    continue;
                                }
                                int index = activities.map.get(key).getIndex();
                                if (!preferences[index]) {
                                    continue;
                                }
                                selfRankingData.put(key, document.getLong(key));
                                rankingM.put(key, 0);
                                rankingN.put(key, 0);
                            }
                            Log.d("DATA in LIST", selfRankingData.toString());
                            loadAllUserData(y,m,d);
                        }
                    }
                }else{
                    Log.d("Data in Cloud", "Error getting documents" , task.getException());
                }
            }
        });
    }

    private void loadAllUserData1 (String year, String month, String day) {
        final String ym = year+month;
        final String d = day;
        list.clear();
        db.collection("extrasensory").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task){
                if(task.isSuccessful()){
                    for (DocumentSnapshot doc : task.getResult()) {
                        Log.d("test",doc.getId());
                        doc.getReference().collection(ym).document(d).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                                if(task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if(document != null){
                                        if(!document.exists()){
                                            loadListView();
                                            loadNoRecordView(true);
                                            spinner.setVisibility(View.GONE);
                                        } else {
                                            Log.d("DATA IN CLOUD", document.getId() + " -> " + document.getData());
                                            Map<String, Object> obj = document.getData();
                                            TreeMap<Float, String> rankings = new TreeMap<>();
                                            for (String key : obj.keySet()) {
                                                if (!selfRankingData.containsKey(key)) {
                                                    continue;
                                                }
                                                if(document.getLong(key) <= selfRankingData.get(key)) {
                                                    rankingM.put(key, rankingM.get(key)+1);
                                                }
                                                rankingN.put(key, rankingN.get(key)+1);
                                            }
                                            for (String key: selfRankingData.keySet()) {
                                                float m = (float)rankingM.get(key);
                                                float n = (float)rankingN.get(key);
                                                float r = 100 *  m / n;
                                                Log.d("ha????", key+" "+m+"/"+n+" "+r+" "+ String.format("%.0f%%", r));
                                            }
                                        }
                                    }
                                }else{
                                    Log.d("Data in Cloud", "Error getting documents" , task.getException());
                                }
                            }
                        });
                    }
                    for (String key: selfRankingData.keySet()) {
                        HashMap<String,String> temp = new HashMap<>();
                        temp.put("Activity", key);
                        float m = (float)rankingM.get(key);
                        float n = (float)rankingN.get(key);
                        float r = 100 *  m / n;
                        temp.put("Ranking", String.format("%.0f%%", r));
                        Log.d("checkcheck", key+" "+m+"/"+n+" "+r+" "+ String.format("%.0f%%", r));
                        list.add(temp);
                    }
                    loadListView();
                    spinner.setVisibility(View.GONE);
                }else{
                    Log.d("Data in Cloud", "Error getting documents" , task.getException());
                }
            }
        });
    }

    private void loadAllUserData(String year, String month, String day) {
        DocumentReference dbData;
        dbData = db.collection("alluser").document(year+month+day);
        rankingMap.clear();
        list.clear();
        dbData.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document != null){
                        if(!document.exists()){
                            loadListView();
                            loadNoRecordView(true);
                            spinner.setVisibility(View.GONE);
                        } else {
                            Log.d("DATA IN CLOUD", document.getId() + " -> " + document.getData());
                            Map<String, Object> obj = document.getData();
                            TreeMap<Float, String> rankings = new TreeMap<>();
                            for (String key : obj.keySet()) {
                                if (!selfRankingData.containsKey(key)) {
                                    continue;
                                }
                                long selfTime = selfRankingData.get(key);
                                Log.d("self data", key + " -- " + Long.toString(selfTime));
                                ArrayList allUserData = (ArrayList) document.get(key);
                                Log.d("check", document.get(key).toString()+" "+allUserData.size());
                                int n = allUserData.size();
                                int r = 0;
                                for(Object t: allUserData){
                                    if(selfTime>=(long)t) r++;
                                }
                                float rank = 100 * (float) r / (float) n;
                                String ranking = String.format("%.0f%%", rank);
                                Log.d("ranking", key+" "+ranking);
                                rankings.put(rank, key);
                            }
                            NavigableMap<Float, String> descendingMap = rankings.descendingMap();
                            for(NavigableMap.Entry<Float,String> entry: descendingMap.entrySet()){
                                HashMap<String,String> temp = new HashMap<>();
                                temp.put("Activity", entry.getValue());
                                temp.put("Ranking", String.format("%.0f%%", entry.getKey()));
                                list.add(temp);
                            }
                            loadListView();
                            spinner.setVisibility(View.GONE);
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
                        RankingFragment.this,
                        /*d.getYear(),
                        d.getMonthValue()-1,
                        d.getDayOfMonth()*/
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH)+1,
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
        if(flag == true) noRecordView.setVisibility(View.VISIBLE);
    }
    private void loadListView(){
        ListView listView=(ListView) getView().findViewById(R.id.rankingListView);
        RankingAdapter adapter=new RankingAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                int pos=position+1;
                Toast.makeText(getActivity(), Integer.toString(pos)+" Clicked", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public static RankingFragment newInstance() {
        RankingFragment fragment = new RankingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        /*d = LocalDate.of(year, monthOfYear+1, dayOfMonth);
        load(userId, d);*/
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear+1);
        calendar.set(Calendar.MINUTE, dayOfMonth);
        load(userId, calendar);
    }
}