package com.betteru.ucsd.myapplication4;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Yuting on 11/3/2017.
 */

public class ChallengeAdapter extends BaseAdapter {
    public ArrayList<ChallengeModel> list;
    Activity activity;
    TextView txtTitle;
    TextView txtParticipantNum;
    TextView txtActivityNum;

    public ChallengeAdapter(Activity activity,ArrayList<ChallengeModel> list){
        super();
        this.activity=activity;
        this.list=list;
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
        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.item_challenge, null);
            txtTitle = (TextView) convertView.findViewById(R.id.challenge_title);
            txtParticipantNum =(TextView) convertView.findViewById(R.id.challenge_participants_number);
            txtActivityNum =(TextView) convertView.findViewById(R.id.challenge_activities_number);
        }
        ChallengeModel obj =list.get(position);
        txtTitle.setText(obj.title);
        txtParticipantNum.setText(Integer.toString(obj.participants.size()));
        txtActivityNum.setText(Integer.toString(obj.activities.size()));
        return convertView;
    }

}
