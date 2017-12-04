package com.betteru.ucsd.myapplication4;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Yuting on 11/30/2017.
 */

public class ChallengeActivityResultAdapter extends BaseAdapter {
    public ArrayList<String> nameList;
    public ArrayList<String> activityList;
    public ArrayList<Integer> iconList;
    Activity activity;
    ImageView imgIcon;
    TextView txtActivity;
    TextView txtName;

    public ChallengeActivityResultAdapter(Activity activity,ArrayList<String> nameList, ArrayList<Integer> iconList, ArrayList<String> activityList){
        super();
        this.activity =activity;
        this.nameList = nameList;
        this.iconList = iconList;
        this.activityList = activityList;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stuct
        return nameList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return nameList.get(position);
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

            convertView=inflater.inflate(R.layout.item_challenge_winner, null);
            txtActivity = (TextView) convertView.findViewById(R.id.textView_winner_act);
            txtName = (TextView) convertView.findViewById(R.id.textView_winner);
            imgIcon = (ImageView) convertView.findViewById(R.id.imageView_winner_photo);
        }
        txtName.setText(nameList.get(position));
        imgIcon.setImageResource(iconList.get(position));
        txtActivity.setText(activityList.get(position).toUpperCase());
        return convertView;
    }
}
