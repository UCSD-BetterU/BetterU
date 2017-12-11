package com.betteru.ucsd.myapplication4;

import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Yuting on 11/3/2017.
 */

public class ChallengeModel implements Serializable {
    String ownerId;
    String owner_name;
    String title;
    String timeStamp;
    ArrayList<String> activities;
    ArrayList<String> participants;
    ArrayList<String> participants_name;
    ArrayList<String> winner;
    ArrayList<String> winner_name;
    ArrayList<String> winner_data;
    ArrayList<Integer> activitiesIcon;
    public Calendar date = Calendar.getInstance();
    String id;

    //static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //cal.setTime(sdf.parse("Mon Mar 14 16:02:37 GMT 2011"));// all done
    ChallengeModel(String ownerId, String owner_name, String title, String timeStamp,
                   ArrayList<String> participants, ArrayList<String> participants_name, ArrayList<String> activities)
    {
        this.ownerId = ownerId;
        this.owner_name = owner_name;
        this.title = title;
        this.timeStamp = timeStamp;
        try {
            this.date.setTime(sdf.parse(timeStamp));
        }catch(Exception e)
        {
            Log.e("parse error", e.toString());
            this.date = Calendar.getInstance();
        }
        this.activities = activities;
        this.participants = participants;
        this.participants_name = participants_name;
        this.id = "";
        setActivityIcon();
    }
    public void setId(String id)
    {
        this.id = id;
    }

    private void setActivityIcon()
    {
        this.activitiesIcon = new ArrayList<>();
        for(int i = 0; i < this.activities.size(); i++)
        {
            try {
                ChallengeActivityEnum obj = ChallengeActivityEnum.get(activities.get(i));
                this.activitiesIcon.add(obj.getIcon());
            }catch(Exception e){
                this.activitiesIcon.add(R.drawable.ic_remove_circle_black_48dp);
            }
        }

    }

    public void setWinner(ArrayList<String> winner, ArrayList<String> winner_name, ArrayList<String> winner_data){
        this.winner = winner;
        this.winner_name = winner_name;
        this.winner_data = winner_data;
    }
}
