package com.betteru.ucsd.myapplication4;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;

/**
 * Created by Yuting on 11/30/2017.
 */

public class ChallengeActivityResultAdapter extends BaseAdapter {
    ArrayList<String> nameList;
    ArrayList<String> activityList;
    ArrayList<String> idList;
    ArrayList<Integer> dataList;
    Activity activity;
    ProfilePictureView profile;
    TextView txtActivity;
    TextView txtName;

    public ChallengeActivityResultAdapter(Activity activity,ArrayList<String> nameList, ArrayList<String> idList, ArrayList<Integer> dataList, ArrayList<String> activityList){
        super();
        this.activity =activity;
        this.nameList = nameList;
        this.idList = idList;
        this.dataList = dataList;
        this.activityList = activityList;
    }
    @Override
    public int getCount() {
        return nameList.size();
    }

    @Override
    public Object getItem(int position) {
        return nameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater=activity.getLayoutInflater();
            convertView=inflater.inflate(R.layout.item_challenge_winner, null);
        }
        txtActivity = (TextView) convertView.findViewById(R.id.textView_winner_act);
        txtName = (TextView) convertView.findViewById(R.id.textView_winner);
        profile = (ProfilePictureView) convertView.findViewById(R.id.profileView_winner);

        txtName.setText(nameList.get(position));
        profile.setProfileId(idList.get(position));
        profile.setCropped(true);
        txtActivity.setText(activityList.get(position).toUpperCase() + " " +
                Integer.toString(dataList.get(position)) + " MIN");
        return convertView;
    }
}
