package com.betteru.ucsd.myapplication4;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.*;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.R.id.list;

/**
 * Created by Yuting on 10/30/2017.
 */

public class RankingActivity extends AppCompatActivity implements BasicFragment.NoticeDialogListener{
    // Write a message to the database
    private ListView mainListView ;
    private TextView noRecordView;
    private ArrayAdapter<String> listAdapter ;
    ArrayList<HashMap<String,String>> list;

    FirebaseFirestore db;
    String userId = "user0001";
    LocalDate d = LocalDate.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        db = FirebaseFirestore.getInstance();
        list = new ArrayList<HashMap<String,String>>();
        load(userId, d);

        FloatingActionButton prevButton = (FloatingActionButton) findViewById(R.id.button_prevDate);
        prevButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                d = d.minusDays(1);
                load(userId, d);
            }
        });

        FloatingActionButton nextButton = (FloatingActionButton) findViewById(R.id.button_nextDate);
        nextButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v){
                d = d.plusDays(1);
                load(userId, d);
            }
        });


    }
    private void load(String userId, LocalDate d)
    {
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
                            Log.d("DATA IN CLOUT", document.getId() + " -> " + document.getData());
                            Map<String, Object> obj = document.getData();
                            for(String key: obj.keySet()) {
                                Log.d("DATA IN CLOUD", key);
                                HashMap<String, String> temp = new HashMap<String, String>();
                                temp.put("Activity", key);
                                temp.put("Ranking", document.getString(key));
                                list.add(temp);
                            }
                        }
                        Log.d("DATA in LIST", list.toString());
                        loadListView();
                        loadNoRecordView(false);
                    }else{
                        Log.d("Data in Cloud", "Error getting documents" , task.getException());
                    }
                }
        });
    }
    private void loadLocal()
    {
        String[] activity = new String[] {"cooking","walking","cooking","walking","cooking","walking"};
        String[] ranking = new String[] {"10%", "90%","10%","90%","10%","90%"};
        for(int i = 0; i < activity.length; i++){
            HashMap<String,String> temp=new HashMap<String, String>();
            temp.put("Activity", activity[i]);
            temp.put("Ranking", ranking[i]);
            list.add(temp);
        }
    }
    private void loadButton(String date){
        Button button = (Button) findViewById(R.id.button_date);
        button.setText(date);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showDialog();
            }
        });
    }
    private void loadNoRecordView(Boolean flag){
        noRecordView = (TextView) findViewById(R.id.textView_noRecord);
        if(flag == true) noRecordView.setVisibility(View.VISIBLE);
        else noRecordView.setVisibility(View.GONE);
    }
    private void loadListView(){
        ListView listView=(ListView)findViewById(R.id.rankingListView);
        RankingAdapter adapter=new RankingAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                int pos=position+1;
                Toast.makeText(RankingActivity.this, Integer.toString(pos)+" Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new BasicFragment();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        Toast.makeText(RankingActivity.this, "Positive Click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        Toast.makeText(RankingActivity.this, "Negative Click", Toast.LENGTH_SHORT).show();

    }
}
