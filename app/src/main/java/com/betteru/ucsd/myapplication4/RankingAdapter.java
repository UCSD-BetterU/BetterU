package com.betteru.ucsd.myapplication4;

/**
 * Created by Yuting on 10/30/2017.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class RankingAdapter extends BaseAdapter{

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    ImageView icon;

    public static final String FIRST_COLUMN="Activity";
    public static final String SECOND_COLUMN="Ranking";

    public RankingAdapter(Activity activity,ArrayList<HashMap<String, String>> list){
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
         ExtrasensoryActivities activities = new ExtrasensoryActivities();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.ranking_item, null);

            icon = (ImageView) convertView.findViewById(R.id.activity_icon);
            txtFirst=(TextView) convertView.findViewById(R.id.activity_content);
            txtSecond=(TextView) convertView.findViewById(R.id.activity_ranking);
        }

        HashMap<String, String> map=list.get(position);
        ExtrasensoryActivity activity = activities.map.get(map.get(FIRST_COLUMN));
        txtFirst.setText(activity.getLabel());
        txtSecond.setText(map.get(SECOND_COLUMN));
        icon.setImageResource(activity.getIcon());
        return convertView;
    }

}
