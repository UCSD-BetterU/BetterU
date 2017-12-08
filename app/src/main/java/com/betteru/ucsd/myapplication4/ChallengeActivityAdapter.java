package com.betteru.ucsd.myapplication4;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuting on 11/5/2017.
 */

public class ChallengeActivityAdapter extends BaseAdapter {
    //public ArrayList<UserModel> list = new ArrayList<>();
    public ArrayList<String> nameList;
    public ArrayList<Integer> iconList;
    Activity activity;
    ImageView imgIcon;
    TextView txtName;
    ProfilePictureView profile;

    public ChallengeActivityAdapter(Activity activity,ArrayList<String> nameList, ArrayList<Integer> iconList){
        super();
        this.activity =activity;
        this.nameList = nameList;
        this.iconList = iconList;
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
