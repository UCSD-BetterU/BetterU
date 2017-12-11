package com.betteru.ucsd.myapplication4.challenge;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.betteru.ucsd.myapplication4.R;
import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;

/**
 * Created by Yuting on 12/11/2017.
 */

public class ChallengeActivityListAdapter extends BaseAdapter {
    public ArrayList<String> nameList;
    public ArrayList<Integer> iconList;
    Activity activity;
    ImageView imgIcon;
    TextView txtName;
    ProfilePictureView profile;

    public ChallengeActivityListAdapter(Activity activity,ArrayList<String> nameList, ArrayList<Integer> iconList){
        super();
        this.activity = activity;
        this.nameList = nameList;
        this.iconList = iconList;
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
            convertView=inflater.inflate(R.layout.item_challenge_activities, null);
        }

        txtName = (TextView) convertView.findViewById(R.id.textView_icon_name);
        imgIcon = (ImageView) convertView.findViewById(R.id.imageView_icon);
        profile = (ProfilePictureView) convertView.findViewById(R.id.profileView_icon);

        txtName.setText(nameList.get(position));
        imgIcon.setImageResource(iconList.get(position));
        profile.setVisibility(View.GONE);
        return convertView;
    }
}
