package com.betteru.ucsd.myapplication4;

/**
 * Created by verazou on 11/19/2017.
 */

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FriendsAdapter extends BaseAdapter{

    public ArrayList<String> friendsList;

    Activity activity;
    TextView friendName;

    public static final String FIRST_COLUMN="Activity";
    public static final String SECOND_COLUMN="Ranking";

    public FriendsAdapter(Activity activity, ArrayList<String> friendsList){
        super();
        this.activity = activity;
        this.friendsList=friendsList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return friendsList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return friendsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = activity.getLayoutInflater();

        if(convertView == null){

            convertView = inflater.inflate(R.layout.friend_item, null);

            friendName = (TextView) convertView.findViewById(R.id.friend_name);
        }

        String name = friendsList.get(position);
        friendName.setText(name);
        return convertView;
    }

}
