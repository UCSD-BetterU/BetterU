package com.betteru.ucsd.myapplication4;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Yuting on 11/3/2017.
 */

public class ChallengeAdapter extends BaseAdapter {
    public ArrayList<ChallengeModel> list;
    Activity activity;
    TextView txtTitle;
    TextView txtDate;
    TextView txtParticipantNum;
    TextView txtActivityNum;
    ImageView imgStatus;

    public ChallengeAdapter(Activity activity,ArrayList<ChallengeModel> list){
        super();
        this.activity=activity;
        this.list=list;
    }
    public void refresh(ArrayList<ChallengeModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub


        if(convertView == null){
            LayoutInflater inflater=activity.getLayoutInflater();
            convertView=inflater.inflate(R.layout.item_challenge, null);
        }
        txtTitle = (TextView) convertView.findViewById(R.id.challenge_title);
        txtParticipantNum =(TextView) convertView.findViewById(R.id.challenge_participants_number);
        txtActivityNum =(TextView) convertView.findViewById(R.id.challenge_activities_number);
        txtDate = (TextView) convertView.findViewById(R.id.textView_challengeDate);
        imgStatus = (ImageView) convertView.findViewById(R.id.imageView_challengeIcon);

        ChallengeModel obj =list.get(position);
        txtTitle.setText(obj.title);
        txtParticipantNum.setText(Integer.toString(obj.participants.size()));
        txtActivityNum.setText(Integer.toString(obj.activities.size()));
        txtDate.setText(obj.sdf.format(obj.date.getTime()));
        if(obj.date.after(Calendar.getInstance()))
            imgStatus.setImageResource(R.color.colorChallengeFuture);
        else if(obj.winner == null)
            imgStatus.setImageResource(R.color.colorChallengeNear);
        else
            imgStatus.setImageResource(R.color.colorChallengeEnd);
        return convertView;
    }

}
