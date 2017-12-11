package com.betteru.ucsd.myapplication4;

/**
 * Created by Yuting on 10/30/2017.
 */

import android.app.Activity;
import android.graphics.Typeface;
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
    TextView txt1, txt2, txt3, txt4;
    ImageView icon;

    public static final String FIRST_COLUMN="Activity";
    public static final String SECOND_COLUMN="Ranking";
    public static final String THIRD_COLUMN="Users";
    public static final String COLOR = "Color";

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
        if(convertView == null){
            LayoutInflater inflater=activity.getLayoutInflater();
            convertView=inflater.inflate(R.layout.ranking_item, null);
        }

        ExtrasensoryActivities activities = new ExtrasensoryActivities();

        icon = (ImageView) convertView.findViewById(R.id.activity_icon);
        txt1=(TextView) convertView.findViewById(R.id.activity_content);
        txt2=(TextView) convertView.findViewById(R.id.activity_ranking_1);
        txt3=(TextView) convertView.findViewById(R.id.activity_ranking_2);
        txt4=(TextView) convertView.findViewById(R.id.activity_ranking_3);
        HashMap<String, String> map=list.get(position);
        ExtrasensoryActivity activity = activities.map.get(map.get(FIRST_COLUMN));
        txt1.setText(activity.getLabel());
        txt2.setText("Top ");
        txt3.setText(map.get(SECOND_COLUMN));
        txt4.setText(map.get(THIRD_COLUMN));
        String color = map.get(COLOR);
        if (!color.equals("")) {
            //txt2.setTextColor(Integer.valueOf(color));
            txt3.setTextColor(Integer.valueOf(color));
            //txt4.setTextColor(Integer.valueOf(color));
            txt3.setTypeface(null, Typeface.BOLD);
        }
        icon.setImageResource(activity.getIcon());
        return convertView;
    }

}
