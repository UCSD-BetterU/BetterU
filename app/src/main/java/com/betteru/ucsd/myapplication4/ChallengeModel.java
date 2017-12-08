package com.betteru.ucsd.myapplication4;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Yuting on 11/3/2017.
 */

public class ChallengeModel implements Serializable {
    String ownerId;
    String title;
    String timeStamp;
    ArrayList<String> activities;
    ArrayList<String> participants;
    ArrayList<String> participants_name;
    ArrayList<String> winner;
    ArrayList<String> winner_name;
    ArrayList<Integer> activitiesIcon;
    LocalDate date;
    String id;

    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    ChallengeModel(String ownerId, String title, String timeStamp,
                   ArrayList<String> participants, ArrayList<String> participants_name, ArrayList<String> activities)
    {
        this.ownerId = ownerId;
        this.title = title;
        this.timeStamp = timeStamp;
        this.date = LocalDate.parse(timeStamp, formatter);
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

    public void setWinner(ArrayList<String> winner, ArrayList<String> winner_name){
        this.winner = winner;
        this.winner_name = winner_name;
    }
}
