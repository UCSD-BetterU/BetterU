package com.betteru.ucsd.myapplication4;

import android.app.DialogFragment;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RankingFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private ListView mainListView ;
    private TextView noRecordView;
    private ArrayAdapter<String> listAdapter ;
    ArrayList<HashMap<String,String>> list;

    private boolean[] preferences = new boolean[51];
    HashMap<String, Long> selfRankingData;
    HashMap<String, Integer> rankingMap;

    FirebaseFirestore db;
    String userId = "user0001";
    LocalDate d = LocalDate.now();

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

        spinner = (ProgressBar) view.findViewById(R.id.spinner);
        noRecordView = (TextView) getView().findViewById(R.id.textView_noRecord);

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

        FloatingActionButton settingButton = (FloatingActionButton) getView().findViewById(R.id.button_setting);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new RankingSettingsDialogFragment();
                dialog.show(getFragmentManager(), "RankingSettingsDialogFragment");
            }
        });
    }

    private void load(String userId, LocalDate d) {
        String monthS = String.format("%02d", d.getMonthValue());
        String dayS = String.format("%02d", d.getDayOfMonth());
        String yearS = String.format("%04d", d.getYear());
        loadRankingPreferences(userId, yearS, monthS, dayS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd");
        String text = d.format(formatter);
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
        dbData = db.collection("extrasensory").document(userId).collection(year+month).document(day);
        selfRankingData.clear();
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
                                HashMap<String, String> temp = new HashMap<String, String>();
                                temp.put("Activity", key);
                                temp.put("Ranking", ranking);
                                list.add(temp);
                                loadListView();
                                spinner.setVisibility(View.GONE);
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
                        RankingFragment.this,
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
        d = LocalDate.of(year, monthOfYear+1, dayOfMonth);
        load(userId, d);
    }
}