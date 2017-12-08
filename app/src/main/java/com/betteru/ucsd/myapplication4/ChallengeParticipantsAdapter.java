package com.betteru.ucsd.myapplication4;

/**
 * Created by Yuting on 12/7/2017.
 */

import android.app.Activity;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;


public class ChallengeParticipantsAdapter extends BaseAdapter {
    //public ArrayList<UserModel> list = new ArrayList<>();
    public ArrayList<String> nameList;
    public ArrayList<String> idList;
    Activity activity;
    TextView txtName;
    ImageView imgIcon;
    ProfilePictureView profile;

    public ChallengeParticipantsAdapter(Activity activity,ArrayList<String> nameList, ArrayList<String> idList){
        super();
        this.activity =activity;
        this.nameList = nameList;
        this.idList = idList;
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

            convertView=inflater.inflate(R.layout.item_challenge_activities, null);
            txtName = (TextView) convertView.findViewById(R.id.textView_icon_name);
            imgIcon = (ImageView) convertView.findViewById(R.id.imageView_icon);
            profile = (ProfilePictureView) convertView.findViewById(R.id.profileView_icon);
            profile = (ProfilePictureView) convertView.findViewById(R.id.profileView_icon);
        }
        txtName.setText(nameList.get(position));
        imgIcon.setVisibility(View.GONE);
        profile.setProfileId(idList.get(position));
        profile.setCropped(true);
        return convertView;
    }
}

