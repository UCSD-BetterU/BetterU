package com.betteru.ucsd.myapplication4.challenge;

/**
 * Created by Yuting on 12/11/2017.
 */

import android.util.Log;
import com.betteru.ucsd.myapplication4.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Yuting on 11/3/2017.
 */

public class ChallengeModel implements Serializable, Comparable<ChallengeModel> {
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

    @Override
    public int compareTo(ChallengeModel f) {

        if (date.before(f.date)) {
            return 1;
        }
        else if (date.after(f.date)) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
