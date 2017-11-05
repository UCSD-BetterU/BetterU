package com.betteru.ucsd.myapplication4;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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


public class RankingPeerFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private ListView mainListView ;
    private TextView noRecordView;
    private ArrayAdapter<String> listAdapter ;
    ArrayList<HashMap<String,String>> list;

    FirebaseFirestore db;
    String userId = "user0001";
    LocalDate d = LocalDate.now();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ranking_peer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        list = new ArrayList<HashMap<String, String>>();

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
        load(userId, monthS, dayS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd");
        String text = d.format(formatter);
        loadButton(text);
    }

    private void load(String userId, String month, String day)
    {
        DocumentReference dbUser;
        DocumentReference dbData;
        try {
            dbUser = db.collection("ranking").document(userId);
            dbData = dbUser.collection(month).document(day);
        }catch (Exception e)
        {
            Log.d("Exception", e.toString());
            return;
        }
        list.clear();
        dbData.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document != null){
                        if(!document.exists()){
                            loadNoRecordView(true);
                        } else {
                            Log.d("DATA IN CLOUT", document.getId() + " -> " + document.getData());
                            Map<String, Object> obj = document.getData();
                            for (String key : obj.keySet()) {
                                Log.d("DATA IN CLOUD", key);
                                HashMap<String, String> temp = new HashMap<String, String>();
                                temp.put("Activity", key);
                                temp.put("Ranking", document.getString(key));
                                list.add(temp);
                            }
                            Log.d("DATA in LIST", list.toString());
                            loadListView();
                            loadNoRecordView(false);
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
                        RankingPeerFragment.this,
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

    public static RankingPeerFragment newInstance() {
        RankingPeerFragment fragment = new RankingPeerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        d = LocalDate.of(year, monthOfYear+1, dayOfMonth);
        load(userId, d);
    }

    private class PickerAdapter extends FragmentPagerAdapter {
        private static final int NUM_PAGES = 2;
        Fragment datePickerFragment;

        PickerAdapter(FragmentManager fm) {
            super(fm);
            datePickerFragment = new DatePickerFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            return datePickerFragment;
        }
    }

}