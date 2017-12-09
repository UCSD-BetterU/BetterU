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

import com.facebook.login.widget.ProfilePictureView;

public class FriendsAdapter extends BaseAdapter{

    public ArrayList<UserModel> friendsList;

    Activity activity;
    TextView friendNameView;
    ProfilePictureView profilePicView;

    public static final String FIRST_COLUMN="Activity";
    public static final String SECOND_COLUMN="Ranking";

    public FriendsAdapter(Activity activity, ArrayList<UserModel> friendsList){
        super();
        this.activity = activity;
        this.friendsList=friendsList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (friendsList == null)
            return 0;
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
            profilePicView = (ProfilePictureView) convertView.findViewById(R.id.friend_profile);
            friendNameView = (TextView) convertView.findViewById(R.id.friend_name);
        }

        profilePicView.setProfileId(friendsList.get(position).getUserId());
        profilePicView.setCropped(true);

        String name = friendsList.get(position).getName();
        friendNameView.setText(name);
        return convertView;
    }

}
