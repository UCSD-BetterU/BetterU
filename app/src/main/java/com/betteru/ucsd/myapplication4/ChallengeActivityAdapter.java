package com.betteru.ucsd.myapplication4;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuting on 11/5/2017.
 */

public class ChallengeActivityAdapter extends BaseAdapter {
    public ArrayList<UserModel> list = new ArrayList<>();
    public ArrayList<String> nameList;
    public ArrayList<Integer> iconList;
    Activity activity;
    ImageView imgIcon;
    TextView txtName;

    public ChallengeActivityAdapter(Activity activity,ArrayList<String> nameList, ArrayList<Integer> iconList){
        super();
        this.activity =activity;
        this.nameList = nameList;
        this.iconList = iconList;
        for(int i = 0; i < nameList.size(); i++)
        {
            UserModel user = new UserModel(nameList.get(i), iconList.get(i));
            list.add(user);
        }
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stuct
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

            convertView=inflater.inflate(R.layout.item_challenge_activities, null);
            txtName = (TextView) convertView.findViewById(R.id.textView_icon_name);
            imgIcon = (ImageView) convertView.findViewById(R.id.imageView_icon);
        }
        txtName.setText(nameList.get(position));
        imgIcon.setImageResource(iconList.get(position));
        return convertView;
    }
}
